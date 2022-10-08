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

package cn.govast.vasttools.delegate.activity

import androidx.activity.ComponentActivity
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vasttools.extension.reflexViewBinding
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/6
// Description: 
// Documentation:
// Reference:

open class ActivityVbDelegate<VB : ViewBinding>(
    activity: ComponentActivity,
) : ActivityDelegate(activity) {

    // Snackbar
    private var mSnackbar by NotNUllVar<Snackbar>()

    // ViewBinding
    private val mBinding: VB by lazy {
        activity.reflexViewBinding()
    }

    override fun getSnackbar(): Snackbar {
        mSnackbar = Snackbar.make(mBinding.root, getDefaultTag(), Snackbar.LENGTH_SHORT)
        return mSnackbar
    }

    override fun getBinding(): VB {
        return mBinding
    }

}