package org.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.fabric.api.event.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(ForgeConfigApiBrigeImpl.MODID)
public class ForgeConfigApiBrigeImpl {
    public static final String MODID = "connectorextras_forgeconfigapiport";
    public static final String UPSTREAM_MODID = "forgeconfigapiport";

    public ForgeConfigApiBrigeImpl(IEventBus bus) {
        if (!ModList.get().isLoaded(UPSTREAM_MODID)) {
            return;
        }

        bus.addListener(ForgeConfigApiBrigeImpl::onClientSetup);

        bus.addListener(ForgeConfigApiBrigeImpl::onConfigLoad);
        bus.addListener(ForgeConfigApiBrigeImpl::onConfigReload);
        bus.addListener(ForgeConfigApiBrigeImpl::onConfigUnload);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        ((ConfigScreenFactoryRegistryImpl) ConfigScreenFactoryRegistry.INSTANCE).getFactories()
            .forEach((modId, factory) -> {
                ModList.get().getModContainerById(modId)
                    .ifPresent(c ->
                        c.registerExtensionPoint(IConfigScreenFactory.class, (_, screen) -> factory.apply(screen)));
            });
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
