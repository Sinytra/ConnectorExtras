package dev.su5ed.sinytra.connectorextras.energybridge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(EnergyBridge.MODID)
public class EnergyBridge {
    public static final String MODID = "energybridge";

    public EnergyBridge() {
        if (ModList.get().isLoaded("team_reborn_energy")) {
            EnergyBridgeSetup.init();
        }
    }
}
