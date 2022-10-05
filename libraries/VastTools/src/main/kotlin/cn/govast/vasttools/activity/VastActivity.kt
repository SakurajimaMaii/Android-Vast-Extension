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

package cn.govast.vasttools.activity

import androidx.appcompat.app.AppCompatActivity
import cn.govast.vasttools.delegate.ActivityDelegate

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:20
// Description: BaseVastActivity.

/**
 * The parent class for [VastVmActivity] , [VastVbActivity] ,
 * [VastVbVmActivity].
 *
 * @since 0.0.9
 */
sealed class VastActivity : AppCompatActivity() {

    private val mActivityDelegate by lazy {
        createActivityDelegate()
    }

    protected fun getContext() = mActivityDelegate.getContext()

    protected fun getDefaultTag(): String = this.javaClass.simpleName

    protected fun enableActionBar(enable: Boolean) {
        mActivityDelegate.enableActionBar(enable)
    }

    protected fun enableFullScreen(enable: Boolean) {
        mActivityDelegate.enableFullScreen(enable)
    }

    protected fun getBaseActivity() {
        mActivityDelegate.getBaseActivity()
    }

    protected fun getSnackbar() = mActivityDelegate.getSnackbar()

    protected fun getRequestBuilder() = mActivityDelegate.getRequestBuilder()

    protected fun createMainScope() = mActivityDelegate.createMainScope()

    protected abstract fun createActivityDelegate(): ActivityDelegate

}