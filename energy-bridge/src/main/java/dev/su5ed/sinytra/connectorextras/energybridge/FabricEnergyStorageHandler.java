package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorageHandler implements IEnergyStorage {
    private final EnergyStorage storage;
    private final ItemStack stack;
    private final FabricEnergySlotHandler handler;

    public FabricEnergyStorageHandler(EnergyStorage storage) {
        this.storage = storage;
        this.stack = null;
        this.handler = null;
    }

    public FabricEnergyStorageHandler(EnergyStorage storage, ItemStack stack, FabricEnergySlotHandler handler) {
        this.storage = storage;
        this.stack = stack;
        this.handler = handler;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (Transaction.isOpen()) {
            return 0;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long e = EnergyBridge.convertForgeToFabricEnergy(amount);
            long originalAmount = this.storage.getAmount();
            long inserted = (int) this.storage.insert(e, transaction);
            if (!simulate) {
                transaction.commit();
                if (stack != null) {
                    stack.setTag(handler.getStack().getTag());
                }
            } else if (stack != null && originalAmount == (this.storage.getAmount() - inserted)) {
                //Some implementations just commit the transaction by themselves, so it must be reverted afterwards
                //We can't just extract the energy because the storage could be insert-only
                handler.setStack(stack);
            }
            return EnergyBridge.unConvertForgeToFabricEnergy(inserted);
        }
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        if (Transaction.isOpen()) {
            return 0;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long e = EnergyBridge.unConvertFabricToForgeEnergy(amount);
            long extracted = (int) this.storage.extract(e, transaction);
            if (!simulate) {
                transaction.commit();
                if (stack != null) {
                    stack.setTag(handler.getStack().getTag());
                }
            }
            return EnergyBridge.convertFabricToForgeEnergy(extracted);
        }
    }

    @Override
    public int getEnergyStored() {
        return EnergyBridge.convertFabricToForgeEnergy(this.storage.getAmount());
    }

    @Override
    public int getMaxEnergyStored() {
        return EnergyBridge.convertFabricToForgeEnergy(this.storage.getCapacity());
    }

    @Override
    public boolean canExtract() {
        return this.storage.supportsExtraction();
    }

    @Override
    public boolean canReceive() {
        return this.storage.supportsInsertion();
    }
}
