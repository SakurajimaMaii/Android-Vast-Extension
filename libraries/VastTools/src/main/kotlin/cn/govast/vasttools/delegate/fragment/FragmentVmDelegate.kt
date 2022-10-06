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

package cn.govast.vasttools.delegate.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import cn.govast.vasttools.extension.CreateViewModel
import cn.govast.vasttools.extension.reflexViewModel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/6
// Description: 
// Documentation:
// Reference:

open class FragmentVmDelegate<VM : ViewModel>(
    fragment: Fragment,
) : FragmentDelegate(fragment) {

    // ViewModel
    private val mViewModel: VM by lazy {
        fragment.reflexViewModel(object : CreateViewModel {
            override fun createVM(modelClass: Class<out ViewModel>): ViewModel {
                return createViewModel(modelClass)
            }
        }, setVmBySelf())
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

}