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

package com.ave.vastgui.tools.fragment.delegate

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.tools.lifecycle.reflexViewModel
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/6
// Description: 
// Documentation:
// Reference:

open class FragmentVbVmDelegate<VB : ViewBinding, VM : ViewModel>(
    fragment: Fragment,
) : FragmentDelegate(fragment) {

    // ViewBinding
    private var mBinding: VB? = null

    // ViewModel
    private val mViewModel: VM by lazy {
        fragment.reflexViewModel(setVmBySelf()) {
            return@reflexViewModel createViewModel(it)
        }
    }

    override fun getBinding(): VB {
        return mBinding ?: throw NullPointerException("mBinding is null.")
    }

    override fun clearBinding() {
        mBinding = null
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

    init {
        mBinding = fragment.reflexViewBinding()
    }

}