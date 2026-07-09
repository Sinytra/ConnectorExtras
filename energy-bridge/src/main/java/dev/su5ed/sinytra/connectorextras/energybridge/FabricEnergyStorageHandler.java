package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.impl.transfer.compat.TransferCompatUtil;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorageHandler implements EnergyHandler {
    private final EnergyStorage storage;

    public FabricEnergyStorageHandler(EnergyStorage storage) {
        this.storage = storage;
    }

    @Override
    public long getAmountAsLong() {
        return EnergyBridge.convertFabricToForgeEnergy(this.storage.getAmount());
    }

    @Override
    public long getCapacityAsLong() {
        return EnergyBridge.convertFabricToForgeEnergy(this.storage.getCapacity());
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        if (!this.storage.supportsInsertion()) {
            return 0;
        }
        long e = EnergyBridge.unConvertFabricToForgeEnergy(amount);
        long inserted = this.storage.extract(e, TransferCompatUtil.toFabricCtx(transaction));
        return EnergyBridge.convertFabricToForgeEnergy(inserted);
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        if (!this.storage.supportsExtraction()) {
            return 0;
        }
        long e = EnergyBridge.unConvertFabricToForgeEnergy(amount);
        long extracted = this.storage.extract(e, TransferCompatUtil.toFabricCtx(transaction));
        return EnergyBridge.convertFabricToForgeEnergy(extracted);
    }
}
