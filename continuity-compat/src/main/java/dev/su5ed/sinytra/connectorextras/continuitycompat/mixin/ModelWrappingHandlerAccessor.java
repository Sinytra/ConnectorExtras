package dev.su5ed.sinytra.connectorextras.continuitycompat.mixin;

import com.google.common.collect.ImmutableMap;
import me.pepperbell.continuity.client.resource.ModelWrappingHandler;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModelWrappingHandler.class)
public interface ModelWrappingHandlerAccessor {
    @Accessor
    @Mutable
    static void setBLOCK_STATE_MODEL_IDS(ImmutableMap<ModelResourceLocation, BlockState> map) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    static ImmutableMap<ModelResourceLocation, BlockState> callCreateBlockStateModelIdMap() {
        throw new UnsupportedOperationException();
    }
}
