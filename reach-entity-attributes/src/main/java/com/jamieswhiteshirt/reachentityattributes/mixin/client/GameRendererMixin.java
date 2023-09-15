package com.jamieswhiteshirt.reachentityattributes.mixin.client;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin implements SynchronousResourceReloader/*, AutoCloseable*/ {
    @Shadow @Final MinecraftClient client;

    @ModifyVariable(method = "updateTargetedEntity(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1), ordinal = 4)
    private double getActualAttackRange1(final double entityReach) {
        if (this.client.player != null) {
            return ReachEntityAttributes.getAttackRange(this.client.player, entityReach);
        }
        return entityReach;
    }
}
