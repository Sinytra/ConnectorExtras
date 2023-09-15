package dev.su5ed.sinytra.connectorextras.energybridge;

import com.google.common.primitives.Ints;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(EnergyBridge.MODID)
public class EnergyBridge {
    public static final String MODID = "connectorextras_energy_bridge";

    public EnergyBridge() {
        if (ModList.get().isLoaded("team_reborn_energy")) {
            EnergyBridgeSetup.init();
        }
        ModLoadingContext ctx = ModLoadingContext.get();
        ctx.registerConfig(ModConfig.Type.COMMON, EnergyBridgeConfig.COMMON_SPEC);
    }

    public static int convertFabricToForgeEnergy(long amount) {
        return convertForward(amount, EnergyBridgeConfig.COMMON.fabricToForgeEnergy);
    }

    public static long unConvertFabricToForgeEnergy(int amount) {
        return convertBackwards(amount, EnergyBridgeConfig.COMMON.fabricToForgeEnergy);
    }

    public static long convertForgeToFabricEnergy(int amount) {
        return convertBackwards(amount, EnergyBridgeConfig.COMMON.forgeToFabricEnergy);
    }

    public static int unConvertForgeToFabricEnergy(long amount) {
        return convertForward(amount, EnergyBridgeConfig.COMMON.forgeToFabricEnergy);
    }

    public static int convertForward(long amount, ForgeConfigSpec.IntValue ratio) {
        if (amount > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (amount < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        long fe = ratio.get() * amount;
        return Ints.saturatedCast(fe);
    }

    private static long convertBackwards(int amount, ForgeConfigSpec.IntValue ratio) {
        int value = ratio.get();
        return value == 0 ? 0 : amount / value;
    }
}
