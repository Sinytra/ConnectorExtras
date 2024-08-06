package dev.su5ed.sinytra.connectorextras.util;

import com.mojang.logging.LogUtils;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSigner;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Manifest;

import static cpw.mods.modlauncher.api.LambdaExceptionUtils.uncheck;

public class HackyModuleInjector {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Unsafe UNSAFE = uncheck(() -> {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    });

    public static boolean injectModuleSources(String moduleName, URL source) {
        try {
            ModuleLayer layer = Launcher.INSTANCE.findLayerManager().orElseThrow().getLayer(IModuleLayerManager.Layer.GAME).orElseThrow();
            ResolvedModule module = layer.configuration().findModule(moduleName).orElse(null);
            if (module == null) {
                LOGGER.warn("Module {} is not present, skipping class injection", moduleName);
                return false;
            }

            Class<?> jarModuleReference = Class.forName("cpw.mods.cl.JarModuleFinder$JarModuleReference");
            ModuleReference reference = module.reference();
            if (!jarModuleReference.isInstance(reference)) {
                LOGGER.error("Module {} does not contain a jar module reference", moduleName);
                return false;
            }

            Field jarField = jarModuleReference.getDeclaredField("jar");
            SecureJar.ModuleDataProvider originalProvider = (SecureJar.ModuleDataProvider) UNSAFE.getObject(reference, UNSAFE.objectFieldOffset(jarField));
            Path relocatePath = getRelocatedClassesPath(source);
            if (relocatePath == null) {
                LOGGER.error("Source path for {} not found", moduleName);
                return false;
            }
            SecureJar.ModuleDataProvider wrappedProvider = new ModuleDataProviderWrapper(originalProvider, relocatePath);
            UNSAFE.putObject(reference, UNSAFE.objectFieldOffset(jarField), wrappedProvider);

            LOGGER.debug("Successfully injected source {} into module {}", source, moduleName);
            return true;
        } catch (Throwable t) {
            LOGGER.error("Error injecting classes into module {}", moduleName, t);
            return false;
        }
    }

    private static Path getRelocatedClassesPath(URL source) throws URISyntaxException, IOException {
        if (source != null) {
            Path sourcePath = Path.of(source.toURI());
            URI pathFsUri = new URI("path://" + source.getPath());
            FileSystem pathFS = FileSystems.newFileSystem(pathFsUri, Map.of("packagePath", sourcePath));
            return pathFS.getPath("/");
        }
        return null;
    }

    public record ModuleDataProviderWrapper(SecureJar.ModuleDataProvider provider, Path relocatePath) implements SecureJar.ModuleDataProvider {
        @Override
        public Optional<InputStream> open(String name) {
            return provider.open(name)
                .or(() -> {
                    try {
                        Path path = relocatePath.resolve(name);
                        return Optional.of(Files.newInputStream(path));
                    } catch (Exception ignored) {
                    }
                    return Optional.empty();
                });
        }

        //@formatter:off
        @Override public String name() {return provider.name();}
        @Override public ModuleDescriptor descriptor() {return provider.descriptor();}
        @Override public URI uri() {return provider.uri();}
        @Override public Optional<URI> findFile(String name) {return provider.findFile(name);}
        @Override public Manifest getManifest() {return provider.getManifest();}
        @Override public CodeSigner[] verifyAndGetSigners(String cname, byte[] bytes) {return provider.verifyAndGetSigners(cname, bytes);}
        //@formatter:on
    }
}
