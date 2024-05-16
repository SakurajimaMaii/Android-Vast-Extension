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

package com.ave.vastgui.tools.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.tools.viewbinding.reflectViewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/11 22:58
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

/**
 * VastVbFragment.
 *
 * ```kotlin
 * // Use in kotlin
 * class SampleVbFragment : VastVbFragment<FragmentSampleVbBinding>() {
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          // Something to do
 *      }
 * }
 * ```
 *
 * @param VB [ViewBinding] of the fragment layout.
 */
abstract class VastVbFragment<VB : ViewBinding> : VastFragment() {

    // ViewBinding
    private var mBinding: VB? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = reflectViewBinding(container, VastVbFragment::class.java)
        return getBinding().root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    override fun getBinding(): VB {
        return mBinding ?: throw RuntimeException("ViewBinding is null.")
    }

    override fun clearBinding() {
        mBinding = null
    }

    override fun setVmBySelf(): Boolean = false

}