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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityDialogBinding
import com.ave.vastgui.core.annotation.ExperimentalApi
import com.ave.vastgui.tools.fragment.FullScreenDialogFragment
import com.ave.vastgui.tools.view.dialog.MaterialAlertDialogBuilder
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.google.android.material.textview.MaterialTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/20
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/dialog/material-alert-dialog-builder/

@OptIn(ExperimentalApi::class)
class DialogActivity : AppCompatActivity(R.layout.activity_dialog) {

    private val binding by viewBinding(ActivityDialogBinding::bind)
    private val dialogBuilder by lazy {
        MaterialAlertDialogBuilder(this)
    }

    private val fullScreenDialogFragment by lazy { FullScreenDialogFragment(R.layout.dialog_fragment_full_screen) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.dialogBtn.setOnClickListener {
            dialogBuilder.apply {
                setView(R.layout.dialog_custom)
                requireView().apply {
                    findViewById<MaterialTextView>(R.id.title).text = "这是一个自定义布局"
                }
                show()
            }
        }

        binding.fullscreenDialogBtn.setOnClickListener {
            fullScreenDialogFragment.show(supportFragmentManager, "FullScreenDialogFragment")
        }
    }

}