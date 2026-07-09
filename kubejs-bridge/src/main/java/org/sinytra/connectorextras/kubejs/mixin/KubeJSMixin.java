package org.sinytra.connectorextras.kubejs.mixin;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import net.neoforged.neoforgespi.locating.IModFile;
import org.sinytra.connectorextras.kubejs.KubeJSCompatSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(KubeJS.class)
public abstract class KubeJSMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/base/Stopwatch;createStarted()Lcom/google/common/base/Stopwatch;"))
    private void initFabricSetup(CallbackInfo ci) {
        KubeJSCompatSetup.initFabricPlugins();
    }
    
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/plugin/KubeJSPlugins;load(Ljava/util/List;Z)V"))
    private List<IModFile> removeInitializedMods(List<IModFile> mods) {
        return KubeJSCompatSetup.filterInitializedMods(mods);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ldev/latvian/mods/kubejs/plugin/KubeJSPlugins;forEachPlugin(Ljava/util/function/Consumer;)V"))
    private void initNeoForgePlugins(Consumer<KubeJSPlugin> callback) {
        KubeJSCompatSetup.forForgePluginsOnly(callback);
    }
}
