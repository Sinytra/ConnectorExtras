package dev.su5ed.sinytra.connectorextras.energybridge;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class EnergyBridgeConfig {
    public static final ModConfigSpec COMMON_SPEC;

    public static final Common COMMON;

    static {
        Pair<Common, ModConfigSpec> commonPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();
    }

    private EnergyBridgeConfig() {
    }

    public static final class Common {
        public final ModConfigSpec.IntValue fabricToForgeEnergy;
        public final ModConfigSpec.IntValue forgeToFabricEnergy;

        private Common(ModConfigSpec.Builder builder) {
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
