package org.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.neoforge.api.v5.ForgeConfigRegistry;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;

public class ConfigRegistryImpl implements ConfigRegistry {
    @Override
    public void register(String modId, Type type, IConfigSpec spec) {
        getModContainer(modId).registerConfig(type, spec);
    }

    @Override
    public void register(String modId, Type type, IConfigSpec spec, String fileName) {
        getModContainer(modId).registerConfig(type, spec, fileName);
    }

    @Override
    public void register(String modId, Type type, net.minecraftforge.fml.config.IConfigSpec<?> spec) {
        ForgeConfigRegistry.INSTANCE.register(modId, type, spec);
    }

    @Override
    public void register(String modId, Type type, net.minecraftforge.fml.config.IConfigSpec<?> spec, String fileName) {
        ForgeConfigRegistry.INSTANCE.register(modId, type, spec, fileName);
    }

    private ModContainer getModContainer(String modId) {
        return ModList.get()
            .getModContainerById(modId)
            .orElseThrow(() -> new IllegalStateException("Invalid mod id '%s'".formatted(modId)));
    }
}
