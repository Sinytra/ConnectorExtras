package dev.su5ed.sinytra.connectorextras.modmenu.mixin;

import dev.su5ed.sinytra.connectorextras.modmenu.ModMenuCompatSetup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftMixin {

    @Inject(method = "lambda$new$4", at = @At("HEAD"))
    private void initModMenuCompat(RealmsClient realmsclient, ResourceReload reloadinstance, RunArgs arg, CallbackInfo ci) {
        ModMenuCompatSetup.init();
    }
}
