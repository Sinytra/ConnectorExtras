package dev.su5ed.sinytra.connectorextras.emibridge;

import com.mojang.logging.LogUtils;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.unsafe.UnsafeHacks;
import org.slf4j.Logger;

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

@Mod("connectorextras_emi_bridge")
public class EMIBridge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void injectModule() {
        try {
            ModuleLayer layer = Launcher.INSTANCE.findLayerManager().orElseThrow().getLayer(IModuleLayerManager.Layer.GAME).orElseThrow();
            ResolvedModule emiModule = layer.configuration().findModule("emi").orElse(null);
            if (emiModule == null) {
                LOGGER.debug("EMI is not present, skipping class injection");
                return;
            }

            Class<?> jarModuleReference = Class.forName("cpw.mods.cl.JarModuleFinder$JarModuleReference");
            ModuleReference reference = emiModule.reference();
            if (!jarModuleReference.isInstance(reference)) {
                LOGGER.error("EMI module does not contain a jar module reference");
                return;
            }

            Field jarField = jarModuleReference.getDeclaredField("jar");
            SecureJar.ModuleDataProvider originalProvider = UnsafeHacks.getField(jarField, reference);
            Path relocatePath = getRelocatedClassesPath();
            if (relocatePath == null) {
                LOGGER.error("EMI API relocated path not found");
                return;
            }
            SecureJar.ModuleDataProvider wrappedProvider = new ModuleDataProviderWrapper(originalProvider, relocatePath);
            UnsafeHacks.setField(jarField, reference, wrappedProvider);

            LOGGER.debug("Successfully injected EMI Fabric API classes");
        } catch (Throwable t) {
            LOGGER.error("Error injecting EMI Fabric API classes", t);
        }
    }

    public static Path getRelocatedClassesPath() throws URISyntaxException, IOException {
        URL relocateUrl = EMIBridge.class.getResource("/relocate");
        if (relocateUrl != null) {
            Path relocatePath = Path.of(relocateUrl.toURI());
            URI pathFsUri = new URI("path://" + relocateUrl.getPath());
            FileSystem pathFS = FileSystems.newFileSystem(pathFsUri, Map.of("packagePath", relocatePath));
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
