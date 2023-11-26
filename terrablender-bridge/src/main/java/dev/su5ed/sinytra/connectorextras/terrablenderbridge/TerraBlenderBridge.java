package dev.su5ed.sinytra.connectorextras.terrablenderbridge;

import com.mojang.logging.LogUtils;
import dev.su5ed.sinytra.connectorextras.util.HackyModuleInjector;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("connectorextras_terrablender_bridge")
public class TerraBlenderBridge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TerraBlenderBridge() {
        LOGGER.info("Injecting TerraBlender API classes");
        if (HackyModuleInjector.injectModuleSources("terrablender", TerraBlenderBridge.class.getResource("/relocate"))) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(TerraBlenderBridge::onCommonSetup);
        }
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(TerraBlenderBridgeSetup::init);
    }
}
