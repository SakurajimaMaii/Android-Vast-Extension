/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.view.alphabetsidebar

import android.content.SharedPreferences
import com.ave.vastgui.tools.sharedpreferences.ISharedPreferencesOwner
import com.ave.vastgui.tools.sharedpreferences.SpEncrypted
import com.ave.vastgui.tools.sharedpreferences.int
import com.ave.vastgui.tools.utils.AppUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/29
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/alphabetsidebar/alphabetsidebar/

/**
 * Alphabet SharedPreferences.
 *
 * Used to save target index of the element in [AlphabetSideBar.mAlphabet].
 *
 * @since 0.5.4
 */
internal object AlphabetSp : ISharedPreferencesOwner {

    override val name: String =
        "${AppUtils.getPackageName("")}.${AlphabetSp::class.java.simpleName}"
    override val kv: SharedPreferences by lazy {
        SpEncrypted(name).getSharedPreferences()
    }

    var Favorite by int(0)
    var A by int(0)
    var B by int(0)
    var C by int(0)
    var D by int(0)
    var E by int(0)
    var F by int(0)
    var G by int(0)
    var H by int(0)
    var I by int(0)
    var J by int(0)
    var K by int(0)
    var L by int(0)
    var M by int(0)
    var N by int(0)
    var O by int(0)
    var P by int(0)
    var Q by int(0)
    var R by int(0)
    var S by int(0)
    var T by int(0)
    var U by int(0)
    var V by int(0)
    var W by int(0)
    var X by int(0)
    var Y by int(0)
    var Z by int(0)
    var Other by int(0)

}