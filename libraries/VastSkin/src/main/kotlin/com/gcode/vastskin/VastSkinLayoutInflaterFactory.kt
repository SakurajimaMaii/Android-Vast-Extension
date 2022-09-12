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

package com.gcode.vastskin

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.gcode.vastskin.utils.VastSkinUtils
import java.lang.reflect.Constructor
import java.util.*

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/27 18:35

internal class VastSkinLayoutInflaterFactory(private val activity: Activity) :
    LayoutInflater.Factory2,
    Observer {

    /** The tag for log. */
    private val tag = this::class.java.simpleName

    private val skinAttribute: VastSkinAttribute =
        VastSkinAttribute()

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        var view = createSDKView(name, context, attrs)
        if (null == view) {
            view = createView(name, context, attrs)
        }
        if (null != view) {
            skinAttribute.look(view, attrs)
        }
        return view

    }

    /**
     * Create a view object define in Android SDK, if the view is
     * custom, the [createSDKView] will return `null`.
     *
     * [createSDKView] will first check the [name] to determine if
     * the view is custom. If the view is custom, it will return null.
     * Otherwise the prefix in the [mClassPrefixList] and the [name]
     * will be combined one by one, and finally the view will be constructed.
     *
     * @since 0.0.1
     */
    private fun createSDKView(name: String, context: Context, attrs: AttributeSet): View? {
        // The view is custom
        if (-1 != name.indexOf('.')) {
            return null
        }
        // The view is define in Android SDK
        for (classPrefix in mClassPrefixList) {
            val view = createView(classPrefix + name, context, attrs)
            // view is null probably because the combination of prefix and name is wrong.
            if (view != null) {
                return view
            }
        }
        return null
    }

    /**
     * [createView] will first find the constructor,
     * then create the view.
     *
     * @since 0.0.1
     */
    private fun createView(name: String, context: Context, attrs: AttributeSet): View? {
        val constructor = findConstructor(context, name)
        return constructor?.newInstance(context, attrs)
    }

    /**
     * Get the constructor of the class.
     *
     * @since 0.0.1
     */
    private fun findConstructor(context: Context, name: String): Constructor<out View>? {
        var constructor = mConstructorMap[name]
        if (constructor == null) {
            try {
                val clazz = Class.forName(name, false, context.classLoader).asSubclass(
                    View::class.java
                )
                constructor = clazz.getConstructor(*mConstructorSignature)
                mConstructorMap[name] = constructor
            } catch (e:ClassNotFoundException){
                // Probably because the combination of prefix and name is wrong.
                // So just return null, no other operations are required.
                return null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return constructor
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    override fun update(o: Observable, arg: Any) {
        VastSkinUtils.updateStatusBarColor(activity)
        skinAttribute.applySkin()
    }

    companion object {

        private val mClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
        )

        private val mConstructorSignature = arrayOf(
            Context::class.java, AttributeSet::class.java
        )

        private val mConstructorMap = HashMap<String, Constructor<out View>?>()

    }
}