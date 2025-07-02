/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.view.rvdecoration

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.BaseAdapter
import com.ave.vastgui.adapter.base.ItemHolder
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityRecyclerviewDecorationBinding
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.recyclerview.decoration.GridSpacingDecoration
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.properties.Delegates

class RecyclerViewDecorationActivity : ComponentActivity(R.layout.activity_recyclerview_decoration) {

    private val binding by viewBinding(ActivityRecyclerviewDecorationBinding::bind)

    private var adapter1 by Delegates.notNull<SimpleAdapter1>()

    private var adapter2 by Delegates.notNull<SimpleAdapter2>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rv1.adapter = SimpleAdapter1(this).also { adapter1 = it }
        binding.rv1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter1.add(listOf("你好", "这是一段话"), android.R.layout.simple_list_item_1)

        binding.rv2.adapter = SimpleAdapter2(this).also { adapter2 = it }
        binding.rv2.layoutManager = GridLayoutManager(this, 3)
        binding.rv2.addItemDecoration(GridSpacingDecoration(3, 10f.DP))
        adapter2.add(listOf("你好", "这是一段话", "示例内容", "随便写点"), android.R.layout.simple_list_item_1)
    }

    private class SimpleAdapter1(context: Context) :
        BaseAdapter<String>(context, mutableListOf(SimpleHolder.Companion)) {
        class SimpleHolder(itemView: View) : ItemHolder<String>(itemView) {
            private val textView: TextView = itemView.findViewById(android.R.id.text1)

            override fun onBindData(item: String) {
                textView.text = item
                textView.setBackgroundColor(Color.GRAY)
            }

            companion object : HolderFactory<String> {
                override fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemHolder<String> {
                    val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                    return SimpleHolder(view)
                }

                override val layoutId: Int = android.R.layout.simple_list_item_1
            }
        }
    }

    private class SimpleAdapter2(context: Context) :
        BaseAdapter<String>(context, mutableListOf(SimpleHolder.Companion)) {
        class SimpleHolder(itemView: View) : ItemHolder<String>(itemView) {
            private val textView: TextView = itemView.findViewById(android.R.id.text1)

            override fun onBindData(item: String) {
                textView.text = item
                textView.setBackgroundColor(ColorUtils.getColorIntWithTransparency(50, Color.RED))
            }

            companion object : HolderFactory<String> {
                override fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemHolder<String> {
                    val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                    return SimpleHolder(view)
                }

                override val layoutId: Int = android.R.layout.simple_list_item_1
            }
        }
    }

}