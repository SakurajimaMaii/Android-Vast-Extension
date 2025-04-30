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
import android.os.VibrationEffect
import android.os.VibrationEffect.Composition
import android.os.Vibrator
import android.os.Vibrator.VIBRATION_EFFECT_SUPPORT_YES
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.utils.permission.isPermissionGranted

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/30

class VibratorCompat(private val context: Context) {

    @IntDef(
        VibrationEffect.EFFECT_TICK,
        VibrationEffect.EFFECT_CLICK,
        VibrationEffect.EFFECT_HEAVY_CLICK,
        VibrationEffect.EFFECT_DOUBLE_CLICK
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class EffectType

    @IntDef(
        Composition.PRIMITIVE_CLICK,
        Composition.PRIMITIVE_SPIN,
        Composition.PRIMITIVE_THUD,
        Composition.PRIMITIVE_TICK,
        Composition.PRIMITIVE_LOW_TICK,
        Composition.PRIMITIVE_QUICK_FALL,
        Composition.PRIMITIVE_QUICK_RISE,
        Composition.PRIMITIVE_SLOW_RISE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class PrimitiveType

    private val vibrator =
        ContextCompat.getSystemService(context, Vibrator::class.java)

    val hasVibrator: Boolean
        get() = vibrator?.hasVibrator() == true

    val isAvailable: Boolean
        get() = isPermissionGranted() && hasVibrator

    val hasAmplitudeControl: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator?.hasAmplitudeControl() == true

    val id: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) vibrator?.id ?: -1 else -1

    val qFactor: Float
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            vibrator?.qFactor ?: Float.NaN else Float.NaN

    val resonantFrequency: Float
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            vibrator?.resonantFrequency ?: Float.NaN else Float.NaN

    @SuppressLint("MissingPermission")
    fun vibrate(timeMills: Long) {
        if (!isAvailable) return
        vibrator?.vibrate(timeMills)
    }

    fun areEffectsSupported(@EffectType effectIds: IntArray): IntArray {
        var result: IntArray = intArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result = vibrator?.areEffectsSupported(*effectIds) ?: result
        }
        return result
    }

    fun areAllEffectsSupported(@EffectType effectIds: IntArray): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator?.areAllEffectsSupported(*effectIds) == VIBRATION_EFFECT_SUPPORT_YES
    }

    fun arePrimitivesSupported(@PrimitiveType primitiveIds: IntArray): BooleanArray {
        var result: BooleanArray = booleanArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            result = vibrator?.arePrimitivesSupported(*primitiveIds) ?: result
        }
        return result
    }

    fun areAllPrimitivesSupported(@PrimitiveType primitiveIds: IntArray): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator?.areAllPrimitivesSupported(*primitiveIds) == true
    }

    fun getPrimitiveDurations(@PrimitiveType primitiveIds: IntArray): IntArray {
        var result: IntArray = intArrayOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            result = vibrator?.getPrimitiveDurations(*primitiveIds) ?: result
        }
        return result
    }

    @SuppressLint("MissingPermission")
    fun cancel() {
        if (!isAvailable) return
        vibrator?.cancel()
    }

    private fun isPermissionGranted(): Boolean {
        return context.isPermissionGranted(Manifest.permission.VIBRATE)
    }

}