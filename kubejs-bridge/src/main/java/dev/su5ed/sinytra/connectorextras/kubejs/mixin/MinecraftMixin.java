package dev.su5ed.sinytra.connectorextras.kubejs.mixin;

import dev.su5ed.sinytra.connectorextras.kubejs.KubeJSCompat;
import dev.su5ed.sinytra.connectorextras.kubejs.KubeJSCompatSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 100)
public abstract class MinecraftMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"), remap = false)
    private void kubejsBridgeEarlyInit(GameConfig gameConfig, CallbackInfo ci) {
        if (KubeJSCompat.isEnabled()) {
            KubeJSCompatSetup.initFabricPlugins();
        }
    }
}
