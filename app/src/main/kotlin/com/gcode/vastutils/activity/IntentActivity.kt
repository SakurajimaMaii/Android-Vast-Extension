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

package com.gcode.vastutils.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.IntentUtils.createAlarm
import com.gcode.vasttools.utils.IntentUtils.createOnceAlarm
import com.gcode.vasttools.utils.IntentUtils.dialPhoneNumber
import com.gcode.vasttools.utils.IntentUtils.openEmail
import com.gcode.vasttools.utils.IntentUtils.openWebPage
import com.gcode.vasttools.utils.IntentUtils.openWirelessSettings
import com.gcode.vasttools.utils.IntentUtils.searchWeb
import com.gcode.vasttools.utils.IntentUtils.sendMmsMessage
import com.gcode.vastutils.databinding.ActivityIntentBinding

class IntentActivity : VastVbActivity<ActivityIntentBinding>() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().callBtn.setOnClickListener {
            dialPhoneNumber(this, "12345678910")
        }

        getBinding().searchWeb.setOnClickListener {
            searchWeb(this, "12345678910")
        }

        getBinding().openWebPage.setOnClickListener {
            openWebPage(this, "http://www.baidu.com")
        }

        getBinding().sendMmsMessage.setOnClickListener {
            sendMmsMessage(this, "123456", "1238489")
        }

        getBinding().sendEmail.setOnClickListener {
            openEmail(this, arrayOf("12345678910@qq.com"))
        }

        getBinding().createOnceAlarm.setOnClickListener {
            createOnceAlarm(this, "你好", 12, 30)
        }

        getBinding().createAlarm.setOnClickListener {
            createAlarm(this, "你好", 12, 30)
        }

        getBinding().wifiSetting.setOnClickListener {
            openWirelessSettings(this)
        }
    }
}