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

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.ave.vastgui.core.coroutines.suspendCoroutineWithTimeout
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.coroutines.resumeWithException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/6
// Documentation:

/** @since 1.5.3 */
class GeocoderCompat {

    /** @since 1.5.3 */
    constructor(context: Context) {
        geocoder = Geocoder(context.applicationContext)
    }

    /** @since 1.5.3 */
    constructor(context: Context, locale: Locale) {
        geocoder = Geocoder(context.applicationContext, locale)
    }

    /** @since 1.5.3 */
    val geocoder: Geocoder

    /**
     * @see Geocoder.getFromLocation
     * @since 1.5.3
     */
    fun getFromLocation(@FloatRange(from = -90.0, to = 90.0) latitude: Double,
                        @FloatRange(from = -180.0, to = 180.0) longitude: Double,
                        @IntRange(from = 0) maxResults: Int,
                        listener: GeocodeListenerCompat) {
        if (!isPresent) {
            listener.onError("There doesn't exist a geocoder implementation present that may return results.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(latitude, longitude, maxResults, listener.toGeocodeListener())
            } catch (ex: Exception) {
                listener.onError(ex.message)
            }
        } else {
            executor.submit {
                try {
                    @Suppress("DEPRECATION")
                    val address = geocoder
                        .getFromLocation(latitude, longitude, maxResults) ?: emptyList()
                    listener.onGeocode(address)
                } catch (ex: Exception) {
                    listener.onError(ex.message)
                }
            }
        }
    }

    /**
     * @see Geocoder.getFromLocation
     * @since 1.5.3
     */
    suspend fun getFromLocation(@FloatRange(from = -90.0, to = 90.0) latitude: Double,
                                @FloatRange(from = -180.0, to = 180.0) longitude: Double,
                                @IntRange(from = 0) maxResults: Int,
                                timeoutMills: Long = 60000L) =
        suspendCoroutineWithTimeout(timeoutMills) { continuation ->
            getFromLocation(latitude, longitude, maxResults, object : GeocodeListenerCompat {
                override fun onGeocode(addresses: List<Address>) {
                    continuation.resumeWith(Result.success(addresses))
                }

                override fun onError(errorMessage: String?) {
                    continuation.resumeWithException(Throwable(errorMessage))
                }
            })
        }

    /**
     * @see Geocoder.getFromLocationName
     * @since 1.5.3
     */
    fun getFromLocationName(locationName: String, @IntRange maxResults: Int, listener: GeocodeListenerCompat) {
        if (!isPresent) {
            listener.onError("There doesn't exist a geocoder implementation present that may return results.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocationName(locationName, maxResults, listener.toGeocodeListener())
            } catch (ex: Exception) {
                listener.onError(ex.message)
            }
        } else {
            executor.submit {
                try {
                    @Suppress("DEPRECATION")
                    val address = geocoder
                        .getFromLocationName(locationName, maxResults) ?: emptyList()
                    listener.onGeocode(address)
                } catch (ex: Exception) {
                    listener.onError(ex.message)
                }
            }
        }
    }

    /**
     * @see Geocoder.getFromLocationName
     * @since 1.5.3
     */
    suspend fun getFromLocationName(locationName: String, @IntRange maxResults: Int, timeoutMills: Long = 60000L) =
        suspendCoroutineWithTimeout(timeoutMills) { continuation ->
            getFromLocationName(locationName, maxResults, object : GeocodeListenerCompat {
                override fun onGeocode(addresses: List<Address>) {
                    continuation.resumeWith(Result.success(addresses))
                }

                override fun onError(errorMessage: String?) {
                    continuation.resumeWithException(Throwable(errorMessage))
                }
            })
        }

    /**
     * @see Geocoder.getFromLocationName
     * @since 1.5.3
     */
    fun getFromLocationName(locationName: String,
                            @IntRange maxResults: Int,
                            @FloatRange(from = -90.0, to = 90.0) lowerLeftLatitude: Double,
                            @FloatRange(from = -180.0, to = 180.0) lowerLeftLongitude: Double,
                            @FloatRange(from = -90.0, to = 90.0) upperRightLatitude: Double,
                            @FloatRange(from = -180.0, to = 180.0) upperRightLongitude: Double,
                            listener: GeocodeListenerCompat) {
        if (!isPresent) {
            listener.onError("There doesn't exist a geocoder implementation present that may return results.")
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude,
                    upperRightLatitude, upperRightLongitude, listener.toGeocodeListener())
            } catch (ex: Exception) {
                listener.onError(ex.message)
            }
        } else {
            executor.submit {
                try {
                    @Suppress("DEPRECATION")
                    val address = geocoder.getFromLocationName(locationName, maxResults, lowerLeftLatitude,
                        lowerLeftLongitude, upperRightLatitude, upperRightLongitude) ?: emptyList()
                    listener.onGeocode(address)
                } catch (ex: Exception) {
                    listener.onError(ex.message)
                }
            }
        }
    }

    /**
     * @see Geocoder.getFromLocationName
     * @since 1.5.3
     */
    suspend fun getFromLocationName(locationName: String,
                                    @IntRange maxResults: Int,
                                    @FloatRange(from = -90.0, to = 90.0) lowerLeftLatitude: Double,
                                    @FloatRange(from = -180.0, to = 180.0) lowerLeftLongitude: Double,
                                    @FloatRange(from = -90.0, to = 90.0) upperRightLatitude: Double,
                                    @FloatRange(from = -180.0, to = 180.0) upperRightLongitude: Double,
                                    timeoutMills: Long = 60000L) =
        suspendCoroutineWithTimeout(timeoutMills) { continuation ->
            getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude,
                upperRightLatitude, upperRightLongitude, object : GeocodeListenerCompat {
                    override fun onGeocode(addresses: List<Address>) {
                        continuation.resumeWith(Result.success(addresses))
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resumeWithException(Throwable(errorMessage))
                    }
                })
        }

    companion object {
        /** @since 1.5.3 */
        private val executor = Executors.newCachedThreadPool()

        /**
         * @see Geocoder.isPresent
         * @since 1.5.3
         */
        val isPresent: Boolean get() = Geocoder.isPresent()
    }

}