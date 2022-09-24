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

package cn.govast.vastutils.activity.baselvadpexample

import android.os.Bundle
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vastutils.activity.baselvadpexample.model.LVIA
import cn.govast.vastutils.activity.baselvadpexample.model.LVIB
import cn.govast.vastutils.activity.baselvadpexample.viewHolder.LVIAVH
import cn.govast.vastutils.activity.baselvadpexample.viewHolder.LVIBVH
import cn.govast.vastutils.databinding.ActivityListViewBinding
import cn.govast.vastutils.component.VastBaseAdapter

class ListViewActivity : VastVbActivity<ActivityListViewBinding>() {

    private val data:ArrayList<VastBaseAdapter.BaseItem> = ArrayList<VastBaseAdapter.BaseItem>().apply {
        for(i in 0..100){
            add(LVIA(i.toString()))
            add(LVIB(i.toString()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().dataList.adapter = ListViewAdapter(this,data, mutableListOf(
            LVIAVH.Factory(),
            LVIBVH.Factory()))
    }
}