package dev.su5ed.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ForgeConfigApiPortImpl.MODID)
public class ForgeConfigApiPortImpl {
    public static final String MODID = "forgeconfigapiport";

    // TODO Normalize modids
    public ForgeConfigApiPortImpl() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ForgeConfigApiPortImpl::onConfigLoad);
        bus.addListener(ForgeConfigApiPortImpl::onConfigReload);
        bus.addListener(ForgeConfigApiPortImpl::onConfigUnload);

        ((ForgeConfigRegistryImpl) ForgeConfigRegistry.INSTANCE).processConfigs();
    }

    private static void onConfigLoad(ModConfigEvent.Loading event) {
        Event<ModConfigEvents.Loading> fabricEvent = ModConfigEvents.loading(event.getConfig().getModId());
        fabricEvent.invoker().onModConfigLoading(event.getConfig());
    }

    private static void onConfigReload(ModConfigEvent.Reloading event) {
        Event<ModConfigEvents.Reloading> fabricEvent = ModConfigEvents.reloading(event.getConfig().getModId());
        fabricEvent.invoker().onModConfigReloading(event.getConfig());
    }

    private static void onConfigUnload(ModConfigEvent.Unloading event) {
        Event<ModConfigEvents.Unloading> fabricEvent = ModConfigEvents.unloading(event.getConfig().getModId());
        fabricEvent.invoker().onModConfigUnloading(event.getConfig());
    }
}
