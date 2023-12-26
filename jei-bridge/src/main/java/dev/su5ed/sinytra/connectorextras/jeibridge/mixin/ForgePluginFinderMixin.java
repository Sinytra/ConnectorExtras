package dev.su5ed.sinytra.connectorextras.jeibridge.mixin;

import mezz.jei.api.IModPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ForgePluginFinder.class)
public abstract class ForgePluginFinderMixin {
    @Inject(method = "getModPlugins", at = @At("RETURN"), remap = false, cancellable = true)
    private static void connectorextras_jei_bridge$addFabricPlugins(CallbackInfoReturnable<List<IModPlugin>> cir) {
        List<IModPlugin> forgePlugins = cir.getReturnValue();
        List<IModPlugin> fabricPlugins = FabricLoader.getInstance()
                .getEntrypointContainers("jei_mod_plugin", IModPlugin.class)
                .stream()
                .map(EntrypointContainer::getEntrypoint)
                .toList();
        List<IModPlugin> merged = new ArrayList<>();
        merged.addAll(forgePlugins);
        merged.addAll(fabricPlugins);
        cir.setReturnValue(merged);
    }
}
