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

package cn.govast.vastutils.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import cn.govast.vasttools.activity.VastVbVmActivity
import cn.govast.vasttools.utils.IntentUtils.createAlarm
import cn.govast.vasttools.utils.IntentUtils.createOnceAlarm
import cn.govast.vasttools.utils.IntentUtils.dialPhoneNumber
import cn.govast.vasttools.utils.IntentUtils.openEmail
import cn.govast.vasttools.utils.IntentUtils.openWebPage
import cn.govast.vasttools.utils.IntentUtils.openWirelessSettings
import cn.govast.vasttools.utils.IntentUtils.searchWeb
import cn.govast.vasttools.utils.IntentUtils.sendMmsMessage
import cn.govast.vasttools.utils.LogUtils
import cn.govast.vasttools.utils.shortcut.AppShortcutsUtils
import cn.govast.vastutils.R
import cn.govast.vastutils.broadcastreceiver.ShortcutReceiver
import cn.govast.vastutils.broadcastreceiver.ShortcutReceiver.Companion.CREATE_SHORT_CUT
import cn.govast.vastutils.databinding.ActivityIntentBinding
import cn.govast.vastutils.network.NetworkRepository
import cn.govast.vastutils.viewModel.BasicViewModel

class IntentActivity : VastVbVmActivity<ActivityIntentBinding, BasicViewModel>() {

    private lateinit var shortcutReceiver: ShortcutReceiver

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shortcutReceiver = ShortcutReceiver()
        registerReceiver(shortcutReceiver, IntentFilter().apply {
            addAction(CREATE_SHORT_CUT)
        })

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

        getBinding().createShortcut.setOnClickListener {
            AppShortcutsUtils.setPinnedShortcutResult {
                return@setPinnedShortcutResult PendingIntent.getBroadcast(
                    this@IntentActivity.getContext(),
                    0,
                    Intent(CREATE_SHORT_CUT),
                    PendingIntent.FLAG_IMMUTABLE
                )
            }.createPinnedShortcut(
                MainActivity::class.java,
                "987654321",
                R.mipmap.ic_launcher,
                "测试快捷方式",
                this@IntentActivity.getContext()
            )
        }

        getBinding().getQRCode.setOnClickListener {
            getRequestBuilder()
                .suspendWithListener({ getViewModel().getQRCode() }) {
                    onSuccess = { QRCodeKey ->
                        LogUtils.i(getDefaultTag(), QRCodeKey.data.unikey)
                    }
                }
            getViewModel().searchSong("海阔天空")
        }

        getViewModel().songResult.getState().observeState(this) {
        }

        getViewModel().songResult.observe(this) {
            LogUtils.d(getDefaultTag(), it.result.songCount.toString())
        }
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return BasicViewModel(NetworkRepository())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(shortcutReceiver)
    }
}