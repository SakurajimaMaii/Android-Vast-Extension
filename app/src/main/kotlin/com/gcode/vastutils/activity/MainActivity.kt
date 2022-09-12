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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcode.vastadapter.adapter.VastBindAdapter
import com.gcode.vastadapter.interfaces.VAapClickEventListener
import com.gcode.vastadapter.interfaces.VastBindAdapterItem
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.ResUtils
import com.gcode.vastutils.BR
import com.gcode.vastutils.R
import com.gcode.vastutils.activity.baseadpexample.BaseAdapterActivity
import com.gcode.vastutils.activity.basebindadpexample.BaseBindingAdapterActivity
import com.gcode.vastutils.databinding.ActivityMainBinding
import com.gcode.vastutils.model.IntentSelect

class MainActivity : VastVbActivity<ActivityMainBinding>() {

    private lateinit var mSplashScreen: SplashScreen

    // 列表rv适配器
    inner class Adapter(
        data: MutableList<VastBindAdapterItem>,
        context: Context
    ) : VastBindAdapter(data, context) {
        override fun setVariableId(): Int {
            return BR.intentSelect
        }
    }

    // 列表数据源
    private val data: MutableList<VastBindAdapterItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableActionBar = false
        mSplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        initData()
        mBinding.rv.apply {
            adapter = Adapter(data, mContext)
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initData() {
        data.apply {
            add(IntentSelect(
                ResUtils.getString(R.string.loading_page),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, NetStateActivity::class.java))
                        mSnackbar.setText(ResUtils.getString(R.string.loading_page)).show()
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_adapter),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(
                            Intent(
                                mContext,
                                BaseAdapterActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_bind_adapter),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(
                            Intent(
                                mContext,
                                BaseBindingAdapterActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_intent),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, IntentActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.shape),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, ShapeActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_fragment_activity),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(
                            Intent(
                                mContext,
                                FragmentsActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.theme),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, ThemeActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.download),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, DownloadActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.file),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, FileActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.date),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        mContext.startActivity(Intent(mContext, DateActivity::class.java))
                    }
                }
            ))
        }
    }
}