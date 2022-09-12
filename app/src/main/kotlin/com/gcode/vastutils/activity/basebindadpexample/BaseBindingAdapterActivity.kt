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

package com.gcode.vastutils.activity.basebindadpexample

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gcode.vastadapter.adapter.VastBindAdapter
import com.gcode.vastadapter.interfaces.VAapClickEventListener
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener
import com.gcode.vastadapter.interfaces.VastBindAdapterItem
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.ToastUtils.showShortMsg
import com.gcode.vastutils.R
import com.gcode.vastutils.activity.basebindadpexample.model.Person
import com.gcode.vastutils.activity.basebindadpexample.model.Picture
import com.gcode.vastutils.databinding.ActivityBaseBindingAdapterBinding

class BaseBindingAdapterActivity : VastVbActivity<ActivityBaseBindingAdapterBinding>() {

    private val datas: MutableList<VastBindAdapterItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()

        val adapter = BaseBindingAdapter(datas, this)

        adapter.setOnItemClickListener(object : VastBindAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // Something you want to do
            }
        })
        adapter.setOnItemLongClickListener(object : VastBindAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int): Boolean {
                // Something you want to do
                return true
            }
        })


        mBinding.dataRv.adapter = adapter
        mBinding.dataRv.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {

        val click = object : VAapClickEventListener {
            override fun vAapClickEvent(view: View, pos: Int) {
                showShortMsg(this@BaseBindingAdapterActivity,"Click event and pos is $pos.")
            }
        }

        val longClick = object : VAdpLongClickEventListener {
            override fun vAdpLongClickEvent(view: View, pos: Int): Boolean {
                showShortMsg(this@BaseBindingAdapterActivity,"Long click event and pos is $pos.")
                return true
            }
        }

        for (i in 0..10) {
            datas.add(Person(i.toString(), i.toString(), click, null))
            datas.add(Picture(R.drawable.ic_knots, null, longClick))
        }
    }
}