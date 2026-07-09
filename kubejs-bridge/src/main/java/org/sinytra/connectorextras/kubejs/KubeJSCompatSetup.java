package org.sinytra.connectorextras.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugins;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KubeJSCompatSetup {
    public static Set<IModFile> loadedMods;
    public static List<KubeJSPlugin> fabricPlugins;

    public static void initFabricPlugins() {
        List<IModInfo> mods = FMLLoader.getCurrent().getLoadingModList().getMods().stream()
            .filter(KubeJSCompatSetup::isConnectorMod)
            .<IModInfo>map(Function.identity())
            .toList();
        loadedMods = mods.stream()
            .map(m -> m.getOwningFile().getFile())
            .collect(Collectors.toUnmodifiableSet());

        KubeJSPlugins.load(List.copyOf(loadedMods), FMLEnvironment.getDist().isClient());
        fabricPlugins = List.copyOf(KubeJSPlugins.getAll());

        KubeJSPlugins.forEachPlugin(KubeJSPlugin::init);
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::initStartup);
    }

    public static List<IModFile> filterInitializedMods(List<IModFile> mods) {
        return mods.stream()
            .filter(m -> !KubeJSCompatSetup.loadedMods.contains(m))
            .toList();
    }

    public static void forForgePluginsOnly(Consumer<KubeJSPlugin> callback) {
        KubeJSPlugins.forEachPlugin(plugin -> {
            if (!fabricPlugins.contains(plugin)) {
                callback.accept(plugin);
            }
        });
    }

    private static boolean isConnectorMod(IModInfo modInfo) {
        return FabricLoader.getInstance().getModContainer(modInfo.getModId())
            .map(c -> c.getMetadata().getCustomValue("connector:active"))
            .map(CustomValue::getAsBoolean)
            .orElse(false);
    }
}
