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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

/**
 * A {@link net.minecraft.client.option.KeyBinding} base class to be used when you want to define modifiers keys as default
 */
@Environment(EnvType.CLIENT)
public class AmecsKeyBinding extends KeyBinding {
	private final KeyModifiers defaultModifiers;

	/**
	 * Constructs a new amecs keybinding. And because how the vanilla key binding works. It is automatically registered.
	 * <br>
	 * See {@link KeyBindingUtils#unregisterKeyBinding(KeyBinding)} for how to unregister it
	 * If you want to set the key's translationKey directly use {@link #AmecsKeyBinding(String, net.minecraft.client.util.InputUtil.Type, int, String, KeyModifiers)} instead
	 *
	 * @param id the id to use
	 * @param type the input type which triggers this keybinding
	 * @param code the default key code
	 * @param category the id of the category which should include this keybinding
	 * @param defaultModifiers the default modifiers
	 */
	public AmecsKeyBinding(Identifier id, InputUtil.Type type, int code, String category, KeyModifiers defaultModifiers) {
		this("key." + id.getNamespace() + "." + id.getPath(), type, code, category, defaultModifiers);
	}

	/**
	 * Constructs a new amecs keybinding. And because how the vanilla key binding works. It is automatically registered.
	 * <br>
	 * See {@link KeyBindingUtils#unregisterKeyBinding(KeyBinding)} for how to unregister it
	 *
	 * @param id the id to use
	 * @param type the input type which triggers this keybinding
	 * @param code the default key code
	 * @param category the id of the category which should include this keybinding
	 * @param defaultModifiers the default modifiers
	 */
	public AmecsKeyBinding(String id, InputUtil.Type type, int code, String category, KeyModifiers defaultModifiers) {
		super(id, KeyConflictContext.UNIVERSAL, defaultModifiers != null ? defaultModifiers.getFirst() : KeyModifier.NONE, type, code, category);
		if (defaultModifiers == null || defaultModifiers == KeyModifiers.NO_MODIFIERS) {
			defaultModifiers = new KeyModifiers(); // the modifiable version of: KeyModifiers.NO_MODIFIERS
		}
		this.defaultModifiers = defaultModifiers;
	}

	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		if (pressed) {
			onPressed();
		} else {
			onReleased();
		}
	}

	/**
	 * A convenience method which gets fired when the keybinding is used
	 */
	public void onPressed() {
	}

	/**
	 * A convenience method which gets fired when the keybinding is stopped being used
	 */
	public void onReleased() {
	}

	/**
	 * Resets this keybinding (triggered when the user clicks on the "Reset" button).
	 */
	public void resetKeyBinding() {
		setKeyModifierAndCode(getDefaultKeyModifier(), getKey());
	}

	public KeyModifiers getDefaultModifiers() {
		return defaultModifiers;
	}
}
