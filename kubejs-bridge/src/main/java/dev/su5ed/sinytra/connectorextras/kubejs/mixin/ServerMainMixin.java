package dev.su5ed.sinytra.connectorextras.kubejs.mixin;

import dev.su5ed.sinytra.connectorextras.kubejs.KubeJSCompat;
import dev.su5ed.sinytra.connectorextras.kubejs.KubeJSCompatSetup;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Main.class, priority = 100)
public abstract class ServerMainMixin {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/loading/ServerModLoader;load()V"), remap = false)
    private static void kubejsBridgeEarlyInit(CallbackInfo ci) {
        if (KubeJSCompat.isEnabled()) {
            KubeJSCompatSetup.initFabricPlugins();
        }
    }
}
