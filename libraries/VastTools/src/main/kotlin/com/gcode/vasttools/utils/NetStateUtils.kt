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
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/2 9:03
// Description: With NetStateUtils, you can easily check some network status about your device
// Documentation: [NetStateUtils](https://sakurajimamaii.github.io/VastDocs/document/en/NetStateUtils.html)

object NetStateUtils {
    /**
     * Get [NetworkInfo]
     *
     * @param context context.
     * @return [NetworkInfo] object.
     * @since 0.0.6
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    internal fun getNetWorkInfo(context: Context): NetworkInfo? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetworkInfo
        } else {
            throw RuntimeException("NetworkInfo was deprecated in API level 29.")
        }
    }

    /**
     * Get [NetworkCapabilities]
     *
     * @param context context.
     * @return [NetworkCapabilities] object.
     * @since 0.0.6
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    @Throws(RuntimeException::class)
    internal fun getNetworkCapabilities(context: Context): NetworkCapabilities? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nw = cm.activeNetwork
            cm.getNetworkCapabilities(nw)
        } else {
            throw RuntimeException("App api version should be greater than 29.")
        }
    }

    /**
     * Is network available
     *
     * @param context context.
     * @return true if network is available,false otherwise.
     * @since 0.0.6
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context)
            if ((null != networkInfo) and (networkInfo!!.isConnected)) {
                networkInfo.isAvailable
            } else false
        } else {
            val networkCapabilities = getNetworkCapabilities(context)
            if (null != networkCapabilities) {
                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else false
        }
    }

    /**
     * Is WIFI
     *
     * @param context context.
     * @return true if network is wifi mode,false otherwise.
     * @since 0.0.6
     */
    @JvmStatic
    fun isWIFI(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context)
            if (null != networkInfo) {
                networkInfo.type == ConnectivityManager.TYPE_WIFI
            } else false
        } else {
            val networkCapabilities = getNetworkCapabilities(context)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        }
    }

    /**
     * Is mobile net
     *
     * @param context context.
     * @return true if network is wifi mode,false otherwise.
     * @since 0.0.6
     */
    @JvmStatic
    fun isMobile(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context)
            if (null != networkInfo) {
                networkInfo.type == ConnectivityManager.TYPE_MOBILE
            } else false
        } else {
            val networkCapabilities = getNetworkCapabilities(context)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        }
    }

    /**
     * Get wifi signal strength.
     *
     * @param context context.
     *  -1 when wifi is disconnected or unable,when wifi is connected,
     *     the signal strength is represented by 0-4.
     * @since 0.0.6
     */
    @JvmStatic
    fun getWifiDBM(context: Context): Int {
        if (!isWIFI(context)) return -1
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val wifiManager =
                context.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
            val info = wifiManager.connectionInfo
            if (info.bssid != null) {
                // Signal strength, 5 means the acquired signal strength value is within 5
                return WifiManager.calculateSignalLevel(info.rssi, 5)
            }
        } else {
            val info = getNetworkCapabilities(context)
            val wm =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (null != info) {
                return wm.calculateSignalLevel(info.signalStrength)
            }
        }
        return -1
    }
}