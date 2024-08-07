package dev.su5ed.sinytra.connectorextras.misc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.pitan76.mcpitanlib.api.item.CreativeTabManager;

public final class MCPitanLibBridge {

    public static void init(IEventBus bus) {
        bus.addListener(MCPitanLibBridge::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        CreativeTabManager.allRegister();
    }

    private MCPitanLibBridge() {}
}
