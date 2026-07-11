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
package com.terraformersmc.modmenu.api;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface UpdateInfo {
    /**
     * @return If an update for the mod is available.
     */
    boolean isUpdateAvailable();

    /**
     * @return The message that is getting displayed when an update is available or <code>null</code> to let ModMenu handle displaying the message.
     */
    @Nullable
    default Component getUpdateMessage() {
        return null;
    }

    /**
     * @return The URL to the mod download.
     */
    String getDownloadLink();

    /**
     * @return The update channel this update is available for.
     */
    UpdateChannel getUpdateChannel();
}