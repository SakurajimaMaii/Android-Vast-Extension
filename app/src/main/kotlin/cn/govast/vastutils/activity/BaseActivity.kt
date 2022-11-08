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

package cn.govast.vastutils.activity

import android.os.Bundle
import cn.govast.vasttools.activity.VastVbVmActivity
import cn.govast.vastutils.databinding.ActivityBaseVbBinding
import cn.govast.vastutils.viewModel.SampleSharedVM

class BaseActivity : VastVbVmActivity<ActivityBaseVbBinding, SampleSharedVM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableActionBar(false) // 不显示ActionBar
        super.onCreate(savedInstanceState)

        getBinding().addOne.setOnClickListener {
            getViewModel().addOne()
        }

        getViewModel().count.observe(this){
            getBinding().count.text = it.toString()
        }
    }

}