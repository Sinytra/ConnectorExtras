package dev.su5ed.sinytra.connectorextras.continuitycompat;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ContinuityCompat.MODID)
public class ContinuityCompat {
    public static final String MODID = "connectorextras_continuity_compat";
    public static final String CONTINUITY_MODID = "continuity";

    public ContinuityCompat() {
        if (ModList.get().isLoaded(CONTINUITY_MODID)) {
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(ContinuityCompatSetup::onClientSetup);
        }
    }
}
