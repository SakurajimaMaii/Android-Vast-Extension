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

package com.ave.vastgui.app.theme

import com.ave.vastgui.tools.manager.filemgr.FileMgr
import org.alee.component.skin.service.IOptionFactory
import org.alee.component.skin.service.IThemeSkinOption

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/28

class OptionFactory : IOptionFactory {
    override fun defaultTheme(): Int {
        return 0
    }

    override fun requireOption(theme: Int): IThemeSkinOption? {
        return when (theme) {
            1 -> NightOption()
            else -> null
        }
    }
}

private class NightOption : IThemeSkinOption {
    override fun getStandardSkinPackPath(): LinkedHashSet<String> {
        val pathSet = LinkedHashSet<String>()
        pathSet.add("${FileMgr.appInternalFilesDir().path}/app-skin.skin")
        return pathSet
    }
}