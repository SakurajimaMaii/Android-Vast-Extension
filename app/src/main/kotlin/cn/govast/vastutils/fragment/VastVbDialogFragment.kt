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

package com.gcode.vasttools.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.extension.cast
import cn.govast.vasttools.extension.reflexViewBinding
import cn.govast.vasttools.fragment.VastFragmentInterface

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/7 12:06
// Description: 
// Documentation:

abstract class VastVbDialogFragment<VB : ViewBinding> : DialogFragment(), VastFragmentInterface {

    /**
     * The viewBinding of the fragment, it will be initialized in
     * [Fragment.onCreateView].
     *
     * @since 0.0.6
     */
    private lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = reflexViewBinding(javaClass, layoutInflater)
        return mBinding.root
    }

    final override fun setVmBySelf(): Boolean = false

    final override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

    final override fun getBinding(): VB {
        return cast(mBinding)
    }

    final override fun getViewModel(): ViewModel {
        throw RuntimeException("You should not call this method.")
    }

}