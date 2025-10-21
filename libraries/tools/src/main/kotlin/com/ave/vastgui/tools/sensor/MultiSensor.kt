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

package com.ave.vastgui.tools.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.core.content.ContextCompat
import com.ave.vastgui.core.extension.SingletonHolder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/8

/** @since 1.5.3 */
class MultiSensor internal constructor(context: Context) {

    val manager = ContextCompat.getSystemService(context, SensorManager::class.java)

    val gravity: Sensor? =
        manager?.getDefaultSensor(Sensor.TYPE_GRAVITY)

    val linearAcceleration: Sensor? =
        manager?.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    val rotation: Sensor? =
        manager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    fun requireManager(): SensorManager {
        assert(manager != null) { "The sensorManager is null." }
        return manager!!
    }

    companion object : SingletonHolder<MultiSensor, Context>(::MultiSensor)

}