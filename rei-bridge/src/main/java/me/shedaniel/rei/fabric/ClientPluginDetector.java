package me.shedaniel.rei.fabric;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.common.plugins.PluginManager;

public final class ClientPluginDetector {

    public static void detectClientPlugins() {
        PluginDetectorImpl.loadPlugin(REIClientPlugin.class, PluginManager.getClientInstance().view()::registerPlugin);
    }
}
