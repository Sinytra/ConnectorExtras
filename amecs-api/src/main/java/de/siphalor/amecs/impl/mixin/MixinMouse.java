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

package de.siphalor.amecs.impl.mixin;

import de.siphalor.amecs.api.KeyBindingUtils;
import de.siphalor.amecs.api.KeyModifier;
import de.siphalor.amecs.impl.AmecsAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: Fix the priority when Mixin 0.8 is a thing and try again (-> MaLiLib causes incompatibilities)
@Environment(EnvType.CLIENT)
@Mixin(value = Mouse.class, priority = -2000)
public class MixinMouse {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	private double eventDeltaWheel;

	// If this method changes make sure to also change the corresponding code in KTIG
	private void onScrollReceived(double deltaY, boolean manualDeltaWheel, int scrollAmount) {
		if (manualDeltaWheel) {
			// from minecraft but patched
			// this code might be wrong when the vanilla mc code changes
			if (eventDeltaWheel != 0.0D && Math.signum(deltaY) != Math.signum(eventDeltaWheel)) {
				eventDeltaWheel = 0.0D;
			}

			eventDeltaWheel += deltaY;
			scrollAmount = (int) eventDeltaWheel;
			if (scrollAmount == 0) {
				return;
			}

			eventDeltaWheel -= scrollAmount;
			// -from minecraft
		}

		InputUtil.Key keyCode = KeyBindingUtils.getKeyFromScroll(scrollAmount);

		KeyBinding.setKeyPressed(keyCode, true);
		scrollAmount = Math.abs(scrollAmount);

		while (scrollAmount > 0) {
			KeyBinding.onKeyPressed(keyCode);
			scrollAmount--;
		}
		KeyBinding.setKeyPressed(keyCode, false);

		// default minecraft scroll logic is in HotbarScrollKeyBinding in amecs
	}

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSpectator()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void isSpectator_onMouseScroll(long window, double rawX, double rawY, CallbackInfo callbackInfo, double offset, double deltaY, int scrollAmount) {
		if (AmecsAPI.TRIGGER_KEYBINDING_ON_SCROLL) {
			onScrollReceived(KeyBindingUtils.getLastScrollAmount(), false, scrollAmount);
		}
	}

	@Surrogate
	private void isSpectator_onMouseScroll(long window, double rawX, double rawY, CallbackInfo callbackInfo, double offset, double deltaY, float scrollAmount) {
		isSpectator_onMouseScroll(window, rawX, rawY, callbackInfo, offset, deltaY, (int) scrollAmount);
	}

	// Invoked from ASM coremod hook
	@SuppressWarnings("unused")
	private boolean amecs$onMouseScrolledScreen(boolean handled) {
		if (handled) {
			return true;
		}

		if (AmecsAPI.TRIGGER_KEYBINDING_ON_SCROLL) {
			this.onScrollReceived(KeyBindingUtils.getLastScrollAmount(), true, 0);
		}
		return false;
	}

	@Inject(method = "onMouseScroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void onMouseScroll(long window, double rawX, double rawY, CallbackInfo callbackInfo, double offset, double deltaY) {
		InputUtil.Key keyCode = KeyBindingUtils.getKeyFromScroll(deltaY);

		// check if we have scroll input for the options screen
		if (client.currentScreen instanceof KeybindsScreen) {
			KeyBinding focusedBinding = ((KeybindsScreen) client.currentScreen).selectedKeyBinding;
			if (focusedBinding != null) {
				if (!focusedBinding.isUnbound()) {
					focusedBinding.setKeyModifierAndCode(KeyModifier.fromKey(focusedBinding.getKey()).toForgeKeyModifier(), focusedBinding.getKey());
				}
				// This is a bit hacky, but the easiest way out
				// If the selected binding != null, the mouse x and y will always be ignored - so no need to convert them
				// The key code that InputUtil.MOUSE.createFromCode chooses is always one bigger than the input
				client.currentScreen.mouseClicked(-1, -1, keyCode.getCode());
				// if we do we cancel the method because we do not want the current screen to get the scroll event
				callbackInfo.cancel();
				return;
			}
		}

		KeyBindingUtils.setLastScrollAmount(deltaY);
	}
}
