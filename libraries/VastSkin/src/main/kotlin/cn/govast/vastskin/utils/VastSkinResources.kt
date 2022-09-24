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

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.res.ResourcesCompat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/30 18:52
// Description: Used to loading skin resource from original app or skin apk.

object VastSkinResources {

    private var mSkinPackageName: String? = null

    /**
     * If true, use default resources, otherwise use skin resources
     *
     * @since 0.0.1
     */
    private var isDefaultTheme = true

    /**
     * The original [Resources] of app.
     *
     * @since 0.0.1
     */
    private lateinit var mAppResources: Resources

    /**
     * The [Resources] of skin apk.
     *
     * @since 0.0.1
     */
    private var mSkinResources: Resources? = null

    internal fun initSkinResources(context: Context) {
        mAppResources = context.resources
    }

    internal fun reset() {
        mSkinResources = null
        mSkinPackageName = ""
        isDefaultTheme = true
    }

    /**
     * Update skin apk resource and package name.
     *
     * @since 0.0.1
     */
    internal fun update(resources: Resources?, pkgName: String?) {
        mSkinResources = resources
        mSkinPackageName = pkgName
        isDefaultTheme = TextUtils.isEmpty(pkgName) || resources == null
    }

    /**
     * Obtain the resource name and resource type corresponding to the id through
     * the resource of the app, and find the resource id matching the skin package.
     *
     * @since 0.0.1
     */
    private fun getIdentifier(resId: Int): Int {
        if (isDefaultTheme) {
            return resId
        }
        val resName = mAppResources.getResourceEntryName(resId)
        val resType = mAppResources.getResourceTypeName(resId)
        return mSkinResources!!.getIdentifier(resName, resType, mSkinPackageName)
    }

    /**
     * Get color int by [resId].
     *
     * If [isDefaultTheme] is true, it will return a color int from
     * original app by [resId]. Otherwise, it will first get the resource
     * id in skin apk by [getIdentifier], if resId is a valid id, it will
     * get the resource from skin apk. Otherwise get resource from original app.
     *
     * @since 0.0.1
     */
    internal fun getColor(resId: Int): Int {
        if (isDefaultTheme) {
            return mAppResources.getColor(resId, null)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getColor(resId, null)
        } else mSkinResources!!.getColor(skinId, null)
    }

    /**
     * Get color state list resource by [resId].
     *
     * If [isDefaultTheme] is true, it will return a [ColorStateList] from
     * original app by [resId]. Otherwise, it will first get the resource
     * id in skin apk by [getIdentifier], if resId is a valid id, it will
     * get the resource from skin apk. Otherwise get resource from original app.
     *
     * @since 0.0.1
     */
    internal fun getColorStateList(resId: Int): ColorStateList {
        if (isDefaultTheme) {
            return mAppResources.getColorStateList(resId, null)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getColorStateList(resId, null)
        } else mSkinResources!!.getColorStateList(skinId, null)
    }

    /**
     * Get drawable resource by [resId].
     *
     * If [isDefaultTheme] is true, it will return a [Drawable] from
     * original app by [resId]. Otherwise, it will first get the resource
     * id in skin apk by [getIdentifier], if resId is a valid id, it will
     * get the resource from skin apk. Otherwise get resource from original app.
     *
     * @since 0.0.1
     */
    internal fun getDrawable(resId: Int): Drawable? {
        if (isDefaultTheme) {
            return ResourcesCompat.getDrawable(mAppResources, resId, null)
        }
        val resourceId = getIdentifier(resId)
        return if (resourceId == 0) {
            ResourcesCompat.getDrawable(mAppResources, resId, null)
        } else ResourcesCompat.getDrawable(mSkinResources!!, resId, null)
    }

    /**
     * Get background src by [resId].
     *
     * [getBackground] will first determine whether the resource type,
     * if the type of resource is color, it will use [getColor] to get
     * the color resource. Otherwise, it will use [getDrawable] to get
     * drawable resource.
     *
     * @since 0.0.1
     */
    internal fun getBackground(resId: Int): Any? {
        return when(mAppResources.getResourceTypeName(resId)){
            "color" -> getColor(resId)
            "drawable" -> getDrawable(resId)
            else -> null
        }
    }

    /**
     * Get string by [resId].
     *
     * Because some strings may not be defined in the skin resource package,
     * `mThemeResources.getString` will throw a [NotFoundException] exception
     * during the execution process. At this time, the [getText] method will
     * catch this exception and use the string resource defined in the original
     * app as the return value.
     *
     * @since 0.0.1
     */
    internal fun getText(resId:Int):String{
        return if(isDefaultTheme){
            mAppResources.getString(resId)
        }else{
            try{
                val resIdInSkin = getIdentifier(resId)
                mSkinResources!!.getString(resIdInSkin)
            }catch (e:NotFoundException){
                mAppResources.getString(resId)
            }
        }
    }
}