/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.utils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.os.extension.fromApi31
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/2 9:03
// Description: With NetStateUtils, you can easily check some network status about your device
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/net-state-utils/

object NetStateUtils {

    /**
     * Wifi DBM
     *
     * @since 0.5.3
     */
    sealed class WIFIDBM(val strength: Int) {
        object NOWIFICONNECT : WIFIDBM(-1)
        class CURRENTDBM(strength: Int) : WIFIDBM(strength)
        object UNSPECIFIED : WIFIDBM(-1)

        override fun toString(): String = "${this::class.java.simpleName} strength:$strength"
    }

    /**
     * Get [NetworkInfo].
     *
     * @throws SecurityException Throw exception if there is no permission for
     *     ACCESS_NETWORK_STATE.
     * @see ConnectivityManager.getActiveNetworkInfo
     */
    @JvmStatic
    @Suppress("deprecation")
    @kotlin.jvm.Throws(SecurityException::class)
    internal fun getNetWorkInfo(context: Context): NetworkInfo? {
        if (ContextCompat.checkSelfPermission(context, ACCESS_NETWORK_STATE) != PERMISSION_GRANTED) {
            throw SecurityException("Please apply the permission ACCESS_NETWORK_STATE.")
        }
        return context.getConnectivityManager().activeNetworkInfo
    }

