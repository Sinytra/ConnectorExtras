package dev.su5ed.sinytra.connectorextras.emibridge.mixin;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.platform.forge.EmiAgnosForge;
import dev.emi.emi.registry.EmiPluginContainer;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(EmiAgnosForge.class)
public class EmiAgnosForgeMixin {
    @ModifyVariable(method = "getPluginsAgnos", at = @At("RETURN"), remap = false)
    private List<EmiPluginContainer> connectorextras_emi_bridge$addFabricPlugins(List<EmiPluginContainer> orig) {
        Set<String> ids = orig.stream().map(EmiPluginContainer::id).collect(Collectors.toSet());
        orig.addAll(FabricLoader.getInstance()
            .getEntrypointContainers("emi", EmiPlugin.class)
            .stream()
            .filter(plugin -> !ids.contains(plugin.getProvider().getMetadata().getId()))
            .map(p -> new EmiPluginContainer(p.getEntrypoint(), p.getProvider().getMetadata().getId()))
            .toList());
        return orig;
    }
}
