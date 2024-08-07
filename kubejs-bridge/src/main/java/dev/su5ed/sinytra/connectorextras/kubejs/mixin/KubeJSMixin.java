package dev.su5ed.sinytra.connectorextras.kubejs.mixin;

import dev.architectury.platform.Mod;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.su5ed.sinytra.connectorextras.kubejs.KubeJSCompatSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Consumer;

@Mixin(KubeJS.class)
public abstract class KubeJSMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/plugin/KubeJSPlugins;load(Ljava/util/List;Z)V"))
    private List<Mod> removeInitializedMods(List<Mod> mods) {
        return mods.stream().filter(m -> !KubeJSCompatSetup.loadedMods.contains(m.getModId())).toList();
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/plugin/KubeJSPlugins;forEachPlugin(Ljava/util/function/Consumer;)V"))
    private void initNeoForgePlugins(Consumer<KubeJSPlugin> callback) {
        KubeJSCompatSetup.forForgePluginsOnly(callback);
    }
}
