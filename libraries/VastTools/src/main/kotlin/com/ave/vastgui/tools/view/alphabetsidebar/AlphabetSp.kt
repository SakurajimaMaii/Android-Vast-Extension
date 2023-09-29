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

package com.ave.vastgui.tools.view.alphabetsidebar

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
internal object AlphabetSp {

    private val mSp by lazy {
        SpEncrypted
            .getInstance("${AppUtils.getPackageName("")}.${AlphabetSp::class.java.simpleName}")
            .getSharedPreferences()
    }

    var Favorite by mSp.int(0)
    var A by mSp.int(0)
    var B by mSp.int(0)
    var C by mSp.int(0)
    var D by mSp.int(0)
    var E by mSp.int(0)
    var F by mSp.int(0)
    var G by mSp.int(0)
    var H by mSp.int(0)
    var I by mSp.int(0)
    var J by mSp.int(0)
    var K by mSp.int(0)
    var L by mSp.int(0)
    var M by mSp.int(0)
    var N by mSp.int(0)
    var O by mSp.int(0)
    var P by mSp.int(0)
    var Q by mSp.int(0)
    var R by mSp.int(0)
    var S by mSp.int(0)
    var T by mSp.int(0)
    var U by mSp.int(0)
    var V by mSp.int(0)
    var W by mSp.int(0)
    var X by mSp.int(0)
    var Y by mSp.int(0)
    var Z by mSp.int(0)
    var Other by mSp.int(0)

}