/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastutils.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.gcode.vasttools.activity.VastVbVmActivity
import com.gcode.vasttools.adapter.VastFragmentAdapter
import com.gcode.vastutils.databinding.ActivityFragmentsBinding
import com.gcode.vastutils.fragment.SampleVbFragment
import com.gcode.vastutils.fragment.SampleVbVmFragment
import com.gcode.vastutils.fragment.SampleVmFragment
import com.gcode.vastutils.viewModel.SampleSharedVM

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 19:45
// Description:
// Documentation:

class FragmentsActivity : VastVbVmActivity<ActivityFragmentsBinding,SampleSharedVM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vp2.apply {
            adapter = VastFragmentAdapter(this@FragmentsActivity,ArrayList<Fragment>().apply {
                add(SampleVbVmFragment())
                add(SampleVmFragment())
                add(SampleVbFragment())
            })
        }
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return SampleSharedVM("Activity")
    }

}