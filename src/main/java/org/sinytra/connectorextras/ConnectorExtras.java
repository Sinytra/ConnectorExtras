package org.sinytra.connectorextras;

import org.sinytra.connectorextras.misc.MCPitanLibBridge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod("connectorextras")
public class ConnectorExtras {

    public ConnectorExtras(IEventBus bus) {
        if (ModList.get().isLoaded("mcpitanlibarch")) {
            MCPitanLibBridge.init(bus);
        }
    }
}
