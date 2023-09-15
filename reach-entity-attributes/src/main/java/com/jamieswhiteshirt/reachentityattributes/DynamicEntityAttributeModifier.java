package com.jamieswhiteshirt.reachentityattributes;

import net.minecraft.entity.attribute.EntityAttributeModifier;

import java.util.UUID;
import java.util.function.DoubleSupplier;

public class DynamicEntityAttributeModifier extends EntityAttributeModifier {
    private final DoubleSupplier valueSupplier;

    public DynamicEntityAttributeModifier(UUID uuid, String name, DoubleSupplier value, Operation operation) {
        super(uuid, name, 0.0, operation);
        this.valueSupplier = value;
    }

    @Override
    public double getValue() {
        return this.valueSupplier.getAsDouble();
    }
}
