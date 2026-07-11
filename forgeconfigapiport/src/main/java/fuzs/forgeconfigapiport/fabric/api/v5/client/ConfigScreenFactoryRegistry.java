/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package fuzs.forgeconfigapiport.fabric.api.v5.client;

import net.minecraft.client.gui.screens.Screen;
import org.sinytra.connectorextras.forgeconfigapiport.ConfigScreenFactoryRegistryImpl;

import java.util.function.BiFunction;

/**
 * A registry for enabling an in-game config screen for your mod via the <a
 * href="https://github.com/TerraformersMC/ModMenu">Mod Menu</a> mod (if present). The screen will be accessible from
 * the mod list in-game.
 * <p>
 * Generally this should be used to enable NeoForge's built-in config screen from
 * {@link net.neoforged.neoforge.client.gui.ConfigurationScreen}, which can optionally be extended.
 * <p>
 * Please note that the NeoForge screen is only compatible with {@link net.neoforged.neoforge.common.ModConfigSpec}, no
 * other {@link net.neoforged.fml.config.IConfigSpec} implementation is supported.
 */
public interface ConfigScreenFactoryRegistry {
    ConfigScreenFactoryRegistry INSTANCE = new ConfigScreenFactoryRegistryImpl();

    /**
     * Registers a config screen factory for your mod.
     *
     * @param modId   the id of your mod
     * @param factory the config screen factory, incoming parameters are your mod id and the last screen (the mod list
     *                screen)
     */
    void register(String modId, BiFunction<String, Screen, Screen> factory);
}