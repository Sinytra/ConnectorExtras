package dev.su5ed.sinytra.connectorextras.modmenu.mixin;

import com.mojang.realmsclient.client.RealmsClient;
import dev.su5ed.sinytra.connectorextras.modmenu.ModMenuCompatSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "lambda$new$4", at = @At("HEAD"))
    private void initModMenuCompat(RealmsClient realmsclient, ReloadInstance reloadinstance, GameConfig arg, CallbackInfo ci) {
        ModMenuCompatSetup.init();
    }
}
