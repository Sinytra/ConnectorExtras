package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

public class FabricEnergySlotHandler implements SingleSlotStorage<ItemVariant> {

    private ItemStack stack;

    public FabricEnergySlotHandler(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        this.stack = resource.toStack();
        return maxAmount;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        this.stack = resource.toStack();
        return maxAmount;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(stack);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public long getAmount() {
        return stack.getCount();
    }

    @Override
    public long getCapacity() {
        return stack.getMaxStackSize();
    }

}
