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

package com.ave.vastgui.app.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.databinding.FragmentSenderBinding
import com.ave.vastgui.app.viewmodel.SharedVM
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.fragment.VastVmFragment
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

class SenderFragment(override val layoutId: Int = R.layout.fragment_sender) :
    VastVmFragment<SharedVM>() {

    private val mBinding by viewBinding(FragmentSenderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.sentence.setOnClickListener {
            getViewModel().getSentenceAutoHandle()
        }
    }

    override fun setVmBySelf(): Boolean {
        return false
    }

}