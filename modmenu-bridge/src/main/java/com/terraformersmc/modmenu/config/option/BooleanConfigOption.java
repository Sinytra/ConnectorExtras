/*
 * MIT License
 *
 * Copyright (c) 2018-2020 Prospector
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.terraformersmc.modmenu.config.option;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class BooleanConfigOption implements OptionConvertible {
    private final String key, translationKey;
    private final boolean defaultValue;
    private final Component enabledText;
    private final Component disabledText;

    public BooleanConfigOption(String key, boolean defaultValue, String enabledKey, String disabledKey) {
        ConfigOptionStorage.setBoolean(key, defaultValue);
        this.key = key;
        this.translationKey = "";
        this.defaultValue = defaultValue;
        this.enabledText = Component.translatable(translationKey + "." + enabledKey);
        this.disabledText = Component.translatable(translationKey + "." + disabledKey);
    }

    public BooleanConfigOption(String key, boolean defaultValue) {
        this(key, defaultValue, "true", "false");
    }

    public String getKey() {
        return key;
    }

    public boolean getValue() {
        return ConfigOptionStorage.getBoolean(key);
    }

    public void setValue(boolean value) {
        ConfigOptionStorage.setBoolean(key, value);
    }

    public void toggleValue() {
        ConfigOptionStorage.toggleBoolean(key);
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    public Component getButtonText() {
        return CommonComponents.optionNameValue(Component.translatable(translationKey), getValue() ? enabledText : disabledText);
    }

    @Override
    public OptionInstance<Boolean> asOption() {
        if (enabledText != null && disabledText != null) {
            return new OptionInstance<>(
                    translationKey,
                    OptionInstance.noTooltip(),
                    (text, value) -> value ? enabledText : disabledText,
                    OptionInstance.BOOLEAN_VALUES,
                    getValue(),
                    newValue -> ConfigOptionStorage.setBoolean(key, newValue)
            );
        }

        return OptionInstance.createBoolean(
                translationKey,
                getValue(),
                (value) -> ConfigOptionStorage.setBoolean(key, value)
        );
    }
}