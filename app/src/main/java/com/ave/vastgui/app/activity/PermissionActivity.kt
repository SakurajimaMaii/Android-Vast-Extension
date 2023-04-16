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

package com.ave.vastgui.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.tools.utils.permission.CAMERA
import com.ave.vastgui.tools.utils.permission.DATE
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions
import com.ave.vastgui.tools.utils.permission.requestPermission

class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        requestPermission("android.permission.READ_CALENDAR") {
            granted = {

            }
            denied = {

            }
            noMoreAsk = {

            }
        }
        requestMultiplePermissions(arrayOf(DATE, CAMERA)) {
            allGranted = {

            }
            denied = {

            }
            noMoreAsk = {

            }
        }
    }

}