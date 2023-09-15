package com.jamieswhiteshirt.reachentityattributes.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(EntityAttributeInstance.class)
public interface EntityAttributeInstanceAccessor {
    @Accessor
    Consumer<EntityAttributeInstance> getUpdateCallback();

    @Accessor
    @Mutable
    void setUpdateCallback(Consumer<EntityAttributeInstance> updateCallback);

    @Invoker
    void callOnUpdate();
}
