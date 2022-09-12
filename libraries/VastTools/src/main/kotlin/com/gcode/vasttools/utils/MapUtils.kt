/*
 * Copyright 2022 VastGui
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

package com.gcode.vasttools.utils

import android.content.Context
import android.location.LocationManager
import com.gcode.vasttools.helper.ContextHelper

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 17:41
// Description:
// Documentation:

object MapUtils {
    /**
     * Determine if GPS is turned on.
     *
     * @return true if GPS is turned on, false otherwise.
     * @since 0.0.8
     */
    @JvmStatic
    fun isGPSOPen(): Boolean {
        val locationManager = ContextHelper.getAppContext()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Through GPS satellite positioning, the positioning level can be accurate to the street
        // (through 24 satellite positioning, the positioning is accurate and fast in outdoor and open places).
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // Position determined by WLAN or mobile network (3G/2G) (also called AGPS, Assisted GPS Positioning.
        // Mainly used for positioning indoors or in densely covered places (buildings or dense deep forest, etc.).
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps && network) {
            return true
        }
        return false
    }
}