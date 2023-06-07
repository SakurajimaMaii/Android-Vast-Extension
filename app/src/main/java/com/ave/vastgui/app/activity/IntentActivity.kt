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

package com.ave.vastgui.app.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.databinding.ActivityIntentBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.IntentUtils
import com.ave.vastgui.tools.utils.IntentUtils.createAlarm
import com.ave.vastgui.tools.utils.IntentUtils.dialPhoneNumber
import com.ave.vastgui.tools.utils.IntentUtils.openEmail
import com.ave.vastgui.tools.utils.IntentUtils.openWebPage
import com.ave.vastgui.tools.utils.IntentUtils.openWirelessSettings
import com.ave.vastgui.tools.utils.IntentUtils.searchWeb
import com.ave.vastgui.tools.utils.IntentUtils.sendMmsMessage

class IntentActivity : VastVbActivity<ActivityIntentBinding>() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
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

        getBinding().createAlarm.setOnClickListener {
            val alarmConfig = IntentUtils.AlarmConfig()
                .setMsg("你好")
                .setHour(12)
                .setMinutes(30)
            createAlarm(this, alarmConfig)
        }

        getBinding().wifiSetting.setOnClickListener {
            openWirelessSettings(this)
        }
    }
}