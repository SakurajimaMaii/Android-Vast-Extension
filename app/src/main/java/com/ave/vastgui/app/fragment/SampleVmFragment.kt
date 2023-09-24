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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ave.vastgui.app.R
import com.ave.vastgui.app.viewmodel.SampleSharedVM
import com.google.android.material.textview.MaterialTextView

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

class SampleVmFragment : Fragment() {

    private val mViewModel by viewModels<SampleSharedVM>({ requireActivity() })

    private lateinit var tv: TextView
    private lateinit var count: MaterialTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample_vm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        count = view.findViewById(R.id.count)

        mViewModel.count.observe(requireActivity()) {
            count.text = it.toString()
        }
    }

}