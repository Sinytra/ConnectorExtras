package dev.su5ed.sinytra.connectorextras.modmenu.mixin;

import dev.su5ed.sinytra.connectorextras.modmenu.ModMenuCompat;
import net.minecraftforge.client.gui.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModListScreen.class)
public class ModListScreenMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void initModMenuCompat(CallbackInfo ci) {
        ModMenuCompat.init();
    }
}
