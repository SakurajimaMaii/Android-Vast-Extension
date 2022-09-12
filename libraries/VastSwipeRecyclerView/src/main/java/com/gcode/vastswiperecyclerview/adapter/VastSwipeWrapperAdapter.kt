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

package com.gcode.vastswiperecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gcode.vastswiperecyclerview.R
import com.gcode.vastswiperecyclerview.VastSwipeRvMgr
import com.gcode.vastswiperecyclerview.model.VastSwipeMenu
import com.gcode.vastswiperecyclerview.view.VastSwipeMenuLayout
import com.gcode.vastswiperecyclerview.view.VastSwipeMenuView
import java.lang.reflect.Field

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/6/14
// Description:
// Documentation:

internal class VastSwipeWrapperAdapter(
    private val manager: VastSwipeRvMgr,
    private val mAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val viewHolder = mAdapter.onCreateViewHolder(parent, viewType)

        if(null != manager.mSwipeItemClickListener){
            viewHolder.itemView.setOnClickListener {
                manager.mSwipeItemClickListener!!.onClickEvent(it,viewHolder.bindingAdapterPosition)
            }
        }

        if(null != manager.mSwipeItemLongClickListener){
            viewHolder.itemView.setOnLongClickListener {
                manager.mSwipeItemLongClickListener!!.onLongClickEvent(it,viewHolder.bindingAdapterPosition)
            }
        }

        val contentView = LayoutInflater.from(parent.context).inflate(R.layout.item_vast_swipe_menu, parent,false)
        val viewGroup = contentView.findViewById<FrameLayout>(R.id.swipe_content)
        viewGroup.addView(viewHolder.itemView)

        try {
            val itemView: Field = getSuperClass(viewHolder.javaClass).getDeclaredField("itemView")
            if (!itemView.isAccessible) itemView.isAccessible = true
            itemView[viewHolder] = contentView
        } catch (ignored: Exception) {

        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val itemView = holder.itemView as VastSwipeMenuLayout
        val leftMenuList:MutableList<VastSwipeMenu> = ArrayList()
        val rightMenuList:MutableList<VastSwipeMenu> = ArrayList()
        manager.mSwipeMenuCreator?.onCreateMenu(leftMenuList, rightMenuList, position)
        (itemView.getChildAt(0) as VastSwipeMenuView).apply {
            setManager(manager)
            setPosition(position)
            setMenu(leftMenuList)
        }
        (itemView.getChildAt(2) as VastSwipeMenuView).apply {
            setManager(manager)
            setPosition(position)
            setMenu(rightMenuList)
        }

        mAdapter.onBindViewHolder(holder,position)

    }

    override fun getItemCount() = mAdapter.itemCount

    override fun getItemViewType(position: Int) = mAdapter.getItemViewType(position)

    private fun getSuperClass(aClass: Class<*>): Class<*> {
        val superClass = aClass.superclass
        return if (superClass != null && superClass != Any::class.java) {
            getSuperClass(superClass)
        } else aClass
    }

}