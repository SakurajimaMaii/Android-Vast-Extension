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

package com.ave.vastgui.tools.location

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/5
// Documentation:

/**
 * Unlike the [LocationResultListener], the [SingleLocationResultListener]
 * will be removed by [LocationManagerCompat.removeUpdates]
 * when [onLocationChanged] is invoked.
 *
 * @since 1.5.3
 */
abstract class SingleLocationResultListener(context: Context) : LocationResultListener {

    /** @since 1.5.3 */
    private val locationManager =
        ContextCompat.getSystemService(context.applicationContext, LocationManager::class.java)

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onLocationChanged(locations: List<Location>) {
        super.onLocationChanged(locations)
        locationManager?.let { LocationManagerCompat.removeUpdates(it, this) }
    }

}