/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.hardware

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import com.ave.vastgui.core.extension.cast

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/3 9:53

/**
 * Audio manager
 *
 * @since 1.5.0
 */
fun Context.audioManager(): AudioManager =
    cast(getSystemService(Context.AUDIO_SERVICE))

/**
 * Determine whether the device includes at least one form of audio output.
 *
 * @since 1.5.0
 */
fun Context.isFeatureAudioOutput(version: Int = Build.VERSION.SDK_INT): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT, version)
    } else {
        packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)
    }
}

/**
 * Determine whether a wired headset is connected or not.
 *
 * @since 1.5.0
 */
fun Context.isWiredHeadsetOn(): Boolean {
    if (!isFeatureAudioOutput()) return false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        audioManager().getDevices(AudioManager.GET_DEVICES_OUTPUTS).forEach {
            if (AudioDeviceInfo.TYPE_WIRED_HEADSET == it.type) {
                return true
            }
        }
        return false
    } else {
        return audioManager().isWiredHeadsetOn
    }
}