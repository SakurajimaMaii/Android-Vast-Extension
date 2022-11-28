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
package cn.govast.vastskin.utils

import android.app.Activity
import android.content.Context
import cn.govast.vastskin.utils.VastSkinResources.getColor

internal object VastSkinUtils {

    private val tag = this::class.java.simpleName

    private val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(
        android.R.attr.colorPrimaryDark
    )
    private val STATUS_BAR_COLOR_ATTRS = intArrayOf(
        android.R.attr.statusBarColor, android.R.attr.navigationBarColor
    )

    /**
     * Default resource id.
     */
    private const val DEFAULT_RESOURCE_ID = 0

    /**
     * Returns the resource id corresponding to the attribute in attrs.
     *
     * If you declare the theme like this:
     * ```xml
     * <style name="VastSkin" parent="Theme.Material3.Light.NoActionBar">
     *     <item name="colorPrimary">#0060A9</item>
     * </style>
     * ```
     * when you use [getResourceId] like this:
     * ```kotlin
     * val resId = VastSkinUtils.getResourceId(view.context, intArrayOf(R.attr.colorPrimary))[0]
     * ```
     * The value of resId will be 0,So the correct way should be:
     * ```xml
     * <!-- Declare the color in color.xml-->
     * <color name="surface">#D9E5F3</color>
     * <!-- Declare the color in color.xml-->
     * <style name="VastSkin" parent="Theme.Material3.Light.NoActionBar">
     *     <item name="colorPrimary">@color/surface</item>
     * </style>
     * ```
     * So that the [getResourceId] will return int value of `R.color.surface`
     */
    fun getResourceId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val a = context.theme.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = a.getResourceId(i, DEFAULT_RESOURCE_ID)
        }
        a.recycle()
        return resIds
    }

    fun updateStatusBarColor(activity: Activity) {
        val resIds = getResourceId(activity, STATUS_BAR_COLOR_ATTRS)
        val statusBarColorResId = resIds[0]
        val navigationBarColor = resIds[1]

        if (statusBarColorResId != 0) {
            val color = getColor(statusBarColorResId)
            activity.window.statusBarColor = color
        } else {
            val colorPrimaryDarkResId = getResourceId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (colorPrimaryDarkResId != 0) {
                val color = getColor(colorPrimaryDarkResId)
                activity.window.statusBarColor = color
            }
        }
        if (navigationBarColor != 0) {
            val color = getColor(navigationBarColor)
            activity.window.navigationBarColor = color
        }
    }
}