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

package com.ave.vastgui.app.viewmodel

import androidx.lifecycle.ViewModel
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.sharedpreferences.SpEncryptedExample
import com.ave.vastgui.app.theme.AppTheme
import com.ave.vastgui.tools.theme.darkColorScheme
import com.ave.vastgui.tools.theme.lightColorScheme

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/22
// Description: 
// Documentation:
// Reference:

class ThemeVM : ViewModel() {

    private val logger = mLogFactory.getLog(ThemeVM::class.java)

    /**
     * 切换主题
     */
    fun changeTheme() {
        SpEncryptedExample.isDark = !SpEncryptedExample.isDark
        logger.d("当前暗夜模式 ${SpEncryptedExample.isDark}")
        if (SpEncryptedExample.isDark) {
            AppTheme.update(darkColorScheme)
        } else {
            AppTheme.update(lightColorScheme)
        }
    }

}