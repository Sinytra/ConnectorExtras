package dev.su5ed.sinytra.connectorextras.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugins;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforgespi.language.IModInfo;
import net.neoforged.neoforgespi.locating.IModFile;
import org.sinytra.connector.ConnectorEarlyLoader;

import java.util.List;
import java.util.function.Consumer;

public class KubeJSCompatSetup {
    public static List<String> loadedMods;
    public static List<KubeJSPlugin> fabricPlugins;

    public static void initFabricPlugins() {
        List<IModInfo> mods = ConnectorEarlyLoader.getConnectorMods();
        loadedMods = mods.stream().map(IModInfo::getModId).toList();
        List<IModFile> files = mods.stream().map(m -> m.getOwningFile().getFile()).toList();

        KubeJSPlugins.load(files, FMLEnvironment.dist.isClient());
        fabricPlugins = List.copyOf(KubeJSPlugins.getAll());

        KubeJSPlugins.forEachPlugin(KubeJSPlugin::init);
        KubeJSPlugins.forEachPlugin(KubeJSPlugin::initStartup);
    }

    public static void forForgePluginsOnly(Consumer<KubeJSPlugin> callback) {
        KubeJSPlugins.forEachPlugin(plugin -> {
            if (!fabricPlugins.contains(plugin)) {
                callback.accept(plugin);
            }
        });
    }
}
