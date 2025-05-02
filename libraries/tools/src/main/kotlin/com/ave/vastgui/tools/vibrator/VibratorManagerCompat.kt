/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.vibrator

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.tools.utils.permission.isPermissionGranted

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/1
// Documentation:

/**
 * Helper for accessing features in [VibratorManager] in a backwards
 * compatible fashion.
 *
 * @since 1.5.2
 */
class VibratorManagerCompat(private val context: Context) {

    /**
     * The device's [VibratorManager].
     *
     * @since 1.5.2
     */
    var vibratorManager: VibratorManager? = null
        private set

    /**
     * The device's [Vibrator].
     *
     * @since 1.5.2
     */
    val requireVibratorManager: VibratorManager
        get() = if (isAvailable) vibratorManager!! else throw RuntimeException("The vibratorManager is not available.")

    /**
     * Return `true` if the [Manifest.permission.VIBRATE] is granted and the
     * [vibratorManager] is not null.
     *
     * @since 1.5.2
     */
    val isAvailable: Boolean
        get() = isPermissionGranted() && vibratorManager != null

    /**
     * List all available vibrator ids, returning a possible empty list.
     *
     * @since 1.5.2
     */
    val ids: IntArray
        @SuppressLint("NewApi")
        get() = vibratorManager?.vibratorIds ?: intArrayOf()

    /**
     * The device's [VibratorCompat]. The value of [VibratorCompat.vibrator]
     * is [VibratorManager.getDefaultVibrator] if the SDK version is version
     * [Build.VERSION_CODES.S] and above, or [Context.VIBRATOR_SERVICE]
     * if the SDK version is below [Build.VERSION_CODES.S].
     *
     * @since 1.5.2
     */
    @SuppressLint("NewApi")
    fun getDefaultVibrator(): VibratorCompat {
        return VibratorCompat(context)
    }

    /**
     * Retrieve a single [VibratorCompat] by id.
     *
     * @see isAvailable
     * @since 1.5.2
     */
    @SuppressLint("NewApi")
    fun getVibrator(vibratorId: Int): VibratorCompat? {
        var vibratorCompat: VibratorCompat? = null
        if (isAvailable && ids.contains(vibratorId)) {
            vibratorCompat = VibratorCompat(context, vibratorManager!!.getVibrator(vibratorId))
        }
        return vibratorCompat
    }

    /**
     * Turn all the vibrators off.
     *
     * @see isAvailable
     * @since 1.5.2
     */
    @SuppressLint("NewApi", "MissingPermission")
    fun cancel() {
        if (!isAvailable) return
        vibratorManager!!.cancel()
    }

    /**
     * Return `true` if the [Manifest.permission.VIBRATE] granted. False
     * otherwise.
     *
     * @since 1.5.2
     */
    private fun isPermissionGranted(): Boolean {
        return context.isPermissionGranted(Manifest.permission.VIBRATE)
    }

    init {
        vibratorManager =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ContextCompat.getSystemService(context, VibratorManager::class.java) else null
    }

    companion object : SingletonHolder<VibratorManagerCompat, Context>(::VibratorManagerCompat)

}