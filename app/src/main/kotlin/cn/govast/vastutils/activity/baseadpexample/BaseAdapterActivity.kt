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

package cn.govast.vastutils.activity.baseadpexample

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.govast.vastadapter.AdapterClickListener
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.AdapterLongClickListener
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.ToastUtils.showShortMsg
import cn.govast.vastutils.R
import cn.govast.vastutils.activity.baseadpexample.model.AExample
import cn.govast.vastutils.activity.baseadpexample.model.BExample
import cn.govast.vastutils.activity.baseadpexample.viewholder.AViewHolder
import cn.govast.vastutils.activity.baseadpexample.viewholder.BViewHolder
import cn.govast.vastutils.databinding.ActivityBaseAdapterBinding

class BaseAdapterActivity : VastVbActivity<ActivityBaseAdapterBinding>() {

    private lateinit var adapter: BaseAdapter

    private val datas: MutableList<AdapterItem> = ArrayList()

    private val click = object : AdapterClickListener {
        override fun onItemClick(view: View, pos: Int) {
            showShortMsg("Click event and pos is $pos.")
        }
    }

    private val longClick = object : AdapterLongClickListener {
        override fun onItemLongClick(view: View, pos: Int): Boolean {
            showShortMsg("Long click event and pos is $pos.")
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        adapter = BaseAdapter(datas, mutableListOf(AViewHolder.Factory(), BViewHolder.Factory()))
        adapter.registerClickEvent(object : AdapterClickListener {
            override fun onItemClick(view: View, pos: Int) {
                // Something you want to do
            }
        })
        adapter.registerLongClickEvent(object : AdapterLongClickListener {
            override fun onItemLongClick(view: View, pos: Int): Boolean {
                // Something you want to do
                return true
            }
        })
        getBinding().dataList.adapter = adapter
        getBinding().dataList.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        for (i in 0..10) {
            datas.add(AExample(i.toString(), click, null))
            datas.add(BExample(R.drawable.ic_knots, null, longClick))
        }
    }
}