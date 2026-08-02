package org.sinytra.connectorextras.jeibridge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import mezz.jei.library.startup.JeiStarter;
import mezz.jei.neoforge.JustEnoughItemsClient;
import org.sinytra.connectorextras.jeibridge.JEIBridgeSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JustEnoughItemsClient.class)
public class JustEnoughItemsClientMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureStarter(CallbackInfo ci, @Local JeiStarter starter) {
        JEIBridgeSetup.setJeiStarter(starter);
    }
}
