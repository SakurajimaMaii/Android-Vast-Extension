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
package com.ave.vastgui.app.activity.anim

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.databinding.ActivityFlipAnimBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.anim.flipXAnimation

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/animation/flip-animation/

class FlipAnimActivity : VastVbActivity<ActivityFlipAnimBinding>() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().textview1.setOnLongClickListener {
            flipXAnimation(getBinding().textview1, getBinding().textview2)
            false
        }
        getBinding().textview2.setOnLongClickListener {
            flipXAnimation(getBinding().textview1, getBinding().textview2)
            false
        }
    }

}