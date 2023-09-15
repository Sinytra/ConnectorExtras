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
        if (Transaction.isOpen()) {
            return 0;
        }
        try (Transaction transaction = Transaction.openOuter()) {
            long e = EnergyBridge.convertForgeToFabricEnergy(amount);
            long inserted = (int) this.storage.insert(e, transaction);
            if (!simulate) {
                transaction.commit();
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
