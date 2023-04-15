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

@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.ave.vastgui.tools.manager.filemgr

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/15
// Description: 
// Documentation:
// Reference:

/**
 * File result
 *
 * @since 0.4.0
 */
class FileResult private constructor(val value: Any) {

    val isSuccess: Boolean get() = value !is Failure

    val isFailure: Boolean get() = value is Failure

    fun successOrNull(): String? =
        when (value) {
            is Success -> value.message
            else -> null
        }

    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    companion object {
        @JvmName("success")
        fun success(message: String?): FileResult = FileResult(Success(message))

        @JvmName("failure")
        fun failure(exception: Exception): FileResult = FileResult(Failure(exception))
    }

    data class Success(val message: String?)

    data class Failure(val exception: Exception)

}