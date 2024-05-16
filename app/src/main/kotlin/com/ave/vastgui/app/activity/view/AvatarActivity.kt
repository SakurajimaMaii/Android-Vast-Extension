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
import android.view.ViewGroup.LayoutParams
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityAvatarBinding
import com.ave.vastgui.tools.view.avatar.Avatar
import com.ave.vastgui.tools.view.avatar.AvatarGroup
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/avatar/avatar/
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/avatar/avatargroup/

class AvatarActivity : ComponentActivity(R.layout.activity_avatar) {

    val mBinding by viewBinding(ActivityAvatarBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mAvatar = Avatar(this).apply {
            setAvatar(R.drawable.img_avatar_sample_2)
        }

        mBinding.avatarGroupStart.addView(
            mAvatar, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        )

        mBinding.avatarGroupEnd.setOverlapFrom(AvatarGroup.END)
    }

}