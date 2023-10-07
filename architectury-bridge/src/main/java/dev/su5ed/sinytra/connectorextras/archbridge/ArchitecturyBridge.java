package dev.su5ed.sinytra.connectorextras.archbridge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.LoadingModList;

@Mod(ArchitecturyBridge.MODID)
public class ArchitecturyBridge {
    public static final String MODID = "connectorextras_architectury_bridge";
    private static final String ARCH_MODID = "architectury";

    public ArchitecturyBridge() {
        if (isEnabled()) {
            ArchitecturyBridgeSetup.init();
        }
    }

    public static boolean isEnabled() {
        return LoadingModList.get().getModFileById(ARCH_MODID) != null;
    }
}
