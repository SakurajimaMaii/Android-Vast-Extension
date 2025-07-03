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

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityAvatarBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.avatar.Avatar
import com.ave.vastgui.tools.view.avatar.AvatarGroup
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.random.Random

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/avatar/avatar/
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/avatar/avatargroup/

class AvatarActivity : ComponentActivity(R.layout.activity_avatar) {

    private val logger = logFactory("AvatarActivity")
    private val binding by viewBinding(ActivityAvatarBinding::bind)
    private val radius: Float
        get() = Random(System.currentTimeMillis()).nextInt(0, 20).toFloat().DP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val avatar = Avatar(this).apply {
            srcColor = Color.BLUE
            srcText = "你好"
        }

        binding.avatarGroupStart.addView(avatar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        binding.avatarGroupEnd.setOverlapFrom(AvatarGroup.END)
        binding.root.setOnClickListener {
            logger.d("点击 root")
            binding.avatarGroupStart.setOverlapFrom(AvatarGroup.Companion.END)
            binding.avatarGroupStart.setShape(1 - binding.avatarGroupStart.shape)
            binding.avatarGroupStart.strokeWidth = 10f.DP
            binding.avatarGroupStart.cornerRadius = radius.also { logger.d("切换的半径是 $it") }
        }
    }

}