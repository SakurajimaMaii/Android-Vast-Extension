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

package com.ave.vastgui.app.activity.adpexample

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterLongClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.adpexample.adapter.ListAdapterEx
import com.ave.vastgui.app.activity.adpexample.model.Picture
import com.ave.vastgui.app.activity.adpexample.model.PictureHolder
import com.ave.vastgui.app.activity.adpexample.model.PictureWrapper
import com.ave.vastgui.app.databinding.ActivityAdapterBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.ToastUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/1/21
// Description: 
// Documentation:
// Reference:

class ListAdapterActivity: VastVbActivity<ActivityAdapterBinding>() {

    private val datas: MutableList<PictureWrapper> = ArrayList()
    private val click = object : AdapterClickListener {
        override fun onItemClick(view: View, pos: Int) {
            ToastUtils.showShortMsg("Click event and pos is $pos.")
        }
    }

    private val longClick = object : AdapterLongClickListener {
        override fun onItemLongClick(view: View, pos: Int): Boolean {
            ToastUtils.showShortMsg("Long click event and pos is $pos.")
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        for (i in 0..100) {
            datas.add(PictureWrapper(Picture(R.drawable.ic_knots),click,longClick))
        }
        val adapter = ListAdapterEx(PictureHolder.Factory())
        adapter.submitList(datas)

        getBinding().dataList.adapter = adapter
        getBinding().dataList.layoutManager = LinearLayoutManager(this)
    }
}