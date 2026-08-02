package org.sinytra.connectorextras.jeibridge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("connectorextras_jei_bridge")
public class JEIBridge {
    public static final String JEI_MODID = "jei";

    public JEIBridge(IEventBus bus) {
        if (ModList.get().isLoaded(JEI_MODID)) {
            bus.addListener(JEIBridgeSetup::onClientSetup);
        }
    }
}
