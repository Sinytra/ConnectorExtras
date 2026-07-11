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

import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Locale;

public class EnumConfigOption<E extends Enum<E>> implements OptionConvertible {
    private final String key, translationKey;
    private final Class<E> enumClass;
    private final E defaultValue;

    public EnumConfigOption(String key, E defaultValue) {
        ConfigOptionStorage.setEnum(key, defaultValue);
        this.key = key;
        this.translationKey = "";
        this.enumClass = defaultValue.getDeclaringClass();
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public E getValue() {
        return ConfigOptionStorage.getEnum(key, enumClass);
    }

    public void setValue(E value) {
        ConfigOptionStorage.setEnum(key, value);
    }

    public void cycleValue() {
        ConfigOptionStorage.cycleEnum(key, enumClass);
    }

    public void cycleValue(int amount) {
        ConfigOptionStorage.cycleEnum(key, enumClass, amount);
    }

    public E getDefaultValue() {
        return defaultValue;
    }

    private static <E extends Enum<E>> Component getValueText(EnumConfigOption<E> option, E value) {
        return Component.translatable(option.translationKey + "." + value.name().toLowerCase(Locale.ROOT));
    }

    public Component getButtonText() {
        return CommonComponents.optionNameValue(Component.translatable(translationKey), getValueText(this, getValue()));
    }

    @Override
    public OptionInstance<E> asOption() {
        return new OptionInstance<>(translationKey,
            OptionInstance.noTooltip(),
            (text, value) -> getValueText(this, value),
            new OptionInstance.Enum<>(Arrays.asList(enumClass.getEnumConstants()),
                Codec.STRING.xmap(string -> Arrays.stream(enumClass.getEnumConstants())
                    .filter(e -> e.name().toLowerCase().equals(string))
                    .findAny()
                    .orElse(null), newValue -> newValue.name().toLowerCase())
            ),
            getValue(),
            value -> ConfigOptionStorage.setEnum(key, value)
        );
    }
}