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

package com.ave.vastgui.app.theme.compat

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.ave.vastgui.core.extension.nothing_to_do
import com.google.android.material.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import org.alee.component.skin.executor.ISkinExecutor
import org.alee.component.skin.executor.SkinElement
import org.alee.component.skin.executor.ViewSkinExecutor
import org.alee.component.skin.factory2.IExpandedFactory2
import org.alee.component.skin.parser.IThemeSkinExecutorBuilder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/29

/**
 * 用于兼容 [FloatingActionButton] 。
 */
class FabFactory : IExpandedFactory2 {
    override fun onCreateView(
        origin: View?,
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? = when (name) {
        ShapeableImageView::class.java.simpleName ->
            ShapeableImageView(context, attrs)

        FloatingActionButton::class.java.simpleName ->
            FloatingActionButton(context, attrs)

        else -> origin
    }
}

private class FabSkinExecutor(fullElement: SkinElement) :
    ViewSkinExecutor<FloatingActionButton>(fullElement) {
    override fun applyColor(
        view: FloatingActionButton,
        colorStateList: ColorStateList,
        attrName: String
    ) {
        super.applyColor(view, colorStateList, attrName)
        when (attrName) {
            "backgroundTint" -> view.backgroundTintList = colorStateList
            "tint" -> view.imageTintList = colorStateList
        }
    }
}

private class SivSkinExecutor(fullElement: SkinElement) :
    ViewSkinExecutor<ShapeableImageView>(fullElement) {
    override fun applyDrawable(view: ShapeableImageView, drawable: Drawable, attrName: String) {
        super.applyDrawable(view, drawable, attrName)
        when (attrName) {
            "srcCompat" -> view.setImageDrawable(drawable)
        }
    }
}

/**
 * 用于兼容 [FloatingActionButton] 。
 */
class FabExecutorBuilder : IThemeSkinExecutorBuilder {

    companion object {
        private val SUPPORT_ATTR: Map<Int, String> = HashMap<Int, String>().apply {
            put(R.styleable.ViewBackgroundHelper_backgroundTint, "backgroundTint")
            put(R.styleable.AppCompatImageView_tint, "tint")
        }
    }

    override fun parse(context: Context, attrs: AttributeSet): MutableSet<SkinElement> {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton)
        val elementSet: MutableSet<SkinElement> = HashSet()
        for (key in SUPPORT_ATTR.keys) {
            // FIXME 此处的try catch 是为了避免由于一个属性解析失败导致所有属性都无法换肤
            try {
                if (typedArray.hasValue(key)) {
                    SUPPORT_ATTR[key]?.run {
                        // FIXME 此处代码与addEnabledThemeSkinView 函数一样，需要提供待换肤的属性名称与所使用的资源id给框架
                        elementSet.add(SkinElement(this, typedArray.getResourceId(key, -1)))
                    }
                }
            } catch (ignored: Throwable) {
                nothing_to_do()
            }
        }
        typedArray.recycle()
        return elementSet
    }

    override fun requireSkinExecutor(view: View, element: SkinElement): ISkinExecutor {
        return FabSkinExecutor(element)
    }

    override fun isSupportAttr(view: View, attrName: String): Boolean {
        // FIXME 避免不同View 相同的自定义属性导致处理异常
        if (view !is FloatingActionButton) {
            return false
        }
        return SUPPORT_ATTR.containsValue(attrName)
    }
}

class SivExecutorBuilder : IThemeSkinExecutorBuilder {

    companion object {
        private val SUPPORT_ATTR: Map<Int, String> = HashMap<Int, String>().apply {
            put(R.styleable.AppCompatImageView_srcCompat, "srcCompat")
        }
    }

    override fun parse(context: Context, attrs: AttributeSet): MutableSet<SkinElement> {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeableImageView)
        val elementSet: MutableSet<SkinElement> = HashSet()
        for (key in SUPPORT_ATTR.keys) {
            // FIXME 此处的try catch 是为了避免由于一个属性解析失败导致所有属性都无法换肤
            try {
                if (typedArray.hasValue(key)) {
                    SUPPORT_ATTR[key]?.run {
                        // FIXME 此处代码与addEnabledThemeSkinView 函数一样，需要提供待换肤的属性名称与所使用的资源id给框架
                        elementSet.add(SkinElement(this, typedArray.getResourceId(key, -1)))
                    }
                }
            } catch (ignored: Throwable) {
                nothing_to_do()
            }
        }
        typedArray.recycle()
        return elementSet
    }

    override fun requireSkinExecutor(view: View, element: SkinElement): ISkinExecutor {
        return SivSkinExecutor(element)
    }

    override fun isSupportAttr(view: View, attrName: String): Boolean {
        // FIXME 避免不同View 相同的自定义属性导致处理异常
        if (view !is ShapeableImageView) {
            return false
        }
        return SUPPORT_ATTR.containsValue(attrName)
    }
}