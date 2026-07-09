package org.sinytra.connectorextras.reibridge.mixin.client;

import org.sinytra.connectorextras.reibridge.REIBridgeSetup;
import me.shedaniel.rei.RoughlyEnoughItemsCoreClient;
import me.shedaniel.rei.impl.init.PluginDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RoughlyEnoughItemsCoreClient.class)
public class RoughlyEnoughItemsCoreClientMixin {

    @Inject(method = "onInitializeClient", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/RoughlyEnoughItemsCore;getPluginDetector()Lme/shedaniel/rei/impl/init/PluginDetector;"))
    private void initializeFabricPluginClient(CallbackInfo ci) {
        PluginDetector detector = REIBridgeSetup.FABRIC_PLUGIN_DETECTOR.get();
        detector.detectClientPlugins().get().run();
    }
}
