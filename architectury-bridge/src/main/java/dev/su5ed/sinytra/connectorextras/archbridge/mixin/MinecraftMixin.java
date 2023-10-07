package dev.su5ed.sinytra.connectorextras.archbridge.mixin;

import dev.su5ed.sinytra.connectorextras.archbridge.ArchitecturyBridge;
import dev.su5ed.sinytra.connectorextras.archbridge.ArchitecturyBridgeSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 100)
public abstract class MinecraftMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"), remap = false)
    private void archbridgeEarlyInit(GameConfig gameConfig, CallbackInfo ci) {
        if (ArchitecturyBridge.isEnabled()) {
            ArchitecturyBridgeSetup.setup();
        }
    }
}
