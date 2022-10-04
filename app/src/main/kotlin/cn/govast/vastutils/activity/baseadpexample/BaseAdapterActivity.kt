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
import cn.govast.vastadapter.adapter.VastAdapter
import cn.govast.vastadapter.interfaces.VAapClickEventListener
import cn.govast.vastadapter.interfaces.VAdpLongClickEventListener
import cn.govast.vastadapter.interfaces.VastAdapterItem
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

    private val datas: MutableList<VastAdapterItem> = ArrayList()

    private val click = object : VAapClickEventListener {
        override fun vAapClickEvent(view: View, pos: Int) {
            showShortMsg("Click event and pos is $pos.")
        }
    }

    private val longClick = object : VAdpLongClickEventListener {
        override fun vAdpLongClickEvent(view: View, pos: Int): Boolean {
            showShortMsg("Long click event and pos is $pos.")
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