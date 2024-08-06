package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class ForgeEnergyStorageHandler implements EnergyStorage {
    private final IEnergyStorage storage;
    private final ContainerItemContext context;
    private final ItemStack stack;

    public ForgeEnergyStorageHandler(IEnergyStorage storage) {
        this.storage = storage;
        this.context = null;
        this.stack = null;
    }

    public ForgeEnergyStorageHandler(IEnergyStorage storage, ContainerItemContext context, ItemStack stack) {
        this.storage = storage;
        this.context = context;
        this.stack = stack;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        int fe = EnergyBridge.convertFabricToForgeEnergy(maxAmount);
        int inserted = this.storage.receiveEnergy(fe, true);
        transaction.addCloseCallback((context, result) -> {
            if (result.wasCommitted()) {
                this.storage.receiveEnergy(fe, false);
                updateStack();
            }
        });
        return EnergyBridge.unConvertFabricToForgeEnergy(inserted);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        int fe = EnergyBridge.unConvertForgeToFabricEnergy(maxAmount);
        int extracted = this.storage.extractEnergy(fe, true);
        transaction.addCloseCallback((context, result) -> {
            if (result.wasCommitted()) {
                this.storage.extractEnergy(fe, false);
                updateStack();
            }
        });
        return EnergyBridge.convertForgeToFabricEnergy(extracted);
    }

    private void updateStack() {
        if (stack != null) {
            //We can't open a new Transaction while one is closing, so we make a new Thread
            Thread thread = new Thread(() -> {
                try (Transaction t = Transaction.openOuter()) {
                    //Update the item (NBT).
                    this.context.extract(this.context.getItemVariant(), 1, t);
                    this.context.insert(ItemVariant.of(stack), 1, t);
                    t.commit();
                }
            });
            thread.start();
        }
    }

    @Override
    public long getAmount() {
        return EnergyBridge.convertForgeToFabricEnergy(this.storage.getEnergyStored());
    }

    @Override
    public long getCapacity() {
        return EnergyBridge.convertForgeToFabricEnergy(this.storage.getMaxEnergyStored());
    }

    @Override
    public boolean supportsInsertion() {
        return this.storage.canReceive();
    }

    @Override
    public boolean supportsExtraction() {
        return this.storage.canExtract();
    }
}
