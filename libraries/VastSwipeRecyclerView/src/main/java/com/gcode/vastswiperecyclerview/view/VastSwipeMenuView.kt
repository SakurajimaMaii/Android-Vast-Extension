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

package com.gcode.vastswiperecyclerview.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gcode.vastswiperecyclerview.VastSwipeRvMgr
import com.gcode.vastswiperecyclerview.annotation.ICON_TITLE
import com.gcode.vastswiperecyclerview.annotation.ONLY_ICON
import com.gcode.vastswiperecyclerview.annotation.ONLY_TITLE
import com.gcode.vastswiperecyclerview.model.VastSwipeMenu
import androidx.annotation.IntRange


// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/6/14
// Description: 
// Documentation:
class VastSwipeMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    /** See the definition of the [VastSwipeRvMgr]. */
    private lateinit var manager: VastSwipeRvMgr

    /** The swipe menu position in the list. */
    var position: Int = -1
        private set

    /** Set the position of the menu. */
    fun setPosition(@IntRange(from = 0) position: Int) {
        this.position = position
    }

    fun setMenu(menuList:MutableList<VastSwipeMenu>){
        removeAllViews()

        for(menu in menuList){
            addItem(menu)
        }
    }

    fun setManager(manager: VastSwipeRvMgr) {
        this.manager = manager
    }

    private fun createIcon(item: VastSwipeMenu): ImageView {
        return ImageView(context).apply {
            setImageDrawable(item.icon)
            layoutParams = LayoutParams(manager.iconSize.toInt(), manager.iconSize.toInt()).apply {
                setMargins(10,10,10,10)
            }
        }
    }

    private fun createTitle(item: VastSwipeMenu): TextView {
        return TextView(context).apply {
            text = item.title
            gravity = Gravity.CENTER
            val (size,unit) = manager.titleSize
            setTextSize(unit,size)
            layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    setMargins(10,10,10,10)
                }
            setTextColor(item.titleColor)
        }
    }

    private fun addItem(item: VastSwipeMenu) {
        val parent = LinearLayout(context)
        parent.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            minimumWidth = manager.swipeMenuWidth.toInt()
            gravity = Gravity.CENTER
            orientation = VERTICAL
            background = item.background
            setOnClickListener {
                item.clickListener?.onClickEvent(item, position)
            }
            setOnLongClickListener {
                return@setOnLongClickListener item.clickListener?.onLongClickEvent(
                    item,
                    position
                ) == true
            }
            when (manager.swipeMenuContentStyle) {
                ONLY_ICON -> {
                    if (item.icon != null) {
                        parent.addView(createIcon(item))
                    }
                }
                ONLY_TITLE -> {
                    if (!TextUtils.isEmpty(item.title)) {
                        parent.addView(createTitle(item))
                    }
                }
                ICON_TITLE -> {
                    if (item.icon != null) {
                        parent.addView(createIcon(item))
                    }

                    if (!TextUtils.isEmpty(item.title)) {
                        parent.addView(createTitle(item))
                    }
                }
            }
        }
        addView(parent)
    }

}