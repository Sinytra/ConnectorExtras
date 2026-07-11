package org.sinytra.connectorextras.forgeconfigapiport;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.minecraft.client.gui.screens.Screen;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class ConfigScreenFactoryRegistryImpl implements ConfigScreenFactoryRegistry {
    private final Map<String, UnaryOperator<Screen>> factories = new HashMap<>();

    @Override
    public void register(String modId, BiFunction<String, Screen, Screen> factory) {
        this.factories.put(modId, (Screen screen) -> factory.apply(modId, screen));
    }

    public Map<String, UnaryOperator<Screen>> getFactories() {
        return factories;
    }
}
