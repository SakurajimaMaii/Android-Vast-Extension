/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.vbdelegate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.FragmentSenderBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/11
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#fragment

class VbFragment1 : Fragment(R.layout.fragment_sender) {

    private val mBinding by viewBinding(FragmentSenderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.sentence.setOnClickListener {
            VbFragment2().show(childFragmentManager, null)
        }
    }

}

class VbFragment2 : DialogFragment(R.layout.fragment_sender) {

    val mBinding by viewBinding(FragmentSenderBinding::bind, R.id.fragment_sender_root)

}