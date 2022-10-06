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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import cn.govast.vasttools.delegate.fragment.FragmentVmDelegate
import cn.govast.vasttools.extension.NotNUllVar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:18
// Description: Please make sure that the fragment extends VastVmFragment when the fragment using viewModel.
// Documentation: [VastBaseFragment](https://sakurajimamaii.github.io/VastDocs/document/en/VastBaseFragment.html)

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
 * @since 0.0.6
 */
abstract class VastVmFragment<VM : ViewModel> : VastFragment() {

    /**
     * When you are not using view binding, you should set [layoutId] to the
     * corresponding view resource id of this Fragment.
     *
     * @since 0.0.6
     */
    protected abstract val layoutId: Int

    // Fragment Delegate
    private var mFragmentDelegate by NotNUllVar<FragmentVmDelegate<VM>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentDelegate = object : FragmentVmDelegate<VM>(this) {}
        return inflater.inflate(layoutId, container, false)
    }

    protected fun getViewModel(): VM {
        return mFragmentDelegate.getViewModel()
    }

    final override fun createFragmentDelegate(): FragmentVmDelegate<VM> {
        return mFragmentDelegate
    }

}