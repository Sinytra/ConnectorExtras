/*
 * Copyright 2020-2023 Siphalor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.siphalor.amecs.impl;

import de.siphalor.amecs.api.PriorityKeyBinding;
import de.siphalor.amecs.impl.mixin.KeyMappingLookupAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraftforge.client.settings.KeyMappingLookup;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@ApiStatus.Internal
public class KeyBindingManager {
    private static final Map<InputUtil.Key, List<KeyBinding>> PRIORITY_KEY_BINDINGS = new HashMap<>();

    private KeyBindingManager() {
    }

    /**
     * Registers a key binding to Amecs API
     *
     * @param keyBinding the key binding to register
     * @return whether the keyBinding was added. It is not added if it is already contained
     */
    public static boolean register(KeyBinding keyBinding) {
        if (keyBinding instanceof PriorityKeyBinding) {
            PRIORITY_KEY_BINDINGS.computeIfAbsent(keyBinding.getKey(), k -> new ArrayList<>()).add(keyBinding);
            return true;
        } else {
            KeyMappingLookup lookup = getKeyMappingLookup();
            EnumMap<KeyModifier, Map<InputUtil.Key, Collection<KeyBinding>>> map = ((KeyMappingLookupAccessor) lookup).getMap();
            if (!map.get(keyBinding.getKeyModifier()).containsKey(keyBinding.getKey())) {
                lookup.put(keyBinding.getKey(), keyBinding);
                return true;
            }
            return false;
        }
    }

    /**
     * Unregisters a key binding from Amecs API
     *
     * @param id the key binding to unregister
     * @return whether the keyBinding was removed. It is not removed if it was not contained
     */
    public static boolean unregister(String id) {
        KeyMappingLookup lookup = getKeyMappingLookup();
        EnumMap<KeyModifier, Map<InputUtil.Key, Collection<KeyBinding>>> map = ((KeyMappingLookupAccessor) lookup).getMap();
        for (Map<InputUtil.Key, Collection<KeyBinding>> keyMap : map.values()) {
            for (Collection<KeyBinding> keys : keyMap.values()) {
                for (KeyBinding key : keys) {
                    if (key.getTranslationKey().equals(id)) {
                        keys.remove(key);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Stream<KeyBinding> getMatchingKeyBindings(InputUtil.Key keyCode) {
        List<KeyBinding> keyBindingList = PRIORITY_KEY_BINDINGS.get(keyCode);
        if (keyBindingList == null)
            return Stream.empty();
        Stream<KeyBinding> result = keyBindingList.stream().filter(KeyBindingManager::areExactModifiersPressed);
        List<KeyBinding> keyBindings = result.collect(Collectors.toList());
        if (keyBindings.isEmpty())
            return keyBindingList.stream().filter(keyBinding -> keyBinding.getKeyModifier() == KeyModifier.NONE);
        return keyBindings.stream();
    }

    private static boolean areExactModifiersPressed(KeyBinding keyBinding) {
        return keyBinding.getKeyModifier() == KeyModifier.getActiveModifier();
    }

    public static boolean onKeyPressedPriority(InputUtil.Key keyCode) {
        // because streams are lazily evaluated, this code only calls onPressedPriority so often until one returns true
        Optional<KeyBinding> keyBindings = getMatchingKeyBindings(keyCode).filter(keyBinding -> ((PriorityKeyBinding) keyBinding).onPressedPriority()).findFirst();
        return keyBindings.isPresent();
    }

    public static boolean onKeyReleasedPriority(InputUtil.Key keyCode) {
        // because streams are lazily evaluated, this code only calls onPressedPriority so often until one returns true
        Optional<KeyBinding> keyBindings = getMatchingKeyBindings(keyCode).filter(keyBinding -> ((PriorityKeyBinding) keyBinding).onReleasedPriority()).findFirst();
        return keyBindings.isPresent();
    }

    public static KeyMappingLookup getKeyMappingLookup() {
        return ObfuscationReflectionHelper.getPrivateValue(KeyBinding.class, null, "MAP");
    }
}
