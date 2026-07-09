package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.impl.transfer.compat.FabricItemAccess;
import net.fabricmc.fabric.impl.transfer.compat.NeoContainerItemContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import team.reborn.energy.api.EnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class EnergyBridgeSetup {
    private static final ThreadLocal<Boolean> COMPUTING_CAPABILITY_LOCK = ThreadLocal.withInitial(() -> false);
    private static final Map<EnergyStorage, Object> CAPS = new HashMap<>();

    public static void init(IEventBus bus) {
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity != null && !COMPUTING_CAPABILITY_LOCK.get()) {
                COMPUTING_CAPABILITY_LOCK.set(true);
                EnergyStorage storage = Optional.ofNullable(world.getCapability(Capabilities.Energy.BLOCK, pos, state, blockEntity, context))
                    .map(ForgeEnergyStorageHandler::new)
                    .orElse(null);
                COMPUTING_CAPABILITY_LOCK.set(false);
                return storage;
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((stack, context) -> {
            if (!stack.isEmpty() && !COMPUTING_CAPABILITY_LOCK.get()) {
                COMPUTING_CAPABILITY_LOCK.set(true);
                EnergyStorage storage = Optional.ofNullable(stack.getCapability(Capabilities.Energy.ITEM, new FabricItemAccess(context)))
                    .map(ForgeEnergyStorageHandler::new)
                    .orElse(null);
                COMPUTING_CAPABILITY_LOCK.set(false);
                return storage;
            }
            return null;
        });

        bus.addListener(EnergyBridgeSetup::onRegisterCapabilities);
    }

    private static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (Block block : BuiltInRegistries.BLOCK) {
            event.registerBlock(
                Capabilities.Energy.BLOCK,
                (level, pos, state, blockEntity, context) -> {
                    if (!COMPUTING_CAPABILITY_LOCK.get() && (blockEntity == null || blockEntity.hasLevel())) {
                        COMPUTING_CAPABILITY_LOCK.set(true);
                        EnergyStorage storage = EnergyStorage.SIDED.find(level, pos, state, blockEntity, context);
                        COMPUTING_CAPABILITY_LOCK.set(false);
                        if (storage != null) {
                            return (EnergyHandler) CAPS.computeIfAbsent(storage, FabricEnergyStorageHandler::new);
                        }
                    }
                    return null;
                },
                block
            );
        }

        for (Item item : BuiltInRegistries.ITEM) {
            event.registerItem(
                Capabilities.Energy.ITEM,
                (stack, context) -> {
                    if (!COMPUTING_CAPABILITY_LOCK.get()) {
                        COMPUTING_CAPABILITY_LOCK.set(true);
                        EnergyStorage storage = EnergyStorage.ITEM.find(stack, new NeoContainerItemContext(context));
                        COMPUTING_CAPABILITY_LOCK.set(false);
                        if (storage != null) {
                            return (EnergyHandler) CAPS.computeIfAbsent(storage, FabricEnergyStorageHandler::new);
                        }
                    }
                    return null;
                },
                item
            );
        }
    }
}
