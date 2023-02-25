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
import androidx.fragment.app.FragmentActivity
import com.ave.vastgui.tools.fragment.base.BaseFragment

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/5
// Description: 
// Documentation:
// Reference:

open class FragmentDelegate(
    private val fragment: Fragment
) : BaseFragment {

    override fun getBaseActivity(): FragmentActivity {
        return fragment.requireActivity()
    }

    override fun getDefaultTag(): String {
        return fragment::class.java.simpleName
    }

}