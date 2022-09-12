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

package com.gcode.vastutils.activity.baseadpexample

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcode.vastadapter.adapter.VastAdapter
import com.gcode.vastadapter.interfaces.VAapClickEventListener
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener
import com.gcode.vastadapter.interfaces.VastAdapterItem
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.ToastUtils.showShortMsg
import com.gcode.vastutils.R
import com.gcode.vastutils.activity.baseadpexample.model.AExample
import com.gcode.vastutils.activity.baseadpexample.model.BExample
import com.gcode.vastutils.activity.baseadpexample.viewholder.AViewHolder
import com.gcode.vastutils.activity.baseadpexample.viewholder.BViewHolder
import com.gcode.vastutils.databinding.ActivityBaseAdapterBinding

class BaseAdapterActivity : VastVbActivity<ActivityBaseAdapterBinding>() {

    private lateinit var adapter: BaseAdapter

    private val datas: MutableList<VastAdapterItem> = ArrayList()

    private val click = object : VAapClickEventListener {
        override fun vAapClickEvent(view: View, pos: Int) {
            showShortMsg(this@BaseAdapterActivity,"Click event and pos is $pos.")
        }
    }

    private val longClick = object : VAdpLongClickEventListener {
        override fun vAdpLongClickEvent(view: View, pos: Int): Boolean {
            showShortMsg(this@BaseAdapterActivity,"Long click event and pos is $pos.")
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        adapter = BaseAdapter(datas, mutableListOf(AViewHolder.Factory(), BViewHolder.Factory()))
        adapter.setOnItemClickListener(object : VastAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // Something you want to do
            }
        })
        adapter.setOnItemLongClickListener(object : VastAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int): Boolean {
                // Something you want to do
                return true
            }
        })
        mBinding.dataList.adapter = adapter
        mBinding.dataList.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        for (i in 0..10) {
            datas.add(AExample(i.toString(), click, null))
            datas.add(BExample(R.drawable.ic_knots, null, longClick))
        }
    }
}