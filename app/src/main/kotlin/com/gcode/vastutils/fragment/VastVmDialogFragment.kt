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
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.gcode.vasttools.extension.CreateViewModel
import com.gcode.vasttools.extension.NotNUllSingleVar
import com.gcode.vasttools.extension.cast
import com.gcode.vasttools.extension.reflexViewModel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/7 12:10
// Description: 
// Documentation:

abstract class VastVmDialogFragment<VM : ViewModel> : DialogFragment(), VastFragmentInterface {

    private var vmBySelf by NotNUllSingleVar<Boolean>()

    /**
     * When you are not using view binding, you should set [layoutId] to the
     * corresponding view resource id of this Fragment.
     *
     * @since 0.0.9
     */
    protected abstract val layoutId: Int

    private val mViewModel: VM by lazy {
        reflexViewModel(object : CreateViewModel {
            override fun createVM(modelClass: Class<out ViewModel>): ViewModel {
                return createViewModel(modelClass)
            }
        }, vmBySelf)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vmBySelf = setVmBySelf()
        return inflater.inflate(layoutId, container, false)
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

    override fun setVmBySelf(): Boolean = false

    final override fun getBinding(): ViewBinding {
        throw RuntimeException("You should not call this method.")
    }

    final override fun getViewModel(): VM {
        return cast(mViewModel)
    }

}