package dev.su5ed.sinytra.connectorextras.jeibridge;

import com.mojang.logging.LogUtils;
import dev.su5ed.sinytra.connectorextras.util.HackyModuleInjector;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod("connectorextras_jei_bridge")
public class JEIBridge {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String JEI_MODID = "jei";

    public static void injectModule() {
        LOGGER.info("Injecting JEI API classes");
        HackyModuleInjector.injectModuleSources(JEI_MODID, JEIBridge.class.getResource("/relocate"));
    }
}
