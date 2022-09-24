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

package cn.govast.vastskin.model

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import cn.govast.vastskin.*
import cn.govast.vastskin.utils.VastSkinResources


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/30 20:05
// Description: VastSkinView is used to change the attr of the view specified in skinPairs.

/**
 * [VastSkinViewWrapper] will modify the [view] according
 * to the attribute name and resource id provided by [skinPairs].
 *
 * @property view view which need to change the attributes.
 * @property skinPairs list of [VastSkinPair].
 *
 * @since 0.0.1
 */
internal class VastSkinViewWrapper(private var view: View, private var skinPairs: List<VastSkinPair>) {

    private val tag = this::class.java.simpleName

    fun applySkin() {
        applySkinSupport()
        for (skinPair in skinPairs) {
            // Fix https://github.com/SakurajimaMaii/VastUtils/issues/38
            if(0 != skinPair.resourceId){
                var left: Drawable? = null
                var top: Drawable? = null
                var right: Drawable? = null
                var bottom: Drawable? = null
                when (skinPair.attributeName) {
                    CHANGEABLY_BACKGROUND -> {
                        val background: Any? =
                            VastSkinResources.getBackground(skinPair.resourceId)
                        if (background is Int) {
                            view.setBackgroundColor(background)
                        } else if (null != background) {
                            ViewCompat.setBackground(view, background as Drawable)
                        }
                    }
                    CHANGEABLY_BACKGROUND_TINT -> {
                        val background: Any? =
                            VastSkinResources.getBackground(skinPair.resourceId)
                        if (background is Int) {
                            val colorStateList = ColorStateList.valueOf(background)
                            view.backgroundTintList = colorStateList
                        }
                    }
                    CHANGEABLY_SRC -> {
                        val background = VastSkinResources.getBackground(skinPair.resourceId)
                        if (background is Int) {
                            (view as ImageView).setImageDrawable(ColorDrawable(background))
                        } else {
                            (view as ImageView).setImageDrawable(background as Drawable?)
                        }
                    }
                    CHANGEABLY_TEXT -> (view as TextView).text =
                        VastSkinResources.getText(skinPair.resourceId)
                    CHANGEABLY_TEXT_COLOR -> (view as TextView).setTextColor(
                        VastSkinResources.getColorStateList(skinPair.resourceId)
                    )
                    CHANGEABLY_DRAWABLE_LEFT -> left =
                        VastSkinResources.getDrawable(skinPair.resourceId)
                    CHANGEABLY_DRAWABLE_TOP -> top =
                        VastSkinResources.getDrawable(skinPair.resourceId)
                    CHANGEABLY_DRAWABLE_RIGHT -> right =
                        VastSkinResources.getDrawable(skinPair.resourceId)
                    CHANGEABLY_DRAWABLE_BOTTOM -> bottom =
                        VastSkinResources.getDrawable(skinPair.resourceId)
                    else -> {}
                }
                if (null != left || null != right || null != top || null != bottom) {
                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
                }
            }
        }
    }

    private fun applySkinSupport() {
        if (view is VastSkinSupport) {
            (view as VastSkinSupport).applySkin()
        }
    }
}