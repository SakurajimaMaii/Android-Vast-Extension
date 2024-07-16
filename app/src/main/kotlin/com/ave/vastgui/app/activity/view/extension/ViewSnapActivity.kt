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

package com.ave.vastgui.app.activity.view.extension

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivitySnapshotBinding
import com.ave.vastgui.tools.view.extension.SnapshotOption
import com.ave.vastgui.tools.view.extension.viewSnapshot
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/extension/snapshot-view/

class ViewSnapActivity : ComponentActivity(R.layout.activity_snapshot) {

    private val mBinding by viewBinding(ActivitySnapshotBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LayoutInflater.from(this).inflate(R.layout.viewgroup_inflate, null, false)
        mBinding.snapshotExample.setOnClickListener {
            mBinding.snapshotImage.setImageBitmap(viewSnapshot(mBinding.snapshotWait))
        }

        mBinding.snapshotInflate.setOnClickListener {
            mBinding.snapshotImage.setImageBitmap(viewSnapshot(view, SnapshotOption(1080, 540)))
        }
    }

}