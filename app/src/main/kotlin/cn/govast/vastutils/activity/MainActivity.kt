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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cn.govast.vastadapter.adapter.VastBindAdapter
import cn.govast.vastadapter.interfaces.VAapClickEventListener
import cn.govast.vastadapter.interfaces.VastBindAdapterItem
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.AppUtils
import cn.govast.vasttools.utils.LogUtils
import cn.govast.vasttools.utils.ResUtils
import cn.govast.vastutils.R
import cn.govast.vastutils.activity.baseadpexample.BaseAdapterActivity
import cn.govast.vastutils.activity.basebindadpexample.BaseBindingAdapterActivity
import cn.govast.vastutils.databinding.ActivityMainBinding
import cn.govast.vastutils.model.IntentSelect
import cn.govast.vastutils.BR

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
        enableFullScreen(true)
        mSplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        initData()
        getBinding().rv.apply {
            adapter = Adapter(data, context)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        val (a,b,c) = AppUtils.getMemoryInfo()
        LogUtils.i(getDefaultTag(),"$a $b $c")
    }

    private fun initData() {
        data.apply {
            add(IntentSelect(
                ResUtils.getString(R.string.loading_page),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(Intent(getContext(), NetStateActivity::class.java))
                        getSnackbar().setText(ResUtils.getString(R.string.loading_page)).show()
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_adapter),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
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
                        getContext().startActivity(
                            Intent(
                                getContext(),
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
                        getContext().startActivity(Intent(getContext(), IntentActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.shape),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(Intent(getContext(), ShapeActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.base_fragment_activity),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
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
                        getContext().startActivity(Intent(getContext(), ThemeActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.download),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(Intent(getContext(), cn.govast.vastutils.activity.DownloadActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.file),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(Intent(getContext(), FileActivity::class.java))
                    }
                }
            ))

            add(IntentSelect(
                ResUtils.getString(R.string.date),
                object : VAapClickEventListener {
                    override fun vAapClickEvent(view: View, pos: Int) {
                        getContext().startActivity(Intent(getContext(), cn.govast.vastutils.activity.DateActivity::class.java))
                    }
                }
            ))
        }
    }
}