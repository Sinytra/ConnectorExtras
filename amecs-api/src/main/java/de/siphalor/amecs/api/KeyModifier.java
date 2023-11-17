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

package de.siphalor.amecs.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.siphalor.amecs.impl.AmecsAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
@Environment(EnvType.CLIENT)
public enum KeyModifier {
	// the order of the enums makes a difference when generating the shown name in the gui
	// with this order the old text order is preserved. But now the id values do not increment nicely. But changing them would eliminate
	// backward compatibility with the old save format
	NONE("none", -1),
	ALT("alt", 0, 342, 346),
	SHIFT("shift", 2, 340, 344),
	CONTROL("control", 1, 341, 345);

	// using this array for the values because it is faster than calling values() every time
	public static final KeyModifier[] VALUES = KeyModifier.values();
	private static final BiMap<KeyModifier, net.minecraftforge.client.settings.KeyModifier> FORGE_MAP = ImmutableBiMap.copyOf(Stream.of(VALUES)
		.collect(Collectors.toMap(Function.identity(), m -> net.minecraftforge.client.settings.KeyModifier.valueFromString(m.name()))));

	public final String name;
	public final int id;
	// these keyCodes are all from Type: InputUtil.Type.KEYSYM
	final int[] keyCodes;

	KeyModifier(String name, int id, int... keyCodes) {
		this.name = name;
		this.id = id;
		this.keyCodes = keyCodes;
	}

	public static KeyModifier fromKeyCode(int keyCode) {
		for (KeyModifier keyModifier : VALUES) {
			if (keyModifier == NONE) {
				continue;
			}
			if (keyModifier.matches(keyCode)) {
				return keyModifier;
			}
		}
		return NONE;
	}

	public static KeyModifier fromKey(InputUtil.Key key) {
		if (key == null || key.getCategory() != InputUtil.Type.KEYSYM) {
			return NONE;
		}
		return fromKeyCode(key.getCode());
	}

	public boolean matches(int keyCode) {
		return ArrayUtils.contains(keyCodes, keyCode);
	}

	public String getTranslationKey() {
		return AmecsAPI.MOD_ID + ".modifier." + name;
	}

	public static int getModifierCount() {
		return VALUES.length - 1; // remove 1 for NONE
	}

	public net.minecraftforge.client.settings.KeyModifier toForgeKeyModifier() {
		return FORGE_MAP.getOrDefault(this, net.minecraftforge.client.settings.KeyModifier.NONE);
	}

	public static KeyModifier fromForgeKeyModifier(net.minecraftforge.client.settings.KeyModifier modifier) {
		return FORGE_MAP.inverse().getOrDefault(modifier, NONE);
	}
}
