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
import com.ave.vastgui.tools.utils.permission.Permission
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/permission/permission/

class PermissionActivity : VastVbActivity<ActivityPermissionBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestMultiplePermissions(Permission.ACCESS_BACKGROUND_LOCATION) {
            allGranted = {
                getBinding().permissionInfo.text = "权限已经被授予"
            }
            denied = {
                getBinding().permissionInfo.text = "$it 权限已经被拒绝"
            }
            noMoreAsk = {
                getBinding().permissionInfo.text = "$it 权限已经被拒绝且不会再询问"
            }
            noDeclare = {
                getBinding().permissionInfo.text = "$it \n权限没有在应用清单声明"
            }
        }
    }

}