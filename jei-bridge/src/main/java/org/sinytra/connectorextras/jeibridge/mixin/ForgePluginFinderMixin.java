package org.sinytra.connectorextras.jeibridge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mezz.jei.neoforge.startup.ForgePluginFinder;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ForgePluginFinder.class)
public class ForgePluginFinderMixin {

    @ModifyExpressionValue(method = "getInstances", at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/ModList;getAllScanData()Ljava/util/List;"))
    private static List<ModFileScanData> skipFabricCompatibleInstances(List<ModFileScanData> allScanData) {
        // Remove scan data from "Fabric" mods to avoid loading them too early
        return allScanData.stream()
            .filter(data -> !data.getIModInfoData().stream()
                .allMatch(mod -> mod.getFileProperties().get("launchpad:active") == Boolean.TRUE))
            .toList();
    }
}
