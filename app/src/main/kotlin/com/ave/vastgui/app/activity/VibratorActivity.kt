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

package com.ave.vastgui.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityVibratorBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.string
import com.ave.vastgui.tools.vibrator.EffectType
import com.ave.vastgui.tools.vibrator.PrimitiveType
import com.ave.vastgui.tools.vibrator.VibratorCompat
import com.ave.vastgui.tools.vibrator.VibratorManagerCompat
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation:

class VibratorActivity : ComponentActivity(R.layout.activity_vibrator) {

    private val logger = logFactory(VibratorActivity::class.java)

    private val binding by viewBinding(ActivityVibratorBinding::bind)

    private var manager: VibratorManagerCompat by Delegates.notNull()
    private var vibrator: VibratorCompat by Delegates.notNull()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = VibratorManagerCompat.getInstance(applicationContext)

        // region 振动管理器
        binding.idsTv.text = String.format(string(R.string.vibrator_manager_ids), manager.ids.joinToString(","))
        binding.isAvailableTv.text = String.format(string(R.string.vibrator_manager_is_available), manager.isAvailable.toString())
        binding.vibratorManagerVibrateBtn.apply {
            text = String.format(getString(R.string.vibrator_manager_single_vibrate), 2000)
            setOnClickListener {
                @Suppress("DEPRECATION")
                manager.getDefaultVibrator().requireVibrator.vibrate(2000)
            }
        }
        binding.vibratorManagerCancelBtn.setOnClickListener {
            manager.cancel()
        }
        // endregion

        // region 振动器
        vibrator = VibratorCompat(this)
        binding.hasVibratorTv.text = String.format(string(R.string.vibrator_has_hardware), vibrator.hasVibrator)
        binding.hasAmplitudeControlTv.text = String.format(string(R.string.vibrator_has_amplitude_control), vibrator.hasAmplitudeControl)
        binding.vibratorIdTv.text = String.format(string(R.string.vibrator_id), vibrator.id)
        binding.vibratorQFactorTv.text = String.format(string(R.string.vibrator_qfactor), vibrator.qFactor)
        binding.vibratorResonantfrequencyTv.text = String.format(string(R.string.vibrator_resonantfrequency), vibrator.resonantFrequency)
        binding.vibratorVibrateBtn.apply {
            text = String.format(getString(R.string.vibrator_single_vibrate), 2000)
            setOnClickListener {
                @Suppress("DEPRECATION")
                vibrator.requireVibrator.vibrate(2000)
            }
        }
        binding.vibratorCancelBtn.setOnClickListener {
            vibrator.cancel()
        }

        logger.d { vibrator.areEffectsSupported(*(EffectType.entries.toTypedArray())).joinToString(",") }
        logger.d { vibrator.arePrimitivesSupported(*(PrimitiveType.entries.toTypedArray())).joinToString(",") }
        logger.d { vibrator.getPrimitiveDurations(*(PrimitiveType.entries.toTypedArray())).joinToString(",") }
        // endregion
    }

}