    /**
     * Get [NetworkCapabilities].
     *
     * @throws SecurityException Throw exception if there is no permission for
     *     ACCESS_NETWORK_STATE.
     * @see [ConnectivityManager.getNetworkCapabilities]
     */
    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.Q)
    @kotlin.jvm.Throws(SecurityException::class)
    internal fun getNetworkCapabilities(context: Context): NetworkCapabilities? {
        if (ContextCompat.checkSelfPermission(context, ACCESS_NETWORK_STATE) != PERMISSION_GRANTED)
            throw SecurityException("Please apply the permission ACCESS_NETWORK_STATE.")
        return context.getConnectivityManager().let {
            it.getNetworkCapabilities(it.activeNetwork)
        }
    }


    /**
     * Is network available
     *
     * @return True if network is available,false otherwise.
     * @see getNetWorkInfo
     * @see getNetworkCapabilities
     */
    @JvmStatic
    @kotlin.jvm.Throws(RuntimeException::class, SecurityException::class)
    fun isNetworkAvailable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context) ?: return false
            @Suppress("deprecation")
            networkInfo.isConnected and networkInfo.isAvailable
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
     * @return true if network is wifi mode,false otherwise.
     * @see getNetWorkInfo
     * @see getNetworkCapabilities
     */
    @JvmStatic
    @kotlin.jvm.Throws(RuntimeException::class, SecurityException::class)
    fun isWIFI(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context) ?: return false
            @Suppress("deprecation")
            networkInfo.type == ConnectivityManager.TYPE_WIFI
        } else {
            val networkCapabilities = getNetworkCapabilities(context) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }

    /**
     * Is mobile net
     *
     * @param context context.
     * @return true if network is wifi mode,false otherwise.
     */
    @JvmStatic
    fun isMobile(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context) ?: return false
            @Suppress("deprecation")
            networkInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            val networkCapabilities = getNetworkCapabilities(context) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    /**
     * Is ether net
     *
     * @param context context.
     * @return true if network is ether net,false otherwise.
     * @since 1.2.1
     */
    @JvmStatic
    fun isEtherNet(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = getNetWorkInfo(context) ?: return false
            @Suppress("deprecation")
            networkInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            val networkCapabilities = getNetworkCapabilities(context) ?: return false
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    /**
     * Get wifi signal strength.
     *
     * Return [WIFIDBM.NOWIFICONNECT] when wifi is disconnected or unable or
     * return [WIFIDBM.UNSPECIFIED] when can't get the strength of wifi.
     *
     * When the [Build.VERSION.SDK_INT] is under [Build.VERSION_CODES.S],the
     * signal strength of [WIFIDBM.CURRENTDBM] is represented by
     * 0-4, otherwise the signal strength of [WIFIDBM.CURRENTDBM]
     * is represented by 0-[WifiManager.getMaxSignalLevel].
     *
     * @throws SecurityException Throw this exception if there is no
     *     ACCESS_WIFI_STATE permission.
     * @since 0.5.3
     */
    @JvmStatic
    @kotlin.jvm.Throws(RuntimeException::class, SecurityException::class)
    fun getWifiDBM(context: Context): WIFIDBM {
        if (!isWIFI(context)) return WIFIDBM.NOWIFICONNECT
        // See https://developer.android.com/reference/android/net/wifi/WifiManager#getConnectionInfo()
        // for the deprecate information.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(context, ACCESS_WIFI_STATE) != PERMISSION_GRANTED) {
                throw SecurityException("Please apply the permission ACCESS_WIFI_STATE.")
            }
            val wifiManager = context.getWifiManager()

            @Suppress("deprecation")
            val rssi = wifiManager.connectionInfo.rssi
            // Signal strength, 5 means the acquired signal strength value is within 5
            return WIFIDBM.CURRENTDBM(wifiManager.calculateSignalLevelImpl(rssi, 5))
        } else {
            val wifiManager = context.getWifiManager()
            val networkCapabilities = getNetworkCapabilities(context) ?: return WIFIDBM.UNSPECIFIED
            // Get wifi dbm when using vpn.
            // Must first check whether it is a VPN connection, otherwise an exception will
            // occur android.net.VpnTransportInfo cannot be cast to android.net.wifi.WifiInfo.
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                var rssi = 0
                val request = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()
                val cm = context.getConnectivityManager()
                val networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        rssi = (networkCapabilities.transportInfo as WifiInfo).rssi
                        cm.unregisterNetworkCallback(this)
                    }
                }
                cm.registerNetworkCallback(request, networkCallback)
                return WIFIDBM.CURRENTDBM(wifiManager.calculateSignalLevelImpl(rssi, 5))
            }
            // Get wifi dbm when using wifi.
            else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                val wifiInfo = networkCapabilities.transportInfo as WifiInfo
                return WIFIDBM.CURRENTDBM(wifiManager.calculateSignalLevelImpl(wifiInfo.rssi, 5))
            } else return WIFIDBM.UNSPECIFIED
        }
    }

    /**
     * Get device ip address.
     *
     * @since 1.2.1
     */
    fun getIpAddress(context: Context): String? {
        if (isMobile(context)) return getHostIp()
        else if (isWIFI(context)) {
            fromApi31 {
                val linkProperties = context.getConnectivityManager().let {
                    it.getLinkProperties(it.activeNetwork)
                } ?: return null
                linkProperties.linkAddresses.forEach {
                    if (!it.address.isLoopbackAddress && it.address is Inet4Address) {
                        return it.address.hostAddress
                    }
                }
                return null
            }
            @Suppress("deprecation")
            return intIP2StringIP(context.getWifiManager().connectionInfo.ipAddress)
        } else if (isEtherNet(context)) return getHostIp()
        else return null
    }

    /**
     * Convert ipv4 to int, for example, 255.255.255.255 will be converted to
     * -1
     *
     * @see intIP2StringIP
     * @since 1.2.1
     */
    @JvmStatic
    @kotlin.jvm.Throws(IllegalArgumentException::class)
    fun stringIP2IntIP(ipv4: String): Int {
        val patterns = ipv4.split(".").map { it.toInt() }
        if (!patterns.all { it in 0..255 }) {
            throw IllegalArgumentException("Illegal ipv4 address.")
        }
        var result = 0
        patterns.forEachIndexed { index, i ->
            result = result or (i shl 8 * index)
        }
        return result
    }

    /**
     * Convert int to ipv4, for example, -1 will be converted to
     * 255.255.255.255
     *
     * @see stringIP2IntIP
     * @since 1.2.1
     */
    @JvmStatic
    fun intIP2StringIP(ipv4: Int): String {
        return (((ipv4 and 0xFF).toString() + "." +
                ((ipv4 shr 8) and 0xFF)) + "." +
                ((ipv4 shr 16) and 0xFF)) + "." +
                (ipv4 shr 24 and 0xFF)
    }

    /**
     * Calculate signal level implementation. Click
     * [calculateSignalLevel](https://developer.android.com/reference/android/net/wifi/WifiManager#calculateSignalLevel(int,%20int))
     * to get the deprecate information
     *
     * @param rssi The power of the signal measured in RSSI.
     * @param numLevels Only useful when the [Build.VERSION.SDK_INT] is smaller
     *     than 30.
     * @since 0.5.3
     */
    private fun WifiManager.calculateSignalLevelImpl(rssi: Int, numLevels: Int) = when {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.R ->
            @Suppress("deprecation")
            WifiManager.calculateSignalLevel(rssi, numLevels)

        else ->
            calculateSignalLevel(rssi)
    }

    /**
     * Get [WifiManager].
     *
     * @since 0.5.3
     */
    private fun Context.getWifiManager(): WifiManager =
        applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    /**
     * Get [ConnectivityManager].
     *
     * @since 0.5.3
     */
    private fun Context.getConnectivityManager(): ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Get the host ip.
     *
     * @since 1.2.1
     */
    private fun getHostIp(): String? = runCatching {
        val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val networkInterface: NetworkInterface = en.nextElement()
            val enumIpAddr: Enumeration<InetAddress> = networkInterface.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return@runCatching inetAddress.getHostAddress()
                }
            }
        }
        null
    }.getOrNull()

}