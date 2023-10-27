package dev.su5ed.sinytra.connectorextras.energybridge;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.HashMap;
import java.util.Map;

public class EnergyBridgeSetup {
    private static final ThreadLocal<Boolean> COMPUTING_CAPABILITY_LOCK = ThreadLocal.withInitial(() -> false);
    private static final Map<EnergyStorage, LazyOptional<?>> CAPS = new HashMap<>();

    public static void init() {
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity != null && !COMPUTING_CAPABILITY_LOCK.get()) {
                COMPUTING_CAPABILITY_LOCK.set(true);
                EnergyStorage storage = blockEntity.getCapability(ForgeCapabilities.ENERGY, context)
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
                EnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY, null)
                    .map(ForgeEnergyStorageHandler::new)
                    .orElse(null);
                COMPUTING_CAPABILITY_LOCK.set(false);
                return storage;
            }
            return null;
        });

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, EnergyBridgeSetup::onAttachBlockEntityCapabilities);
        // TODO Fix container item context
//        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, EnergyBridgeSetup::onAttachItemStackCapabilities);
    }

    private static void onAttachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity be = event.getObject();
        event.addCapability(new ResourceLocation(EnergyBridge.MODID, "forge_bridge"), new ICapabilityProvider() {
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY && be.hasLevel() && !COMPUTING_CAPABILITY_LOCK.get()) {
                    COMPUTING_CAPABILITY_LOCK.set(true);
                    EnergyStorage storage = EnergyStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, side);
                    COMPUTING_CAPABILITY_LOCK.set(false);
                    if (storage != null) {
                        return CAPS.computeIfAbsent(storage, s -> LazyOptional.of(() -> new FabricEnergyStorageHandler(storage))).cast();
                    }
                }
                return LazyOptional.empty();
            }
        });
    }

    private static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        event.addCapability(new ResourceLocation(EnergyBridge.MODID, "forge_bridge"), new ICapabilityProvider() {
            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY && !COMPUTING_CAPABILITY_LOCK.get()) {
                    COMPUTING_CAPABILITY_LOCK.set(true);
                    EnergyStorage storage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withConstant(stack));
                    COMPUTING_CAPABILITY_LOCK.set(false);
                    if (storage != null) {
                        return CAPS.computeIfAbsent(storage, b -> LazyOptional.of(() -> new FabricEnergyStorageHandler(storage))).cast();
                    }
                }
                return LazyOptional.empty();
            }
        });
    }
}
