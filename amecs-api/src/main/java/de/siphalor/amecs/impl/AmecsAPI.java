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

import de.siphalor.amecs.api.KeyModifiers;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Mod(AmecsAPI.MOD_ID)
public class AmecsAPI {
	public static final String MOD_ID = "amecsapi";
	public static final String MOD_NAME = "Amecs API";

	public static final KeyModifiers CURRENT_MODIFIERS = new KeyModifiers();

	// this is used by KTIG
	public static boolean TRIGGER_KEYBINDING_ON_SCROLL = true;

	public static String makeKeyID(String keyName) {
		return "key." + MOD_ID + "." + keyName;
	}
}
