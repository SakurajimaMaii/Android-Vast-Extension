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

package com.ave.vastgui.app.activity.reflexexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.tools.lifecycle.reflexViewModel
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/6
// Description: 
// Documentation:
// Reference:

abstract class BaseActivity2<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    // ViewBinding
    protected val mBinding: VB by lazy {
        reflexViewBinding(this)
    }

    // ViewModel
    protected val mViewModel: VM by lazy {
        reflexViewModel(this.javaClass, this) {
            return@reflexViewModel createViewModel(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

}