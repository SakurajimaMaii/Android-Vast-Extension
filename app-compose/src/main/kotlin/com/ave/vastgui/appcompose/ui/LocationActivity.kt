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

package com.ave.vastgui.appcompose.ui

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.location.GeocodeListenerCompat
import com.ave.vastgui.tools.location.GeocoderCompat
import com.ave.vastgui.tools.location.LocationClient
import com.ave.vastgui.tools.location.LocationResultListener
import org.slf4j.LoggerFactory
import kotlin.properties.Delegates

class LocationActivity : ComponentActivity() {

    private val logger = LoggerFactory.getLogger("LocationActivity")

    private var locationClient: LocationClient by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        locationClient = LocationClient.getInstance(this)
        setContent {
            AndroidVastExtensionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        // logger.debug("The result location is {}", LocationClient.getInstance(this).tryGetLastKnownLocation())
        locationClient.startLocation(LocationManager.NETWORK_PROVIDER, object : LocationResultListener {
            override fun onLocationChanged(location: Location) {
                logger.debug("The current location is Lng:${location.longitude} Lat:${location.latitude}")
                GeocoderCompat(this@LocationActivity)
                    .getFromLocation(location.latitude, location.longitude, 5, GeocodeListener())
            }

            override fun onLocationFailure(ex: Exception) {
                logger.error(ex.message)
            }
        })
    }

    private inner class GeocodeListener : GeocodeListenerCompat {
        override fun onGeocode(addresses: List<Address>) {
            logger.debug("The result of geocoder is ${addresses.joinToString(",")}")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}