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

package com.ave.vastgui.app.activity.log

import android.os.Bundle
import com.ave.vastgui.app.databinding.ActivityLogBinding
import com.ave.vastgui.app.viewmodel.SampleSharedVM
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.activity.widget.screenConfig

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

class LogActivity : VastVbVmActivity<ActivityLogBinding, SampleSharedVM>() {

    private val logger = mLogFactory.getLog(LogActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenConfig(mEnableActionBar = false, mEnableFullScreen = false)

        getBinding().addOne.setOnClickListener {
            getViewModel().addOne()
        }

        getViewModel().count.observe(this) { count ->
            getBinding().count.text = count.toString()
            logger.d("这是一条日志 $count")
        }
    }

}