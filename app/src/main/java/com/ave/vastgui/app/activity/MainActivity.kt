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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.VastBindAdapter
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.adpexample.AdapterActivity
import com.ave.vastgui.app.activity.adpexample.BindAdapterActivity
import com.ave.vastgui.app.activity.viewexample.ArcProgressViewActivity
import com.ave.vastgui.app.activity.viewexample.RatingActivity
import com.ave.vastgui.app.activity.viewexample.Vp2IndicatorActivity
import com.ave.vastgui.app.databinding.ActivityMainBinding
import com.ave.vastgui.app.model.IntentSelect
import com.ave.vastgui.app.model.IntentSelectWrapper
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.ResUtils

class MainActivity : VastVbActivity<ActivityMainBinding>() {

    private lateinit var mSplashScreen: SplashScreen

    // 列表rv适配器
    inner class Adapter(
        data: MutableList<AdapterItemWrapper<*>>,
        context: Context
    ) : VastBindAdapter(data, context) {
        override fun setVariableId(): Int {
            return BR.intentSelect
        }
    }

    // 列表数据源
    private val data: MutableList<AdapterItemWrapper<*>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        mSplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        initData()
        getBinding().rv.apply {
            adapter = Adapter(data, context)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initData() {
        data.apply {
            add(IntentSelectWrapper(
                IntentSelect(ResUtils.getString(R.string.loading_page)),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                NetStateActivity::class.java
                            )
                        )
                        getSnackbar().setText(ResUtils.getString(R.string.loading_page)).show()
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(ResUtils.getString(R.string.base_adapter)),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                AdapterActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(
                ResUtils.getString(R.string.base_bind_adapter)
            ),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                BindAdapterActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(
                ResUtils.getString(R.string.base_intent)
            ),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                IntentActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(
                ResUtils.getString(R.string.shape)
            ),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                ShapeActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(
                ResUtils.getString(R.string.base_fragment_activity)
            ),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                Vp2IndicatorActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(
                ResUtils.getString(R.string.download)
            ),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                ArcProgressViewActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(ResUtils.getString(R.string.file)),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                FileActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(ResUtils.getString(R.string.date)),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                DateActivity::class.java
                            )
                        )
                    }
                }
            ))

            add(IntentSelectWrapper(IntentSelect(ResUtils.getString(R.string.ratingView)),
                object : AdapterClickListener {
                    override fun onItemClick(view: View, pos: Int) {
                        getContext().startActivity(
                            Intent(
                                getContext(),
                                RatingActivity::class.java
                            )
                        )
                    }
                }
            ))
        }
    }

}