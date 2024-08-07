package dev.su5ed.sinytra.connectorextras.jeibridge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import mezz.jei.api.IModPlugin;
import mezz.jei.neoforge.startup.ForgePluginFinder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ForgePluginFinder.class)
public abstract class ForgePluginFinderMixin {
    @ModifyReturnValue(method = "getModPlugins", at = @At("RETURN"), remap = false)
    private static List<IModPlugin> connectorextras_jei_bridge$addFabricPlugins(List<IModPlugin> forgePlugins) {
        List<IModPlugin> fabricPlugins = FabricLoader.getInstance()
                .getEntrypointContainers("jei_mod_plugin", IModPlugin.class)
                .stream()
                .map(EntrypointContainer::getEntrypoint)
                .toList();
        List<IModPlugin> merged = new ArrayList<>();
        merged.addAll(forgePlugins);
        merged.addAll(fabricPlugins);
        return merged;
    }
}
