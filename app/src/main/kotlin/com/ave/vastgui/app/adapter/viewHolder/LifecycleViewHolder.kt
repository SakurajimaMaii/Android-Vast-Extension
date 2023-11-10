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

package com.ave.vastgui.app.adapter.viewHolder

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.ave.vastgui.adapter.base.BaseBindHolder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/11/4

class LifecycleViewHolder(binding: ViewDataBinding) : BaseBindHolder(binding), LifecycleOwner {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    fun onCreate() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            // View onDetached 的时候回调 onDestroy()
            override fun onViewDetachedFromWindow(v: View) {
                itemView.removeOnAttachStateChangeListener(this)
                onDestroy()
            }

            // View onAttached 的时候回调 onCreate()
            override fun onViewAttachedToWindow(v: View) {
                onCreate()
            }
        })
    }

}