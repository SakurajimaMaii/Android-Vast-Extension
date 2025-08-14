import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ave.vastgui.tools.sensor.MultiSensor
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/8/11

@RunWith(AndroidJUnit4::class)
class SensorTests {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun gravity() {
        val countDownLatch = CountDownLatch(1)
        val multiSensor = MultiSensor.getInstance(context)
        val gravity = multiSensor.gravity
        Log.d("Test", "重力传感器是否为空：${gravity == null}")
        if (gravity != null) {
            multiSensor.sensor?.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    Log.d("Test", "event=${event.values.joinToString(",")}")
                    countDownLatch.countDown()
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    Log.d("Test", "sensor=${sensor} accuracy=${accuracy}")
                }
            }, gravity, 1000)
        }
        countDownLatch.await()
    }
}