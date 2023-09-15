package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraftforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorageHandler implements IEnergyStorage {
    private final EnergyStorage storage;

    public FabricEnergyStorageHandler(EnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            int inserted = (int) this.storage.insert(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            int extracted = (int) this.storage.extract(amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return (int) this.storage.getAmount();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) this.storage.getCapacity();
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
