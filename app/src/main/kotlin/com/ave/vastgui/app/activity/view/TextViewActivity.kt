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

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.os.Vibrator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityTextViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.os.LifecycleHandler
import com.ave.vastgui.tools.os.LifecycleHandlerThread
import com.ave.vastgui.tools.vibrator.VibratorCompat
import com.ave.vastgui.tools.view.textview.mailboxassociateview.MailBoxAssociateTokenizer
import com.ave.vastgui.tools.view.textview.mailboxassociateview.defaultMailBoxAssociateViewAdapter
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/textview/mail-box-associate-view/
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/textview/marquee-text-view/

class TextViewActivity : AppCompatActivity(R.layout.activity_text_view) {

    private val logger = logFactory(TextViewActivity::class.java)

    private val binding by viewBinding(ActivityTextViewBinding::bind)

    private var vibratorUtil: VibratorCompat by Delegates.notNull()

    private val handlerThread by object : LifecycleHandlerThread("") {
        override val handleMessage: (Message) -> Unit = { msg ->
            if (msg.what == 1) {
                logger.d("接收到消息")
            }
        }
    }

    private val handler by LifecycleHandler()

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        vibratorUtil = VibratorCompat(this)

        binding.mailBoxAssociateView.apply {
            setAdapter(defaultMailBoxAssociateViewAdapter())
            setTokenizer(MailBoxAssociateTokenizer())
        }

        binding.marqueeTextView.setMarqueeNum(2)

        logger.d("发送消息1")
        handlerThread.obtainMessage(1).sendToTarget()

        binding.root.setOnClickListener {
            vibratorUtil.vibrate(1000)
            logger.d("Vibrator 的 id 是 ${ContextCompat.getSystemService(this, Vibrator::class.java)?.resonantFrequency}")
        }
    }

}