package org.sinytra.connectorextras.util;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.classloading.ModuleClassLoader;
import net.neoforged.fml.jarcontents.JarContents;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFile;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class HackyModuleInjector {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Unsafe UNSAFE = uncheck(() -> {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    });
    @SuppressWarnings("removal")
    public static final MethodHandles.Lookup TRUSTED_LOOKUP = uncheck(() -> {
        Field hackfield = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        return (MethodHandles.Lookup) UNSAFE.getObject(UNSAFE.staticFieldBase(hackfield), UNSAFE.staticFieldOffset(hackfield));
    });

    private static final VarHandle MF_JAR_CONTENTS = uncheck(() -> TRUSTED_LOOKUP.findVarHandle(ModFile.class, "contents", JarContents.class));

    private static final Class<?> MOD_INFO = uncheck(() -> Class.forName("net.neoforged.fml.classloading.ModuleClassLoader$ModuleInfo"));
    private static final VarHandle MOD_CACHED_READER = uncheck(() -> TRUSTED_LOOKUP.findVarHandle(MOD_INFO, "cachedReader", ModuleReader.class));

    private static final VarHandle MOD_INFO_CACHE = uncheck(() -> TRUSTED_LOOKUP.findVarHandle(ModuleClassLoader.class, "moduleInfoCache", Map.class));
    private static final Class<?> JAR_MOD_REF = uncheck(() -> Class.forName("net.neoforged.fml.classloading.JarContentsModuleReference"));
    private static final VarHandle JAR_MOD_CONTENTS = uncheck(() -> TRUSTED_LOOKUP.findVarHandle(JAR_MOD_REF, "contents", JarContents.class));

    public static boolean injectModuleSource(String modName, String moduleName, Path source) {
        try {
            ModuleLayer layer = FMLLoader.getCurrent().getGameLayer();
            ResolvedModule module = layer.configuration().findModule(moduleName).orElse(null);
            if (module == null) {
                LOGGER.warn("Module {} is not present, skipping class injection", moduleName);
                return false;
            }

            Class<?> jarModuleReference = Class.forName("net.neoforged.fml.classloading.JarContentsModuleReference");
            ModuleReference reference = module.reference();
            if (!jarModuleReference.isInstance(reference)) {
                LOGGER.error("Module {} does not contain a jar module reference", moduleName);
                return false;
            }

            LoadingModList list = FMLLoader.getCurrent().getLoadingModList();
            ModFile modFile = list.getModFileById(modName).getFile();
            JarContents original = modFile.getContents();
            JarContents combined = JarContents.ofPaths(List.of(original.getPrimaryPath(), source));

            MF_JAR_CONTENTS.set(modFile, combined);
            JAR_MOD_CONTENTS.set(reference, combined);

            ModuleClassLoader cl = (ModuleClassLoader) Thread.currentThread().getContextClassLoader();
            Map<String, Object> moduleInfoCache = (Map<String, Object>) MOD_INFO_CACHE.get(cl);

            Object moduleInfo = moduleInfoCache.get(moduleName);
            if (moduleInfo != null) {
                MOD_CACHED_READER.set(moduleInfo, null);
            }

            LOGGER.debug("Successfully injected source {} into module {}", source, moduleName);
            return true;
        } catch (Throwable t) {
            LOGGER.error("Error injecting classes into module {}", moduleName, t);
            return false;
        }
    }

    private static <T> T uncheck(Callable<T> t) {
        try {
            return t.call();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
