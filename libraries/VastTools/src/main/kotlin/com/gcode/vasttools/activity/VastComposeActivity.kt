/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/6
// Description: 
// Documentation:

/**
 * If you use [Jetpack Compose](https://developer.android.com/jetpack/compose),
 * you can make your activity extends [VastComposeActivity].
 *
 * @since 0.0.9
 */
abstract class VastComposeActivity : ComponentActivity() {

    protected lateinit var mContext: Context

    protected val defaultTag: String
        get() = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

}