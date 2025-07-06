import android.Manifest
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.ave.vastgui.tools.location.LocationClient
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 * Copyright 2021-2025 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/6
// Documentation:

@RunWith(AndroidJUnit4::class)
class LocationTests {

    val runtimePermissionRule: GrantPermissionRule
        @Rule get() = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val locationClient = LocationClient.getInstance(context)

    @Test
    fun tryGetLastKnownLocation() {
        Log.d(TAG, "The result location is ${locationClient.tryGetLastKnownLocation()}")
    }

    companion object {
        const val TAG = "LocationTests"
    }

}