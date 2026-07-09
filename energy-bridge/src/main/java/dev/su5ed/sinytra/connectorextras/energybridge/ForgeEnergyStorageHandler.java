package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.impl.transfer.compat.TransferCompatUtil;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import team.reborn.energy.api.EnergyStorage;

public class ForgeEnergyStorageHandler implements EnergyStorage {
    private final EnergyHandler storage;

    public ForgeEnergyStorageHandler(EnergyHandler storage) {
        this.storage = storage;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        int fe = EnergyBridge.convertFabricToForgeEnergy(maxAmount);
        int inserted = this.storage.insert(fe, TransferCompatUtil.toNeoCtx(transaction));
        return EnergyBridge.unConvertFabricToForgeEnergy(inserted);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        int fe = EnergyBridge.unConvertForgeToFabricEnergy(maxAmount);
        int extracted = this.storage.extract(fe, TransferCompatUtil.toNeoCtx(transaction));
        return EnergyBridge.convertForgeToFabricEnergy(extracted);
    }

    @Override
    public long getAmount() {
        return EnergyBridge.convertForgeToFabricEnergy(this.storage.getAmountAsLong());
    }

    @Override
    public long getCapacity() {
        return EnergyBridge.convertForgeToFabricEnergy(this.storage.getCapacityAsLong());
    }
}
