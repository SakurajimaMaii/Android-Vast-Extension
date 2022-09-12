/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.helper

import android.app.Application
import android.content.Context
import com.gcode.vasttools.ToolsConfig


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/20
// Description: 
// Documentation:

/**
 * Help utils to get context.
 *
 * @since 0.0.9
 */
object ContextHelper{

    /**
     * @since 0.0.9
     */
    private lateinit var application: Application

    /**
     * Initialize [ContextHelper], please call [ToolsConfig.init]
     *
     * @param application the application of your app.
     * @since 0.0.9
     */
    @JvmStatic
    internal fun init(application: Application){
        if(!::application.isInitialized){
            this.application = application
        }
    }

    /**
     * Get application of the app.
     *
     * @return application object.
     * @since 0.0.9
     */
    @JvmStatic
    fun getApp():Application =
        if (!::application.isInitialized){
            throw UninitializedPropertyAccessException("application is not init, please call ToolsConfig.init()")
        } else application

    /**
     * Get applicationContext of the app.
     *
     * @return applicationContext.
     * @since 0.0.9
     */
    @JvmStatic
    fun getAppContext(): Context =
        if (!::application.isInitialized){
            throw UninitializedPropertyAccessException("application is not init, please call ToolsConfig.init()")
        } else application.applicationContext

}
