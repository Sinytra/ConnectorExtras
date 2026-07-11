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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigOptionStorage {
    private static final Map<String, Boolean> BOOLEAN_OPTIONS = new HashMap<>();
    private static final Map<String, Enum<?>> ENUM_OPTIONS = new HashMap<>();
    private static final Map<String, Set<String>> STRING_SET_OPTIONS = new HashMap<>();

    public static void setStringSet(String key, Set<String> value) {
        STRING_SET_OPTIONS.put(key, value);
    }

    public static Set<String> getStringSet(String key) {
        return STRING_SET_OPTIONS.get(key);
    }

    public static void setBoolean(String key, boolean value) {
        BOOLEAN_OPTIONS.put(key, value);
    }

    public static void toggleBoolean(String key) {
        setBoolean(key, !getBoolean(key));
    }

    public static boolean getBoolean(String key) {
        return BOOLEAN_OPTIONS.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E getEnum(String key, Class<E> typeClass) {
        return (E) ENUM_OPTIONS.get(key);
    }

    public static Enum<?> getEnumTypeless(String key, Class<Enum<?>> typeClass) {
        return ENUM_OPTIONS.get(key);
    }

    public static <E extends Enum<E>> void setEnum(String key, E value) {
        ENUM_OPTIONS.put(key, value);
    }

    public static void setEnumTypeless(String key, Enum<?> value) {
        ENUM_OPTIONS.put(key, value);
    }

    public static <E extends Enum<E>> E cycleEnum(String key, Class<E> typeClass) {
        return cycleEnum(key, typeClass, 1);
    }

    public static <E extends Enum<E>> E cycleEnum(String key, Class<E> typeClass, int amount) {
        E[] values = typeClass.getEnumConstants();
        E currentValue = getEnum(key, typeClass);
        E newValue = values[(currentValue.ordinal() + values.length + amount) % values.length];
        setEnum(key, newValue);
        return newValue;
    }
}