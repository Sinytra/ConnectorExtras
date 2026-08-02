package org.sinytra.connectorextras.jeibridge;

import mezz.jei.api.IModPlugin;
import mezz.jei.library.startup.JeiStarter;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

public class JEIBridgeSetup {
    private static JeiStarter jeiStarter;

    public static void setJeiStarter(JeiStarter starter) {
        jeiStarter = starter;
    }

    public static List<IModPlugin> getFabricPlugins() {
        return FabricLoader.getInstance()
            .getEntrypointContainers("jei_mod_plugin", IModPlugin.class)
            .stream()
            .map(EntrypointContainer::getEntrypoint)
            .toList();
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        if (jeiStarter != null) {
            ((JeiStarterExtension) (Object) jeiStarter).loadFabricPlugins();
        }
    }
}
