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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityDialogBinding
import com.ave.vastgui.tools.view.dialog.MaterialAlertDialogBuilder
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.google.android.material.textview.MaterialTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/20
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/dialog/material-alert-dialog-builder/

class DialogActivity : AppCompatActivity(R.layout.activity_dialog) {

    private val mBinding by viewBinding(ActivityDialogBinding::bind)
    private val mDialogBuilder by lazy {
        MaterialAlertDialogBuilder(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.show.setOnClickListener {
            mDialogBuilder.apply {
                setView(R.layout.dialog_custom)
                requireView().apply {
                    findViewById<MaterialTextView>(R.id.title).text = "这是一个自定义布局"
                }
                show()
            }
        }
    }

}