package dev.su5ed.sinytra.connectorextras.archbridge.mixin;

import dev.su5ed.sinytra.connectorextras.archbridge.ArchitecturyBridge;
import dev.su5ed.sinytra.connectorextras.archbridge.ArchitecturyBridgeSetup;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Main.class, priority = 100)
public abstract class ServerMainMixin {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/server/loading/ServerModLoader;load()V"), remap = false)
    private static void archbridgeEarlyInit(CallbackInfo ci) {
        if (ArchitecturyBridge.isEnabled()) {
            ArchitecturyBridgeSetup.setup();
        }
    }
}
