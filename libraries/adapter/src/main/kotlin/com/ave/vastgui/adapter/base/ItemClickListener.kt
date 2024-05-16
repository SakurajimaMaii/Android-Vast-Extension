/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/** @since 1.1.1 */
interface ItemClickListener<T> {

    /**
     * 用于为适配器注册点击事件监听。
     *
     * @param listener 点击监听器。如果 [listener] 为空，它将删除点击监听器。
     * @since 1.1.1
     */
    fun setOnItemClickListener(listener: OnItemClickListener<T>?)

    /** @since 1.1.1 */
    fun getOnItemClickListener(): OnItemClickListener<T>?

    /**
     * 为适配器注册长按监听器。
     *
     * @param listener 一个长点击监听器。如果 [listener] 为 null，它将删除长按监听器。
     * @since 1.1.1
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?)

    /** @since 1.1.1 */
    fun getOnItemLongClickListener(): OnItemLongClickListener<T>?

}