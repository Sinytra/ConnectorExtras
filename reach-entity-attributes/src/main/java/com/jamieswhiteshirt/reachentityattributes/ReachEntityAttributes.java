package com.jamieswhiteshirt.reachentityattributes;

import com.jamieswhiteshirt.reachentityattributes.mixin.EntityAttributeInstanceAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mod("reach_entity_attributes")
@ParametersAreNonnullByDefault
public final class ReachEntityAttributes {
    public static final String MOD_ID = "reach-entity-attributes";

    public static final EntityAttribute REACH = make("reach", 0.0, -1024.0, 1024.0);
    public static final EntityAttribute ATTACK_RANGE = make("attack_range", 0.0, -1024.0, 1024.0);

    public static final UUID REACH_MODIFIER_ID = UUID.fromString("609593b4-401f-48c3-b160-41d8581a4c2b");

    public static double getReachDistance(final LivingEntity entity, final double baseReachDistance) {
        @Nullable final var reachDistance = entity.getAttributeInstance(REACH);
        return (reachDistance != null) ? (baseReachDistance + reachDistance.getValue()) : baseReachDistance;
    }

    public static double getSquaredReachDistance(final LivingEntity entity, final double sqBaseReachDistance) {
        final var reachDistance = getReachDistance(entity, Math.sqrt(sqBaseReachDistance));
        return reachDistance * reachDistance;
    }

    public static double getAttackRange(final LivingEntity entity, final double baseAttackRange) {
        @Nullable final var attackRange = entity.getAttributeInstance(ATTACK_RANGE);
        return (attackRange != null) ? (baseAttackRange + attackRange.getValue()) : baseAttackRange;
    }

    public static double getSquaredAttackRange(final LivingEntity entity, final double sqBaseAttackRange) {
        final var attackRange = getAttackRange(entity, Math.sqrt(sqBaseAttackRange));
        return attackRange * attackRange;
    }

    public static List<PlayerEntity> getPlayersWithinReach(final World world, final int x, final int y, final int z, final double baseReachDistance) {
        return getPlayersWithinReach(player -> true, world, x, y, z, baseReachDistance);
    }

    public static List<PlayerEntity> getPlayersWithinReach(final Predicate<PlayerEntity> viewerPredicate, final World world, final int x, final int y, final int z, final double baseReachDistance) {
        final List<PlayerEntity> playersWithinReach = new ArrayList<>(0);
        for (final PlayerEntity player : world.getPlayers()) {
            if (viewerPredicate.test(player)) {
                final var reach = getReachDistance(player, baseReachDistance);
                final var dx = (x + 0.5) - player.getX();
                final var dy = (y + 0.5) - player.getEyeY();
                final var dz = (z + 0.5) - player.getZ();
                if (((dx * dx) + (dy * dy) + (dz * dz)) <= (reach * reach)) {
                    playersWithinReach.add(player);
                }
            }
        }
        return playersWithinReach;
    }

    public static boolean isWithinAttackRange(final PlayerEntity player, final Entity entity) {
        return player.squaredDistanceTo(entity) <= getSquaredAttackRange(player, 64.0);
    }

    private static EntityAttribute make(final String name, final double base, final double min, final double max) {
        return new ClampedEntityAttribute("attribute.name.generic." + MOD_ID + '.' + name, base, min, max).setTracked(true);
    }

    public static boolean checkWithinActualReach(final PlayerEntity player, final BlockPos pos, final int reachDistance) {
        return player.squaredDistanceTo(pos.toCenterPos()) <= ReachEntityAttributes.getSquaredReachDistance(player, reachDistance * reachDistance);
    }

    public static double getAttackRangeDistance(final double reachDistance, final PlayerEntity player) {
        return ReachEntityAttributes.getReachDistance(player, reachDistance);
    }

    private static void onRegisterEvent(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ATTRIBUTES, helper -> {
            helper.register(new Identifier(MOD_ID, "reach"), REACH);
            helper.register(new Identifier(MOD_ID, "attack_range"), ATTACK_RANGE);
        });
    }

    private static void onAttributeModificaiton(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, REACH);
            event.add(type, ATTACK_RANGE);
        }
    }

    private static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            EntityAttributeInstance reachInstance = living.getAttributeInstance(REACH);
            EntityAttributeInstance entityReachInstance = living.getAttributeInstance(ForgeMod.ENTITY_REACH.get());
            addReachModifier(living, reachInstance, entityReachInstance);
            EntityAttributeInstance blockReachInstance = living.getAttributeInstance(ForgeMod.BLOCK_REACH.get());
            addReachModifier(living, reachInstance, blockReachInstance);
        }
    }

    private static void addReachModifier(LivingEntity living, EntityAttributeInstance reachInstance, EntityAttributeInstance targetInstance) {
        if (targetInstance != null && targetInstance.getModifier(REACH_MODIFIER_ID) == null) {
            EntityAttributeModifier modifier = new DynamicEntityAttributeModifier(REACH_MODIFIER_ID, "Reach", () -> living.getAttributeValue(REACH), EntityAttributeModifier.Operation.ADDITION);
            targetInstance.addTemporaryModifier(modifier);
            if (reachInstance != null) {
                Consumer<EntityAttributeInstance> updateCallback = ((EntityAttributeInstanceAccessor) reachInstance).getUpdateCallback();
                Consumer<EntityAttributeInstance> merged = updateCallback.andThen(i -> ((EntityAttributeInstanceAccessor) targetInstance).callOnUpdate());
                ((EntityAttributeInstanceAccessor) reachInstance).setUpdateCallback(merged);
            }
        }
    }

    public ReachEntityAttributes() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ReachEntityAttributes::onRegisterEvent);
        bus.addListener(ReachEntityAttributes::onAttributeModificaiton);
        MinecraftForge.EVENT_BUS.addListener(ReachEntityAttributes::onEntityJoinLevel);
    }
}
