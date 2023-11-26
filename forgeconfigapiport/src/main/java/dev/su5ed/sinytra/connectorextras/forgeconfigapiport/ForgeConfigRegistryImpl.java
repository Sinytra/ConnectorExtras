package dev.su5ed.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ForgeConfigRegistryImpl implements ForgeConfigRegistry {
    @Override
    public ModConfig register(String modId, ModConfig.Type type, IConfigSpec<?> spec) {
        ModContainer container = getModContainer(modId);
        ModConfig modConfig = new ModConfig(type, spec, container);
        container.addConfig(modConfig);
        return modConfig;
    }

    @Override
    public ModConfig register(String modId, ModConfig.Type type, IConfigSpec<?> spec, String fileName) {
        ModContainer container = getModContainer(modId);
        ModConfig modConfig = new ModConfig(type, spec, container, fileName);
        container.addConfig(modConfig);
        return modConfig;
    }

    private static ModContainer getModContainer(String modid) {
        return ModList.get().getModContainerById(modid).orElseThrow(() -> new RuntimeException("Mod container not found for mod " + modid));
    }
}
