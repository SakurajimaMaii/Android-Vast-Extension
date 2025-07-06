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

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/6
// Documentation:

/**
 * Helper for accessing features in [Geocoder.GeocodeListener].
 *
 * @since 1.5.3
 */
@JvmDefaultWithCompatibility
interface GeocodeListenerCompat {
    /**
     * Invoked when geocoding completes successfully. May return an empty list.
     *
     * @since 1.5.3
     */
    fun onGeocode(addresses: List<Address>)

    /**
     * Invoked when geocoding fails, with a brief error message.
     *
     * @since 1.5.3
     */
    fun onError(errorMessage: String?) {}
}

/**
 * Convert [GeocodeListenerCompat] to [Geocoder.GeocodeListener].
 *
 * @since 1.5.3
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal fun GeocodeListenerCompat.toGeocodeListener() = object : Geocoder.GeocodeListener {
    override fun onGeocode(addresses: List<Address>) = this@toGeocodeListener.onGeocode(addresses)

    override fun onError(errorMessage: String?) = this@toGeocodeListener.onError(errorMessage)
}