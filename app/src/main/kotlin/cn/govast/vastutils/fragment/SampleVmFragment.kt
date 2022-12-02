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

package cn.govast.vastutils.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vasttools.fragment.delegate.FragmentVmDelegate
import cn.govast.vastutils.R
import cn.govast.vastutils.viewmodel.SampleSharedVM
import com.google.android.material.textview.MaterialTextView

class SampleVmFragment : Fragment() {

    private inner class FVD : FragmentVmDelegate<SampleSharedVM>(this) {
        override fun setVmBySelf(): Boolean {
            return false
        }
    }

    private lateinit var tv: TextView
    private lateinit var count: MaterialTextView
    private var mFragmentDelegate by NotNUllVar<FVD>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentDelegate = FVD()
        return inflater.inflate(R.layout.fragment_sample_vm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv = view.findViewById(R.id.tv)
        count = view.findViewById(R.id.count)

        tv.text = mFragmentDelegate.getViewModel().tag

        mFragmentDelegate.getViewModel().count.observe(requireActivity()) {
            count.text = it.toString()
        }
    }

}