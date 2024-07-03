/*
 * Copyright 2021-2024 VastGui
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
import com.ave.vastgui.tools.lifecycle.reflectViewModel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

/**
 * VastVmActivity.
 *
 * ```kotlin
 * // Because don't using the ViewBinding,so just set the layoutId to layout id.
 * class SampleVmFragment(override val layoutId: Int = R.layout.fragment_sample_vm) : VastVmFragment<SampleSharedVM>()
 * {
 *     override fun initView(view: View, savedInstanceState: Bundle?) {
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VM [ViewModel] of the fragment.
 */
abstract class VastVmFragment<VM : ViewModel> : VastFragment() {

    /**
     * When you are not using view binding, you should set [layoutId] to the
     * corresponding view resource id of this Fragment.
     */
    protected abstract val layoutId: Int

    // ViewModel
    private val mViewModel: VM by lazy {
        reflectViewModel(
            this.javaClass,
            if (!setVmBySelf()) requireActivity() else this,
            VastVmFragment::class.java
        ) {
            return@reflectViewModel createViewModel(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

    override fun setVmBySelf(): Boolean = false

}