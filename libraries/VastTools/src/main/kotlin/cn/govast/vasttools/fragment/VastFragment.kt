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

package cn.govast.vasttools.fragment

import androidx.fragment.app.Fragment
import cn.govast.vasttools.delegate.FragmentDelegate

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/14 17:11
// Description: 
// Documentation:

abstract class VastFragment : Fragment() {

    private val mFragmentDelegate by lazy {
        createFragmentDelegate()
    }

    protected fun getDefaultTag(): String{
        return mFragmentDelegate.getDefaultTag()
    }

    protected abstract fun createFragmentDelegate(): FragmentDelegate

    /**
     * When [setVmBySelf] is true, the ViewModel representing the Fragment is
     * retained by itself. When you want the ViewModel to be retained by its
     * associated Activity, please set [setVmBySelf] to false.
     */
    protected abstract fun setVmBySelf(): Boolean

    protected fun getRequestBuilder() = mFragmentDelegate.getRequestBuilder()

    protected fun createMainScope() = mFragmentDelegate.createMainScope()

}