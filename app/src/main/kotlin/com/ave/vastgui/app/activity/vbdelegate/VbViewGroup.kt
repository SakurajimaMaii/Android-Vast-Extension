/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.vbdelegate

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ViewgroupVbBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#viewgroup

class ViewGroupGetViewBindingByDelegate @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    // 使用方式 1
    private val mBinding by viewBinding(ViewgroupVbBinding::bind)
    // 使用方式 2
    // root 是 ViewgroupVbBinding 的根布局 id
    // private val mBinding by viewBinding(ViewgroupVbBinding::bind, R.id.root)

    init {
        inflate(context, R.layout.viewgroup_vb, this)
        orientation = VERTICAL
    }

    fun setFirstName(value: String) {
        mBinding.firstName.text = value
    }

    fun setLastName(value: String) {
        mBinding.lastName.text = value
    }

}