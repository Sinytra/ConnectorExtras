package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraftforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public class ForgeEnergyStorageHandler implements EnergyStorage {
    private final IEnergyStorage storage;

    public ForgeEnergyStorageHandler(IEnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        int fe = EnergyBridge.convertFabricToForgeEnergy(maxAmount);
        int inserted = this.storage.receiveEnergy(fe, true);
        transaction.addCloseCallback((context, result) -> {
            if (result.wasCommitted()) {
                this.storage.receiveEnergy(fe, false);
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
            }
        });
        return EnergyBridge.convertForgeToFabricEnergy(extracted);
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
