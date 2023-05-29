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

package com.ave.vastgui.tools.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.ave.vastgui.tools.helper.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/19
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/ResUtils/

object ResUtils {

    /**
     * Get string by [id] or use [callback] as the default value, throw
     * exception otherwise.
     *
     * @throws Resources.NotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @JvmOverloads
    @Throws(Resources.NotFoundException::class)
    fun getString(@StringRes id: Int, callback: String? = null): String = try {
        ContextHelper.getAppContext().resources.getString(id)
    } catch (exception: Exception) {
        callback ?: throw exception
    }

    /**
     * Get drawable by [name].
     *
     * @param name the name of the drawable.
     * @return The drawable resource get by [name], or use [callback] as the
     *     default value, otherwise throw exception.
     * @throws Resources.NotFoundException
     * @since 0.5.1
     */
    @SuppressLint("DiscouragedApi")
    @JvmStatic
    @JvmOverloads
    @Throws(Resources.NotFoundException::class)
    fun getDrawable(name: String, callback: Drawable? = null): Drawable = try {
        val context = ContextHelper.getAppContext()
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        ResourcesCompat.getDrawable(context.resources, resId, context.theme)!!
    } catch (exception: Exception) {
        callback ?: throw exception
    }

    /**
     * Get drawable by [resId].
     *
     * @param resId the resource id of the drawable.
     * @return the drawable resource get by [resId], or use [callback] as the
     *     default value, otherwise throw exception.
     * @throws Resources.NotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @JvmOverloads
    @Throws(Resources.NotFoundException::class)
    fun getDrawable(
        @DrawableRes resId: Int, callback: Drawable? = null
    ): Drawable = try {
        val context = ContextHelper.getAppContext()
        ResourcesCompat.getDrawable(context.resources, resId, context.theme)!!
    } catch (exception: NotFoundException) {
        callback ?: throw exception
    }


    /**
     * A single color value in the form 0xAARRGGBB. Or color int of [callback]
     * will be return if resource is not exist, otherwise throw exception.
     *
     * @param id the resource id of color.
     * @throws Resources.NotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @JvmOverloads
    @Throws(Resources.NotFoundException::class)
    fun getColor(@ColorRes id: Int, @ColorInt callback: Int? = null): Int = try {
        val context: Context = ContextHelper.getAppContext()
        context.getColor(id)
    } catch (exception: Exception) {
        callback ?: throw exception
    }

    /**
     * Retrieve a dimensional for a particular resource ID for use as an offset
     * in raw pixels. The returned value is converted to integer pixels for
     * you. An offset conversion involves simply truncating the base value to
     * an integer. or use [callback] as the default value, otherwise throw
     * exception.
     *
     * @param id the resource id of dimension.
     * @throws Resources.NotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @JvmOverloads
    @Throws(Resources.NotFoundException::class)
    fun getDimensionPixelOffset(@DimenRes id: Int, callback: Int? = null): Int = try {
        val context: Context = ContextHelper.getAppContext()
        context.resources.getDimensionPixelOffset(id)
    } catch (exception: NotFoundException) {
        callback ?: throw exception
    }

}