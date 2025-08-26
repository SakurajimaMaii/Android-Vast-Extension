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
import androidx.core.location.LocationRequestCompat
import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.tools.utils.permission.isPermissionDenied
import java.util.concurrent.Executors

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/5
// Documentation:

/**
 * A simple location client based on [LocationManager].
 *
 * @since 1.5.3
 */
class LocationClient private constructor(context: Context) {

    /** @since 1.5.3 */
    private val context = context.applicationContext

    /** @since 1.5.3 */
    private val executor = Executors.newCachedThreadPool()

    /** @since 1.5.3 */
    private val locationManager =
        ContextCompat.getSystemService(this.context, LocationManager::class.java)

    /**
     * @see [LocationManager.getProviders]
     * @since 1.5.3
     */
    fun getProviders(enabledOnly: Boolean): List<String> {
        return try {
            locationManager?.getProviders(enabledOnly) ?: emptyList()
        } catch (_: Throwable) {
            emptyList()
        }
    }

    /**
     * Try to get the last known location from available location providers.
     *
     * This will never activate sensors to compute a new location, and will
     * only ever return a cached location.
     *
     * @see LocationManager.getLastKnownLocation
     * @see LocationManager.getProviders
     * @since 1.5.3
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun tryGetLastKnownLocation(): Location? {
        var location: Location? = null
        try {
            if (locationManager == null) {
                throw RuntimeException("The LocationManager is unsupported.")
            }

            if (context.isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
                && context.isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                throw SecurityException("Requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.")
            }

            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                val loc = locationManager.getLastKnownLocation(provider) ?: continue
                if (loc.accuracy < (location?.accuracy ?: Float.MAX_VALUE)) {
                    location = loc
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return location
    }

    /**
     * Start location.
     *
     * @param provider a provider listed by [LocationManager.getAllProviders].
     * @param listener the listener to receive location updates.
     * @param locationRequest the location request containing location
     * parameters.
     * @param allowLastKnownLocation whether to allow the
     * last known location to be returned as a result
     * via [LocationResultListener.onLocationChanged]
     * (only if the location exists).
     * @since 1.5.3
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    @JvmOverloads
    fun startLocation(provider: String,
                      listener: LocationResultListener,
                      locationRequest: LocationRequestCompat = LocationRequestCompat.Builder(1000L).build(),
                      allowLastKnownLocation: Boolean = false) {
        try {
            if (locationManager == null) {
                throw RuntimeException("The LocationManager is unsupported.")
            }

            if (!LocationManagerCompat.hasProvider(locationManager, provider)) {
                throw IllegalArgumentException("The provider (value = $provider) is unsupported.")
            }

            if (context.isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
                && context.isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                throw SecurityException("Requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.")
            }

            if (!isLocationEnabled()) {
                throw RuntimeException("Current the state of location is disabled.")
            }

            if (allowLastKnownLocation) {
                tryGetLastKnownLocation()?.also { listener.onLocationChanged(it) }
                return
            }

            LocationManagerCompat.requestLocationUpdates(locationManager, provider, locationRequest, executor, listener)
        } catch (ex: Exception) {
            ex.printStackTrace()
            listener.onLocationFailure(ex)
        }
    }

    /**
     * Stop location.
     *
     * @param listener the listener will be removed.
     * @since 1.5.3
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun stopLocation(listener: LocationResultListener) {
        try {
            if (locationManager == null) {
                throw RuntimeException("The LocationManager is unsupported.")
            }

            if (context.isPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
                && context.isPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                throw SecurityException("Requires ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permission.")
            }

            LocationManagerCompat.removeUpdates(locationManager, listener)
        } catch (ex: Exception) {
            ex.printStackTrace()
            listener.onLocationFailure(ex)
        }
    }

    companion object : SingletonHolder<LocationClient, Context>(::LocationClient)

}