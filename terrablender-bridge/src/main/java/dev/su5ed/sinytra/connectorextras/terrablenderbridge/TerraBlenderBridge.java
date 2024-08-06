package dev.su5ed.sinytra.connectorextras.terrablenderbridge;

import com.mojang.logging.LogUtils;
import dev.su5ed.sinytra.connectorextras.util.HackyModuleInjector;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod("connectorextras_terrablender_bridge")
public class TerraBlenderBridge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TerraBlenderBridge(IEventBus bus) {
        LOGGER.info("Injecting TerraBlender API classes");

        if (HackyModuleInjector.injectModuleSources("terrablender", TerraBlenderBridge.class.getResource("/relocate"))) {
            bus.addListener(TerraBlenderBridge::onCommonSetup);
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(TerraBlenderBridgeSetup::init);
    }
}
