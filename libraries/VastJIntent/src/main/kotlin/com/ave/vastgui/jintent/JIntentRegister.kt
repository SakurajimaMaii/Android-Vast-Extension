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

package com.ave.vastgui.jintent

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/11
// Description: 
// Documentation:
// Reference:

/**
 * JIntent register.
 *
 * @since 0.0.1
 */
class JIntentRegister : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        injectIntent(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    /**
     * Inject parameters into the jumping [activity].
     *
     * @param activity Target Jump Activity.
     * @param savedInstanceState If the [activity] is being re-initialized
     *     after previously being shut down then this Bundle contains the data
     *     it most recently supplied in.
     * @since 0.0.1
     */
    private fun injectIntent(activity: Activity, savedInstanceState: Bundle?) {
        val bundle = if (savedInstanceState == null) {
            val intent = activity.intent ?: return
            intent.extras
        } else savedInstanceState
        val method = JIntentClassFinder.findJIntentClass(activity)
            ?.getDeclaredMethod("inject", Activity::class.java, Bundle::class.java)
        method?.invoke(null, activity, bundle)
    }

    companion object {
        /**
         * Start activity.
         *
         * @since 0.0.1
         */
        fun startActivity(context: Context, intent: Intent) {
            context.startActivity(intent)
        }
    }

}