package dev.su5ed.sinytra.connectorextras.misc;

import ml.pkom.mcpitanlibarch.api.item.CreativeTabManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class MCPitanLibBridge {

    public static void init(IEventBus bus) {
        bus.addListener(MCPitanLibBridge::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        CreativeTabManager.allRegister();
    }

    private MCPitanLibBridge() {}
}
