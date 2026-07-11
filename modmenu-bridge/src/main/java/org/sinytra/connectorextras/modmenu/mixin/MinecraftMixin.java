package org.sinytra.connectorextras.modmenu.mixin;

import net.minecraft.client.Minecraft;
import org.sinytra.connectorextras.modmenu.ModMenuCompatSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    // Inside setOverlay -> LoadingOverlay -> Util.ifElse
    @Inject(method = "lambda$new$6", at = @At("HEAD"))
    private void initModMenuCompat(CallbackInfo ci) {
        ModMenuCompatSetup.init();
    }
}
