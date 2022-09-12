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

package com.gcode.vastswiperecyclerview.model

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.gcode.vastswiperecyclerview.interfaces.VastSwipeMenuClickListener
import com.gcode.vastswiperecyclerview.R

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/6/14
// Description:
// Documentation:

/**
 * VastSwipeMenuItem
 *
 * @param title menu title
 * @param icon menu icon
 * @param background default background is grey
 * @param titleColor default color is black
 */
class VastSwipeMenu @JvmOverloads constructor(
    private val context: Context,
    title: String = context.resources.getString(R.string.default_slide_item_title),
    icon: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_null),
    background: Drawable? = ContextCompat.getDrawable(context, R.drawable.default_bk_menu_item),
    titleColor: Int = ContextCompat.getColor(context, R.color.default_menu_item_title_color),
    clickListener: VastSwipeMenuClickListener? = null
) {
    /**
     * Title
     */
    var title: String = title
        private set

    /**
     * Icon
     */
    var icon: Drawable? = icon
        private set

    /**
     * Background,default background is **grey**.
     */
    var background: Drawable? = background
        private set

    /**
     * Title color,default color is **black**.
     */
    var titleColor = titleColor
        private set

    var clickListener:VastSwipeMenuClickListener? = null
        private set

    /**
     * Set menu title by [title].
     */
    fun setTitleByString(title: String){
        this.title = title
    }

    /**
     * Set menu title by [titleRes].
     */
    fun setTitleByResId(@StringRes titleRes: Int){
        title = context.resources.getString(titleRes)
    }

    /**
     * Set menu icon by [icon].
     */
    fun setIconByDrawable(icon: Drawable){
        this.icon = icon
    }

    /**
     * Set menu icon by [iconResId].
     */
    fun setIconByResId(@DrawableRes iconResId:Int){
        icon = ContextCompat.getDrawable(context, iconResId)
    }

    /**
     * Set menu background by [background].
     */
    fun setBackgroundByDrawable(background: Drawable){
        this.background = background
    }

    /**
     * Set background by [backgroundResId].
     */
    fun setBackgroundByResId(@DrawableRes backgroundResId:Int){
        background = ContextCompat.getDrawable(context, backgroundResId)
    }

    /**
     * Set solid color by [colorInt]
     */
    fun setBackgroundByColorInt(@ColorInt colorInt: Long){
        background = ColorDrawable(colorInt.toInt())
    }

    /**
     * Set titleColor by [colorResId].
     */
    fun setTitleColorByResId(@ColorRes colorResId:Int){
        titleColor = ContextCompat.getColor(context, colorResId)
    }

    /**
     * Set titleColor by [colorInt].
     */
    fun setTitleColorByColorInt(@ColorInt colorInt:Int){
        titleColor = colorInt
    }

    fun setClickListener(clickListener:VastSwipeMenuClickListener){
        this.clickListener = clickListener
    }

}