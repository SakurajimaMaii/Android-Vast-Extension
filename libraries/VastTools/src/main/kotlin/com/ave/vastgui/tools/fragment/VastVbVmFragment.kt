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

package com.ave.vastgui.tools.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.tools.lifecycle.reflexViewModel
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:11
// Description: Please make sure that the fragment extends VastVbVmFragment when the fragment using viewBinding and viewModel.
// Documentation: [VastVbVmFragment](https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/Fragment/)

/**
 * VastVbVmFragment.
 *
 * ```kotlin
 * // Use in kotlin
 * class SampleVbVmFragment : VastVbVmFragment<FragmentSampleVbVmBinding, SampleSharedVM>() {
 *      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *          // Something to do
 *      }
 * }
 * ```
 *
 * @param VB [ViewBinding] of the fragment layout.
 * @param VM [ViewModel] of the fragment.
 */
abstract class VastVbVmFragment<VB : ViewBinding, VM : ViewModel> : VastFragment() {

    // ViewBinding
    private var mBinding: VB? = null

    // ViewModel
    private val mViewModel: VM by lazy {
        reflexViewModel(this.javaClass, if (!setVmBySelf()) requireActivity() else this) {
            return@reflexViewModel createViewModel(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = reflexViewBinding(this,container)
        return getBinding().root
    }

    override fun onDestroyView() {
        clearBinding()
        super.onDestroyView()
    }

    override fun setVmBySelf(): Boolean = false

    override fun getBinding(): VB {
        return mBinding ?: throw RuntimeException("ViewBinding is null.")
    }

    override fun clearBinding() {
        mBinding = null
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

}