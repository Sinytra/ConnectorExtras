package dev.su5ed.sinytra.connectorextras.geckolibcompat;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.client.RenderProvider;

public class ForgeRenderProvider implements RenderProvider {
    private final IClientItemExtensions itemRenderer;

    public ForgeRenderProvider(IClientItemExtensions extensions) {
        this.itemRenderer = extensions;
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return this.itemRenderer.getCustomRenderer();
    }

    @Override
    public Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
        return this.itemRenderer.getGenericArmorModel(livingEntity, itemStack, equipmentSlot, original);
    }

    @Override
    public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
        return (HumanoidModel<LivingEntity>) this.itemRenderer.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
    }
}