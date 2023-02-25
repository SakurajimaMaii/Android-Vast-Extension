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
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.app.R
import com.ave.vastgui.app.broadcastreceiver.ShortcutReceiver
import com.ave.vastgui.app.broadcastreceiver.ShortcutReceiver.Companion.CREATE_SHORT_CUT
import com.ave.vastgui.app.databinding.ActivityIntentBinding
import com.ave.vastgui.app.network.NetworkRepository
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.tools.network.response.ResponseBuilder
import com.ave.vastgui.tools.utils.IntentUtils
import com.ave.vastgui.tools.utils.IntentUtils.createAlarm
import com.ave.vastgui.tools.utils.IntentUtils.dialPhoneNumber
import com.ave.vastgui.tools.utils.IntentUtils.openEmail
import com.ave.vastgui.tools.utils.IntentUtils.openWebPage
import com.ave.vastgui.tools.utils.IntentUtils.openWirelessSettings
import com.ave.vastgui.tools.utils.IntentUtils.searchWeb
import com.ave.vastgui.tools.utils.IntentUtils.sendMmsMessage
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.utils.shortcut.AppShortcutsUtils

class IntentActivity : BaseActivity<ActivityIntentBinding, NetVM>() {

    private lateinit var shortcutReceiver: ShortcutReceiver

    override fun getViewModel(): NetVM {
        return NetVM()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shortcutReceiver = ShortcutReceiver()
        registerReceiver(shortcutReceiver, IntentFilter().apply {
            addAction(CREATE_SHORT_CUT)
        })

        mBinding.callBtn.setOnClickListener {
            dialPhoneNumber(this, "12345678910")
        }

        mBinding.searchWeb.setOnClickListener {
            searchWeb(this, "12345678910")
        }

        mBinding.openWebPage.setOnClickListener {
            openWebPage(this, "http://www.baidu.com")
        }

        mBinding.sendMmsMessage.setOnClickListener {
            sendMmsMessage(this, "123456", "1238489")
        }

        mBinding.sendEmail.setOnClickListener {
            openEmail(this, arrayOf("12345678910@qq.com"))
        }

        mBinding.createAlarm.setOnClickListener {
            val alarmConfig = IntentUtils.AlarmConfig()
                .setMsg("你好")
                .setHour(12)
                .setMinutes(30)
            createAlarm(this, alarmConfig)
        }

        mBinding.wifiSetting.setOnClickListener {
            openWirelessSettings(this)
        }

        mBinding.createShortcut.setOnClickListener {
            AppShortcutsUtils.setPinnedShortcutResult {
                return@setPinnedShortcutResult PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(CREATE_SHORT_CUT),
                    PendingIntent.FLAG_IMMUTABLE
                )
            }.createPinnedShortcut(
                MainActivity::class.java,
                "987654321",
                R.mipmap.ic_launcher,
                "测试快捷方式",
                this
            )
        }

        mBinding.getQRCode.setOnClickListener {
            ResponseBuilder(lifecycleScope)
                .suspendWithListener({ NetworkRepository().getQRCode() }) {
                    onSuccess = { qRCodeKey ->
                        LogUtils.i(getDefaultTag(), qRCodeKey.data?.unikey)
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(shortcutReceiver)
    }

    private fun getDefaultTag(): String = this.javaClass.simpleName
}