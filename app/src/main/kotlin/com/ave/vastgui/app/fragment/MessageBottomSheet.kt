/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Message
import com.ave.vastgui.app.databinding.FragmentMessageBottomsheetBinding
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4

class MessageBottomSheet : BottomSheetDialogFragment() {

    private val mBinding by viewBinding(FragmentMessageBottomsheetBinding::bind)
    private lateinit var message: Message

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.message = message
    }

    fun setMessage(message: Message) {
        this.message = message
    }

    companion object {
        const val TAG = "MessageBottomSheet"
    }
}