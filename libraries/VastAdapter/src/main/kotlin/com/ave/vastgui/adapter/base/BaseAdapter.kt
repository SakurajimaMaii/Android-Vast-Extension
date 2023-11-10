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

package com.ave.vastgui.adapter.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterClickRegister
import com.ave.vastgui.adapter.widget.AdapterLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10

abstract class BaseAdapter<T : ViewHolder> :
    RecyclerView.Adapter<T>(),
    AdapterClickRegister {

    protected var onItemClickListener: AdapterClickListener? = null
    protected var onItemLongClickListener: AdapterLongClickListener? = null

    final override fun registerClickEvent(l: AdapterClickListener?) {
        onItemClickListener = l
    }

    final override fun registerLongClickEvent(l: AdapterLongClickListener?) {
        onItemLongClickListener = l
    }

    final override fun getClickEvent(): AdapterClickListener? {
        throw RuntimeException("You shouldn't call this method.")
    }

    final override fun getLongClickEvent(): AdapterLongClickListener? {
        throw RuntimeException("You shouldn't call this method.")
    }

}