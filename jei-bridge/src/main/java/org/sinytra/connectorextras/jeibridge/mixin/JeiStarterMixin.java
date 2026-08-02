package org.sinytra.connectorextras.jeibridge.mixin;

import mezz.jei.api.IModPlugin;
import mezz.jei.common.config.ConfigManager;
import mezz.jei.library.load.PluginCaller;
import mezz.jei.library.load.PluginHelper;
import mezz.jei.library.plugins.jei.JeiInternalPlugin;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import mezz.jei.library.startup.JeiStarter;
import org.sinytra.connectorextras.jeibridge.JEIBridgeSetup;
import org.sinytra.connectorextras.jeibridge.JeiStarterExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(JeiStarter.class)
public class JeiStarterMixin implements JeiStarterExtension {
    @Shadow
    @Final
    private List<IModPlugin> plugins;
    @Shadow
    @Final
    private VanillaPlugin vanillaPlugin;
    @Shadow
    @Final
    private ConfigManager configManager;

    @Override
    @Unique
    public void loadFabricPlugins() {
        List<IModPlugin> fabricPlugins = JEIBridgeSetup.getFabricPlugins();
        this.plugins.addAll(fabricPlugins);

        JeiInternalPlugin jeiInternalPlugin = PluginHelper.getPluginWithClass(JeiInternalPlugin.class, this.plugins).orElse(null);
        PluginHelper.sortPlugins(this.plugins, this.vanillaPlugin, jeiInternalPlugin);

        PluginCaller.callOnPlugins("Sending ConfigManager (Fabric)", fabricPlugins, p -> p.onConfigManagerAvailable(this.configManager));
    }
}
