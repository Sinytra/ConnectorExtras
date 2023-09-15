package dev.su5ed.sinytra.connectorextras.reibridge.mixin;

import dev.su5ed.sinytra.connectorextras.reibridge.REIBridge;
import me.shedaniel.rei.RoughlyEnoughItemsCore;
import me.shedaniel.rei.impl.init.PluginDetector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RoughlyEnoughItemsCore.class)
public class RoughlyEnoughItemsCoreMixin {

    @Inject(method = "onInitialize", at = @At("HEAD"), remap = false)
    private void initializeFabricPlugin(CallbackInfo ci) {
        PluginDetector detector = REIBridge.FABRIC_PLUGIN_DETECTOR.get();
        detector.detectCommonPlugins();
        detector.detectServerPlugins();
    }
}
