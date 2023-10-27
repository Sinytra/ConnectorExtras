package dev.su5ed.sinytra.connectorextras.emibridge;

import com.mojang.logging.LogUtils;
import dev.su5ed.sinytra.connectorextras.HackyModuleInjector;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod("connectorextras_emi_bridge")
public class EMIBridge {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String EMI_MODID = "emi";

    public static void injectModule() {
        LOGGER.info("Injecting EMI API classes");
        HackyModuleInjector.injectModuleSources(EMI_MODID, EMIBridge.class.getResource("/relocate"));
    }
}
