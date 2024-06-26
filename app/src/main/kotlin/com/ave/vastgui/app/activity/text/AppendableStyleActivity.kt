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

package com.ave.vastgui.app.activity.text

import android.os.Build
import android.os.Bundle
import android.text.style.ImageSpan
import android.text.style.QuoteSpan
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityAppendableStyleBinding
import com.ave.vastgui.tools.text.appendablestylestring.AppendableStyle
import com.ave.vastgui.tools.text.appendablestylestring.ScriptMode
import com.ave.vastgui.tools.text.appendablestylestring.appendableStyleScope
import com.ave.vastgui.tools.text.appendablestylestring.withImage
import com.ave.vastgui.tools.text.appendablestylestring.withStyle
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.kongzue.dialogx.dialogs.MessageDialog

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/appendable-style-string/appendable-style-string/

class AppendableStyleActivity : ComponentActivity(R.layout.activity_appendable_style) {

    private val mBinding by viewBinding(ActivityAppendableStyleBinding::bind)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = appendableStyleScope {
            withStyle(AppendableStyle(QuoteSpan(ColorUtils.colorHex2Int("#27ae60"), 10, 30))) {
                withStyle(AppendableStyle(fontSize = 20F.SP.toInt())) {
                    appendLine("什么是 Android?")
                }
                append("一个颠覆移动设备功能的平台，你可以访问")
                withStyle(AppendableStyle("https://www.android.com/intl/zh-CN_cn/what-is-android/")) {
                    append("链接")
                }
                appendLine("来了解更多。")
                append(
                    "从只能让设备运行，到让生活更轻松，都是Android在背后提供强力支持。" +
                            "有了Android, 才能让GPS避开拥堵，用手表发短信，让Google助理回答问题。" +
                            "目前有 25 亿部活跃设备搭载了 Android 操作系统。Android 能够为各种设备" +
                            "提供强力支持，从 5G 手机到炫酷的平板电脑，不胜枚举。"
                )
                withStyle(AppendableStyle(ScriptMode.SUPERSCRIPT)) { append("[1]") }
                append("\n")
                withImage(ImageSpan(this@AppendableStyleActivity, R.drawable.android_logo))
            }
        }
        MessageDialog.show("示例", message)
    }

}