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

package com.ave.vastgui.app.adapter

import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import com.ave.vastgui.app.R
import com.ave.vastgui.tools.utils.DateUtils
import com.ave.vastgui.tools.view.avatar.Avatar
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import org.alee.component.skin.service.ThemeSkinService
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/27

/** 绑定适配器 */
object BindingAdapter {
    @JvmStatic
    @BindingAdapter("avatar_text")
    fun setAvatarText(view: Avatar, text: String?) {
        view.refreshWithInvalidate {
            mText = text?.first().toString()
        }
    }

    @JvmStatic
    @BindingAdapter("srcCompat")
    fun setImage(view: ShapeableImageView, @DrawableRes id: Int) {
        // 参考 https://github.com/CoderAlee/PaintedSkin/issues/30
        val drawable = ThemeSkinService
            .getInstance()
            .currentThemeSkinPack
            .getDrawable(id)
        view.setImageDrawable(drawable)
    }

    @JvmStatic
    @BindingAdapter("srcCompat")
    fun setImage(view: ShapeableImageView, url: String) {
        // 参考 https://github.com/CoderAlee/PaintedSkin/issues/30
        view.load(url) {
            crossfade(true)
            placeholder(R.drawable.background)
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun setDate(view: MaterialTextView, date: Long) {
        view.text = SimpleDateFormat(DateUtils.FORMAT_MM_DD_HH_MM, Locale.getDefault()).format(date)
    }
}