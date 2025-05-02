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
import android.media.AudioAttributes
import android.os.Build
import android.os.Process
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Vibrator.VIBRATION_EFFECT_SUPPORT_YES
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.utils.permission.isPermissionGranted
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/30

/**
 * Helper for accessing features in [Vibrator] in a backwards compatible
 * fashion.
 *
 * @since 1.5.2
 */
class VibratorCompat {

    /**
     * @see VibratorManagerCompat.getDefaultVibrator
     * @since 1.5.2
     */
    constructor(context: Context) {
        this.context = context
        vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
    }

    /**
     * @see VibratorManagerCompat.getVibrator
     * @since 1.5.2
     */
    @RequiresApi(Build.VERSION_CODES.S)
    internal constructor(context: Context, vibrator: Vibrator) {
        this.context = context
        this.vibrator = vibrator
    }

    private var context: Context by Delegates.notNull()

    /**
     * The device's [Vibrator].
     *
     * @since 1.5.2
     */
    var vibrator: Vibrator? = null
        private set

    /**
     * The device's [Vibrator].
     *
     * @since 1.5.2
     */
    val requireVibrator: Vibrator
        get() = if (isAvailable) vibrator!! else throw RuntimeException("The vibrator is not available.")

    /**
     * @see Vibrator.hasVibrator
     * @since 1.5.2
     */
    val hasVibrator: Boolean
        get() = vibrator?.hasVibrator() == true

    /**
     * Return `true` if the [Manifest.permission.VIBRATE] is granted and the
     * [hasVibrator] is `true`.
     *
     * @since 1.5.2
     */
    val isAvailable: Boolean
        get() = isPermissionGranted() && hasVibrator

    /**
     * @see Vibrator.hasAmplitudeControl
     * @since 1.5.2
     */
    val hasAmplitudeControl: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasAmplitudeControl() == true

    /**
     * @see Vibrator.id
     * @since 1.5.2
     */
    val id: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) vibrator?.id
            ?: NO_SERVICE_ATTACHED else NO_SERVICE_ATTACHED

    /**
     * @see Vibrator.qFactor
     * @since 1.5.2
     */
    val qFactor: Float
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            vibrator?.qFactor ?: Float.NaN else Float.NaN

    /**
     * @see Vibrator.resonantFrequency
     * @since 1.5.2
     */
    val resonantFrequency: Float
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            vibrator?.resonantFrequency ?: Float.NaN else Float.NaN

    /**
     * @see Vibrator.areEffectsSupported
     * @since 1.5.2
     */
    fun areEffectsSupported(@EffectType effectIds: IntArray): IntArray {
        var result: IntArray = intArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result = vibrator?.areEffectsSupported(*effectIds) ?: result
        }
        return result
    }

    /**
     * @see Vibrator.areAllEffectsSupported
     * @since 1.5.2
     */
    fun areAllEffectsSupported(@EffectType effectIds: IntArray): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator?.areAllEffectsSupported(*effectIds) == VIBRATION_EFFECT_SUPPORT_YES
    }

    /**
     * @see Vibrator.arePrimitivesSupported
     * @since 1.5.2
     */
    fun arePrimitivesSupported(@PrimitiveType primitiveIds: IntArray): BooleanArray {
        var result: BooleanArray = booleanArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result = vibrator?.arePrimitivesSupported(*primitiveIds) ?: result
        }
        return result
    }

    /**
     * @see Vibrator.areAllPrimitivesSupported
     * @since 1.5.2
     */
    fun areAllPrimitivesSupported(@PrimitiveType primitiveIds: IntArray): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator?.areAllPrimitivesSupported(*primitiveIds) == true
    }

    /**
     * @see Vibrator.getPrimitiveDurations
     * @since 1.5.2
     */
    fun getPrimitiveDurations(@PrimitiveType primitiveIds: IntArray): IntArray {
        var result: IntArray = intArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result = vibrator?.getPrimitiveDurations(*primitiveIds) ?: result
        }
        return result
    }

    /**
     * @see Vibrator.cancel
     * @since 1.5.2
     */
    @SuppressLint("MissingPermission")
    fun cancel() {
        if (!isAvailable) return
        vibrator?.cancel()
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

    companion object {
        const val NO_SERVICE_ATTACHED = -1
    }

}