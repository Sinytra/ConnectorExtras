package dev.su5ed.sinytra.connectorextras.forgeconfigapiport;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.unsafe.UnsafeHacks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ForgeConfigRegistryImpl implements ForgeConfigRegistry {
    private final Map<String, ModConfig> configs = new HashMap<>();

    @Override
    public ModConfig register(String modId, ModConfig.Type type, IConfigSpec<?> spec) {
        return createModConfig(modId, c -> new ModConfig(type, spec, c));
    }

    @Override
    public ModConfig register(String modId, ModConfig.Type type, IConfigSpec<?> spec, String fileName) {
        return createModConfig(modId, c -> new ModConfig(type, spec, c, fileName));
    }

    public void processConfigs() {
        Field containerField = LamdbaExceptionUtils.uncheck(() -> {
            Field field = ModConfig.class.getDeclaredField("container");
            field.setAccessible(true);
            return field;
        });
        configs.forEach((modid, config) -> {
            ModContainer container = getModContainer(modid);
            container.addConfig(config);
            try {
                UnsafeHacks.setField(containerField, config, container);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        });
        configs.clear();
    }

    private ModConfig createModConfig(String modid, Function<ModContainer, ModConfig> configConstructor) {
        ModList modList = ModList.get();
        if (modList == null) {
            ModContainer container = createPlaceholderContainer(modid);
            ModConfig config = configConstructor.apply(container);
            configs.put(modid, config);
            return config;
        } else {
            ModContainer container = getModContainer(modid);
            ModConfig config = configConstructor.apply(container);
            container.addConfig(config);
            return config;
        }
    }

    private static ModContainer createPlaceholderContainer(String modId) {
        try {
            Class<?> cls = Class.forName("net.minecraftforge.fml.ModLoader$ErroredModContainer");
            Constructor<?> ctr = cls.getDeclaredConstructor();
            ctr.setAccessible(true);
            ModContainer container = (ModContainer) ctr.newInstance();
            Field modidField = ModContainer.class.getDeclaredField("modId");
            modidField.setAccessible(true);
            UnsafeHacks.setField(modidField, container, modId);

            return container;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static ModContainer getModContainer(String modid) {
        return ModList.get().getModContainerById(modid).orElseThrow(() -> new RuntimeException("Mod container not found for mod " + modid));
    }
}
