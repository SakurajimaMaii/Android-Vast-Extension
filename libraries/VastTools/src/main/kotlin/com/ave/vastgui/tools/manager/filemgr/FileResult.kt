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
 * @property isSuccess True if the operation was successful, otherwise
 *     false.
 * @property isFailure True if the operation was failed, otherwise false.
 * @since 0.4.0
 */
class FileResult private constructor(val value: Any) {

    /**
     * Is success.
     *
     * @since 0.4.0
     */
    val isSuccess: Boolean get() = value !is Failure

    /**
     * Is failure.
     *
     * @since 0.4.0
     */
    val isFailure: Boolean get() = value is Failure

    /**
     * @return Information about the success of the operation, otherwise it is
     *     empty.
     * @since 0.4.0
     */
    fun successOrNull(): String? =
        when (value) {
            is Success -> value.message
            else -> null
        }

    /**
     * @return Exception about the fail of the operation, otherwise it is
     *     empty.
     * @since 0.4.0
     */
    fun exceptionOrNull(): Exception? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    companion object {
        /**
         * @since 0.4.0
         */
        @JvmName("success")
        fun success(message: String?): FileResult = FileResult(Success(message))

        /**
         * @since 0.4.0
         */
        @JvmName("failure")
        fun failure(exception: Exception): FileResult = FileResult(Failure(exception))
    }

    /**
     * Success
     *
     * @property message Information about the success of the operation.
     * @since 0.4.0
     */
    data class Success(val message: String?)

    /**
     * Failure
     *
     * @property exception Exception about the fail of the operation.
     * @since 0.4.0
     */
    data class Failure(val exception: Exception)

}