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

package com.ave.vastgui.adapter.listener

import android.view.View
import com.ave.vastgui.adapter.base.ItemWrapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/27
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * 用于监听列表项内部的控件的长按事件。
 *
 * @since 1.1.1
 */
interface OnItemChildLongClickListener<T: Any> {
    /**
     * 当列表项内部的控件被长按时会触发该回调。
     *
     * @param view 当前被长按的控件。
     * @param item 当前被点击的列表项，如果为空，则表示当前列表为空。
     * @since 1.2.0
     */
    fun onItemLongClick(view: View, pos: Int, item: T?): Boolean
}