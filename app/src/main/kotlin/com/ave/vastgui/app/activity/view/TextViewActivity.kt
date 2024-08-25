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
import com.ave.vastgui.app.databinding.ActivityTextViewBinding
import com.ave.vastgui.tools.activity.widget.screenConfig
import com.ave.vastgui.tools.view.textview.mailboxassociateview.MailBoxAssociateTokenizer
import com.ave.vastgui.tools.view.textview.mailboxassociateview.defaultMailBoxAssociateViewAdapter
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/textview/mail-box-associate-view/
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/textview/marquee-text-view/

class TextViewActivity : AppCompatActivity() {

    private val mBinding by viewBinding(ActivityTextViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenConfig(mEnableActionBar = true, mEnableFullScreen = false)

        mBinding.mailBoxAssociateView.apply {
            setAdapter(defaultMailBoxAssociateViewAdapter())
            setTokenizer(MailBoxAssociateTokenizer())
        }

        mBinding.marqueeTextView.setMarqueeNum(2)
    }

}