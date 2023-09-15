package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraftforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class ForgeEnergyStorageHandler implements EnergyStorage {
    private final IEnergyStorage storage;

    public ForgeEnergyStorageHandler(IEnergyStorage storage) {
        this.storage = storage;
    }

    // TODO Conversion ratio
    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        int inserted = this.storage.receiveEnergy((int) maxAmount, true);
        transaction.addCloseCallback((context, result) -> {
            if (result.wasCommitted()) {
                this.storage.receiveEnergy((int) maxAmount, false);
            }
        });
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        int extracted = this.storage.extractEnergy((int) maxAmount, true);
        transaction.addCloseCallback((context, result) -> {
            if (result.wasCommitted()) {
                this.storage.extractEnergy((int) maxAmount, false);
            }
        });
        return extracted;
    }

    @Override
    public long getAmount() {
        return this.storage.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return this.storage.getMaxEnergyStored();
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
