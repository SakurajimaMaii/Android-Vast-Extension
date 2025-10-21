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

package com.ave.vastgui.appcompose.ui

import android.hardware.SensorEvent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.sensor.DefaultSensorEventListener
import com.ave.vastgui.tools.sensor.MultiSensor
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/10/21

class SensorActivity : ComponentActivity(),
    DefaultSensorEventListener {

    private var multiSensor: MultiSensor by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidVastExtensionTheme {

            }
        }

        multiSensor = MultiSensor.getInstance(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        multiSensor.requireManager().registerListener(this, multiSensor.linearAcceleration!!, 1000)
    }

    override fun onPause() {
        super.onPause()
        multiSensor.requireManager().unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d(TAG, "event=${event.values.joinToString(",")}")
    }

    companion object {
        private const val TAG = "SensorActivity"
    }

}