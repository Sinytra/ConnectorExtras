package dev.su5ed.sinytra.connectorextras.energybridge;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class EnergyBridgeConfig {
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    private EnergyBridgeConfig() {
    }

    public static final class Common {
        public final ForgeConfigSpec.IntValue fabricToForgeEnergy;
        public final ForgeConfigSpec.IntValue forgeToFabricEnergy;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.fabricToForgeEnergy = builder
                .comment("The amount of FE (Forge Energy) converted from 1 E (Team Reborn Energy). Setting the value to 0 disables this conversion.")
                .defineInRange("fabricToForgeEnergy", 10, 0, Integer.MAX_VALUE);
            this.forgeToFabricEnergy = builder
                .comment("The amount of FE (Forge Energy) required for 1 E (Team Reborn Energy). Setting the value to 0 disables this conversion.")
                .defineInRange("forgeToFabricEnergy", 10, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}
