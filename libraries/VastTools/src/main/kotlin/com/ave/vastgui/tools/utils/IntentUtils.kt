/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

import android.Manifest
import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.AlarmClock
import android.provider.Settings
import android.util.Log
import androidx.annotation.IntRange
import androidx.annotation.RequiresPermission
import com.ave.vastgui.tools.view.toast.SimpleToast

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Provides you for some common Intent.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/intent/IntentUtils/

object IntentUtils {

    /** The log tag. */
    private const val tag = "IntentUtils"

    /**
     * Dial phone number.
     *
     * This method does not verify the phone number. If you want to know if
     * your mobile phone number meets the rules,you can click this link
     * [Rules of International Mobile Phone Numbers](https://support.huaweicloud.com/intl/en-us/productdesc-msgsms/phone_numbers.html).
     *
     * @param context context.
     * @param phoneNumber phone number you want to call.
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startIntent(context, intent)
    }

    /**
     * Search [query] by webView
     *
     * @param context context.
     * @param query Content you want to search.
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.INTERNET)
    fun searchWeb(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
        startIntent(context, intent)
    }


    /**
     * Open [url] by WebView
     *
     * @param context context.
     * @param url Url you want to open.
     */
    @JvmStatic
    fun openWebPage(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startIntent(context, intent)
    }

    /**
     * Send message only by SMS app (not other email or social apps)
     *
     * @param context context.
     * @param message What you want to send.
     * @param phoneNumber Who you want to send.Default value is `null`.
     * @param attachment Point to the Uri of the image or video to be attached.
     *     If you are using the ACTION_SEND_MULTIPLE operation, this extra
     *     should be an ArrayList pointing to the image/video Uri to be
     *     attached.And default value is `null`.
     */
    @JvmStatic
    @JvmOverloads
    fun sendMmsMessage(
        context: Context,
        message: String = "",
        phoneNumber: String? = null,
        attachment: Uri? = null
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${phoneNumber}")  // This ensures only SMS apps respond
            putExtra("sms_body", message)
            if (attachment != null) {
                putExtra(Intent.EXTRA_STREAM, attachment)
            }
        }
        startIntent(context, intent)
    }


    /**
     * Open email only by email apps (not other SMS or social apps)
     *
     * @param context context.
     * @param addresses A string array containing all the email addresses of
     *     the recipients of the "primary sender".
     * @param subject Subject of the email.Default value is "".
     * @param text Text of the email.Default value is "".
     */
    @JvmStatic
    @JvmOverloads
    fun openEmail(
        context: Context,
        addresses: Array<String>,
        subject: String = "",
        text: String = ""
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startIntent(context, intent)
    }

    /**
     * Create alarm.
     *
     * @param alarmConfig The alarm config, please refer to [AlarmConfig].
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.SET_ALARM)
    @JvmOverloads
    fun createAlarm(
        context: Context,
        alarmConfig: AlarmConfig = AlarmConfig()
    ) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, alarmConfig.message)
            putExtra(AlarmClock.EXTRA_HOUR, alarmConfig.hour)
            putExtra(AlarmClock.EXTRA_MINUTES, alarmConfig.minutes)
            putExtra(AlarmClock.EXTRA_VIBRATE, alarmConfig.vibrate)
            putExtra(AlarmClock.EXTRA_SKIP_UI, alarmConfig.skipUI)
            putExtra(AlarmClock.EXTRA_DAYS, alarmConfig.days)
            putExtra(AlarmClock.EXTRA_RINGTONE, alarmConfig.music)
        }
        startIntent(context, intent)
    }

    /**
     * Open wifi settings.
     *
     * @param context context.
     */
    @JvmStatic
    fun openWirelessSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        startIntent(context, intent)
    }

    /**
     * Open Application details settings.
     *
     * @param context context.
     */
    @JvmStatic
    fun openApplicationDetailsSettings(context: Context) {
        val intent = Intent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            data = Uri.fromParts("package", context.packageName, null)
        }
        startIntent(context, intent)
    }

    /** Start intent. */
    private fun startIntent(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e(tag, "Target activity not found.", ex)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e(
                    tag,
                    "Maybe you don't adding a <queries> declaration in AndroidManifest.xml."
                )
            }
        } catch (ex: SecurityException) {
            SimpleToast.showShortMsg(ex.message.toString())
            ex.printStackTrace()
        }
    }

    /**
     * AlarmConfig
     *
     * @property message A custom message for the alarm or timer.
     * @property hour The hour of the alarm.
     * @property minutes The minutes of the alarm.
     * @property vibrate Whether or not to activate the device vibrator.
     *     Default value is `false`.
     * @property skipUI Whether or not to display an activity after performing
     *     the action. Default value is `false`.
     * @property days Weekdays for repeating alarm. The value is an
     *     `Array<Int>`. Each item can be: [Calendar.SUNDAY] [Calendar.MONDAY]
     *     [Calendar.TUESDAY] [Calendar.WEDNESDAY] [Calendar.THURSDAY]
     *     [Calendar.FRIDAY] [Calendar.SATURDAY] The [days] default is `null`.
     * @property music A ringtone to be played with this alarm. Default value
     *     is `null`.
     */
    class AlarmConfig {
        internal var message: String = ""

        @IntRange(from = 0, to = 23)
        internal var hour: Int = 0

        @IntRange(from = 0, to = 59)
        internal var minutes: Int = 0
        internal var vibrate: Boolean = false
        internal var skipUI: Boolean = false
        internal var music: Uri? = null
        internal var days: Array<Int>? = null

        /** Set [AlarmConfig.message] */
        fun setMsg(message: String) = apply {
            this.message = message
        }

        /** Set [AlarmConfig.hour] */
        fun setHour(@IntRange(from = 0, to = 23) hour: Int) = apply {
            this.hour = hour
        }

        /** Set [AlarmConfig.minutes] */
        fun setMinutes(@IntRange(from = 0, to = 59) minutes: Int) = apply {
            this.minutes = minutes
        }

        /** Set [AlarmConfig.vibrate] */
        fun setVibrate(vibrate: Boolean) = apply {
            this.vibrate = vibrate
        }

        /** Set [AlarmConfig.skipUI] */
        fun setSkipUI(skipUI: Boolean) = apply {
            this.skipUI = skipUI
        }

        /** Set [AlarmConfig.music] */
        fun setMusic(music: Uri?) = apply {
            this.music = music
        }

        /** Set [AlarmConfig.days] */
        fun setDays(days: Array<Int>?) = apply {
            this.days = days
        }
    }
}