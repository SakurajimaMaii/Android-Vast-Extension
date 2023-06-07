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
import com.ave.vastgui.app.databinding.ActivityPermissionBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.utils.permission.DATE
import com.ave.vastgui.tools.utils.permission.SMS
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions
import com.ave.vastgui.tools.utils.permission.requestPermission

class PermissionActivity : VastVbActivity<ActivityPermissionBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(SMS) {
            granted = {

            }
            denied = {

            }
            noMoreAsk = {

            }
        }
        requestMultiplePermissions(arrayOf(DATE, SMS)) {
            allGranted = {

            }
            denied = {

            }
            noMoreAsk = {
                LogUtils.d(getDefaultTag(), it.toString())
            }
        }
    }

}