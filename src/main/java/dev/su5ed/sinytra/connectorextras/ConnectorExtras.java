package dev.su5ed.sinytra.connectorextras;

import dev.su5ed.sinytra.connectorextras.misc.MCPitanLibBridge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("connectorextras")
public class ConnectorExtras {

    public ConnectorExtras() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        if (ModList.get().isLoaded("mcpitanlibarch")) {
            MCPitanLibBridge.init(bus);
        }
    }
}
