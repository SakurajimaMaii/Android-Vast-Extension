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
import android.os.PersistableBundle
import com.gcode.vasttools.activity.VastVbVmActivity
import com.gcode.vasttools.utils.AppUtils
import com.gcode.vasttools.utils.LogUtils
import com.gcode.vastutils.databinding.ActivityBaseVbBinding
import com.gcode.vastutils.viewModel.SampleSharedVM

class BaseActivity : VastVbVmActivity<ActivityBaseVbBinding, SampleSharedVM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableActionBar = false // 不显示ActionBar
        super.onCreate(savedInstanceState)

        mBinding.addOne.setOnClickListener {
            mViewModel.addOne()
        }

        mViewModel.count.observe(this){
            mBinding.count.text = it.toString()
        }
    }

}