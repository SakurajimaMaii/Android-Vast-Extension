/*
 * Copyright 2021-2024 VastGui
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

import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Documentation: https://ave.entropy2020.cn/documents/adapter/

/** @since 1.1.1 */
interface ItemClickListener<T : Any> {

    /**
     * Register a callback to be invoked when the item is clicked.
     *
     * @param listener Click callback. If [listener] is empty, it will remove the callback.
     * @since 1.1.1
     */
    fun setOnItemClickListener(listener: OnItemClickListener<T>?)

    /** @since 1.1.1 */
    fun getOnItemClickListener(): OnItemClickListener<T>?

    /**
     * Register a callback to be invoked when the item is clicked and held.
     *
     * @param listener If [listener] is empty, it will remove the callback.
     * @since 1.1.1
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?)

    /** @since 1.1.1 */
    fun getOnItemLongClickListener(): OnItemLongClickListener<T>?

}