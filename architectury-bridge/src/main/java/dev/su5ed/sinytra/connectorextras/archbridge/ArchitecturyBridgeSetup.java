package dev.su5ed.sinytra.connectorextras.archbridge;

import dev.architectury.platform.forge.EventBuses;
import dev.su5ed.sinytra.connector.loader.ConnectorEarlyLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.util.HashMap;
import java.util.Map;

public final class ArchitecturyBridgeSetup {
    private static final Map<String, LazyEventBus> BUSES = new HashMap<>();

    public static void setup() {
        for (ModInfo mod : LoadingModList.get().getMods()) {
            String modid = mod.getModId();
            if (ConnectorEarlyLoader.isConnectorMod(modid)) {
                LazyEventBus bus = new LazyEventBus();
                BUSES.put(modid, bus);
                EventBuses.registerModEventBus(modid, bus);
            }
        }
    }

    public static void init() {
        ModList.get().forEachModContainer((modid, container) -> {
            if (container instanceof FMLModContainer fmlModContainer) {
                apply(modid, fmlModContainer.getEventBus());
            }
        });
    }

    private static void apply(String modid, IEventBus bus) {
        LazyEventBus lateEventBus = BUSES.get(modid);
        if (lateEventBus != null) {
            lateEventBus.setBus(bus);
            BUSES.remove(modid);
        }
    }

    private ArchitecturyBridgeSetup() {}
}
