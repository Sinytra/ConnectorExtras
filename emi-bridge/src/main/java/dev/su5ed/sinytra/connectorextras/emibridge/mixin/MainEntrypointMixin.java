package dev.su5ed.sinytra.connectorextras.emibridge.mixin;

import dev.su5ed.sinytra.connectorextras.emibridge.EMIBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({net.minecraft.client.main.Main.class, net.minecraft.server.Main.class})
public class MainEntrypointMixin {

    @Inject(method = "main", at = @At("HEAD"), remap = false)
    private static void beforeGameLaunch(String[] strings, CallbackInfo ci) {
        EMIBridge.injectModule();
    }
}
