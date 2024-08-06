package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnergyBridgeSetup {
    private static final ThreadLocal<Boolean> COMPUTING_CAPABILITY_LOCK = ThreadLocal.withInitial(() -> false);
    private static final Map<EnergyStorage, Object> CAPS = new HashMap<>();

    public static void init(IEventBus bus) {
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity != null && !COMPUTING_CAPABILITY_LOCK.get()) {
                COMPUTING_CAPABILITY_LOCK.set(true);
                EnergyStorage storage = Optional.ofNullable(world.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, blockEntity, context))
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
                EnergyStorage storage = Optional.ofNullable(stack.getCapability(Capabilities.EnergyStorage.ITEM, null))
                    .map(s -> new ForgeEnergyStorageHandler(s, context, stack))
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
                Capabilities.EnergyStorage.BLOCK,
                (level, pos, state, blockEntity, context) -> {
                    if (!COMPUTING_CAPABILITY_LOCK.get() && (blockEntity == null || blockEntity.hasLevel())) {
                        COMPUTING_CAPABILITY_LOCK.set(true);
                        EnergyStorage storage = EnergyStorage.SIDED.find(level, pos, state, blockEntity, context);
                        COMPUTING_CAPABILITY_LOCK.set(false);
                        if (storage != null) {
                            return (IEnergyStorage) CAPS.computeIfAbsent(storage, s -> new FabricEnergyStorageHandler(storage));
                        }
                    }
                    return null;
                },
                block
            );
        }
        for (Item item : BuiltInRegistries.ITEM) {
            event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, context) -> {
                    if (!COMPUTING_CAPABILITY_LOCK.get()) {
                        COMPUTING_CAPABILITY_LOCK.set(true);
                        FabricEnergySlotHandler handler = new FabricEnergySlotHandler(stack);
                        EnergyStorage storage = EnergyStorage.ITEM.find(stack, ContainerItemContext.ofSingleSlot(handler));
                        COMPUTING_CAPABILITY_LOCK.set(false);
                        if (storage != null) {
                            return (IEnergyStorage) CAPS.computeIfAbsent(storage, b -> new FabricEnergyStorageHandler(storage, stack, handler));
                        }
                    }
                    return null;
                },
                item
            );
        }
    }
}
