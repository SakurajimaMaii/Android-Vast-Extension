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

package cn.govast.vasttools.utils

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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Provides you for some common Intent.
// Documentation: [IntentUtils](https://sakurajimamaii.github.io/VastDocs/document/en/IntentUtils.html)

object IntentUtils {

    /**
     * The log tag.
     */
    private const val tag = "IntentUtils"

    /**
     * Dial phone number.
     *
     * This method does not verify the phone number.
     * If you want to know if your mobile phone
     * number meets the rules,you can click this link
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
     * @param attachment Point to the Uri of the image or video to be
     *     attached. If you are using the ACTION_SEND_MULTIPLE
     *     operation, this extra should be an ArrayList pointing to the
     *     image/video Uri to be attached.And default value is `null`.
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
     * @param addresses A string array containing all the email
     *     addresses of the recipients of the "primary sender".
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
     * Create once alarm.If any param perplexes you,please see
     * [createAlarm].
     *
     * @param context context.
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.SET_ALARM)
    @JvmOverloads
    fun createOnceAlarm(
        context: Context,
        message: String,
        @IntRange(from = 0, to = 23) hour: Int,
        @IntRange(from = 0, to = 59) minutes: Int,
        vibrate: Boolean = false,
        skipUI: Boolean = false,
        music: Uri? = null
    ) {
        createAlarm(context, message, hour, minutes, vibrate, skipUI, music)
    }

    /**
     * Create alarm
     *
     * @param context context.
     * @param message A custom message for the alarm or timer.
     * @param hour The hour of the alarm.
     * @param minutes The minutes of the alarm.
     * @param vibrate Whether or not to activate the device vibrator.
     *     Default value is `false`.
     * @param skipUI Whether or not to display an activity after
     *     performing the action. Default value is `false`.
     * @param days Weekdays for repeating alarm. The value is an
     *     `Array<Int>`. Each item can be: [Calendar.SUNDAY]
     *     [Calendar.MONDAY] [Calendar.TUESDAY] [Calendar.WEDNESDAY]
     *     [Calendar.THURSDAY] [Calendar.FRIDAY]
     *     [Calendar.SATURDAY] The [days] default is `null`.
     * @param music A ringtone to be played with this alarm. Default
     *     value is `null`.
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.SET_ALARM)
    @JvmOverloads
    fun createAlarm(
        context: Context,
        message: String,
        @IntRange(from = 0, to = 23) hour: Int,
        @IntRange(from = 0, to = 59) minutes: Int,
        vibrate: Boolean = false,
        skipUI: Boolean = false,
        music: Uri? = null,
        days: Array<Int>? = null,
    ) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
            putExtra(AlarmClock.EXTRA_VIBRATE, vibrate)
            putExtra(AlarmClock.EXTRA_SKIP_UI, skipUI)
            putExtra(AlarmClock.EXTRA_DAYS, days)
            putExtra(AlarmClock.EXTRA_RINGTONE, music)
        }
        startIntent(context, intent)
    }

    /**
     * Open wifi settings.
     *
     * @param context context.
     * @since 0.0.6
     */
    @JvmStatic
    fun openWirelessSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        startIntent(context, intent)
    }

    /**
     * Start intent.
     */
    private fun startIntent(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e(tag, "Target activity not found.", ex)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e(tag, "Maybe you don't adding a <queries> declaration in AndroidManifest.xml.")
            }
        }
    }
}