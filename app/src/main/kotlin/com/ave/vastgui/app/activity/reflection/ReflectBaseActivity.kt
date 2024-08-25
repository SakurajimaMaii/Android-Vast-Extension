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

package com.ave.vastgui.app.activity.reflection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.tools.lifecycle.reflectViewModel
import com.ave.vastgui.tools.viewbinding.reflectViewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/6
// Documentation: https://ave.entropy2020.cn/documents/tools/architecture-components/ui-layer-libraries/view-bind/vb-reflection/
// Documentation: https://ave.entropy2020.cn/documents/tools/architecture-components/ui-layer-libraries/lifecycle-aware-components/vm-reflection/

abstract class ReflectBaseActivity1<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    // ViewBinding
    protected val mBinding: VB by lazy {
        reflectViewBinding(ReflectBaseActivity1::class.java)
    }

    // ViewModel
    protected val mViewModel: VM by lazy {
        reflectViewModel(this.javaClass, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

}

abstract class ReflectBaseActivity2<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    // ViewBinding
    protected val mBinding: VB by lazy {
        reflectViewBinding(ReflectBaseActivity2::class.java)
    }

    // ViewModel
    protected val mViewModel: VM by lazy {
        reflectViewModel(this.javaClass, this) {
            return@reflectViewModel createViewModel(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.getDeclaredConstructor().newInstance()
    }

}