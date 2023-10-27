package dev.su5ed.sinytra.connectorextras.continuitycompat.mixin;

import com.google.common.collect.ImmutableMap;
import me.pepperbell.continuity.client.resource.ModelWrappingHandler;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelWrappingHandler.class)
public class ModelWrappingHandlerMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lme/pepperbell/continuity/client/resource/ModelWrappingHandler;createBlockStateModelIdMap()Lcom/google/common/collect/ImmutableMap;"))
    private static ImmutableMap<ModelResourceLocation, BlockState> onClinit() {
        return ImmutableMap.of();
    }
}
