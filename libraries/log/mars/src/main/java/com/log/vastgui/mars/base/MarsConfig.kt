/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.mars.base

import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/1 0:16
// Description: 
// Documentation:
// Reference:

internal object MarsConfig {

    init {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
    }

    /**
     * File writing modes are divided into asynchronous and synchronous. The
     * Release version must use [MarsWriteMode.Async]. The Debug version
     * can be both, but there may be lags when using [MarsWriteMode.Sync].
     *
     * @since 1.3.4
     */
    var mode = MarsWriteMode.Sync

    /**
     * Log directory. Please provide a separate directory for writing logs. Do
     * not put other files except log files into this directory, otherwise they
     * may be cleaned up by the automatic log cleaning function.
     *
     * @since 1.3.4
     */
    lateinit var logdir: File

    /**
     * Cache directory. When [logdir] is not writable, it will be written
     * to this directory. It is optional. If not selected, please provide
     * "". If you want to provide it, it is recommended to provide the
     * /data/data/packname/files/log directory of the application.
     *
     * @since 1.3.4
     */
    lateinit var cache: File

    /**
     * File name prefix. For example, if the value is log, the generated file
     * name is: log_20170102.xlog.
     *
     * @since 1.3.4
     */
    var namePrefix = "log"

    /**
     * Whether to save log in one file every day.
     *
     * @since 1.3.4
     */
    var singleLogFileEveryday = true

    /**
     * The maximum size of a single log file. By default, the current day's
     * logs are written to a file. By changing [singleLogFileMaxSize] you
     * can Split the day's log into multiple files, each file size is
     * [singleLogFileMaxSize], [singleLogFileMaxSize] defaults to 1MB , means
     * not to split the log into multiple files. It is recommended that each
     * file should not exceed 10M.
     *
     * @since 1.3.4
     */
    var singleLogFileMaxSize = (1024 * 1024).toLong()
        set(value) {
            field = value.coerceIn(1024 * 1024, (10 * 1024 * 1024).toLong())
        }

    /**
     * The maximum storage time of a single log file. Minimum 1 day, default
     * time 10 days.
     *
     * @since 1.3.4
     */
    var singleLogFileStoreTime = 10L
        set(value) {
            field = value.coerceAtLeast(1)
        }

    /**
     * The number of days to cache. Under normal circumstances, just fill in 0.
     * Non-0 means that the log file will be stored in the [cache] directory
     * first, and the log will be moved from [cache] to [default] after
     * [singleLogFileCacheDays] days.
     *
     * @since 1.3.4
     */
    var singleLogFileCacheDays = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    /**
     * The pub_key used for encryption, please refer to the Xlog encryption
     * guide for details.
     *
     * @since 1.3.4
     */
    var pubKey = ""

    /**
     * Complete configuration.
     *
     * @since 1.3.4
     */
    fun init() {
        Xlog().apply {
            Log.setLogImp(this)
            if (singleLogFileEveryday) {
                setMaxFileSize(0L, 0)
            } else {
                setMaxFileSize(0L, singleLogFileMaxSize)
            }
            setMaxAliveTime(0L, singleLogFileStoreTime)
            // The printing of logs is determined by a unified switch.
            Log.setConsoleLogOpen(true)
            Log.appenderOpen(
                Log.LEVEL_ALL,
                mode.value,
                cache.path,
                logdir.path,
                namePrefix,
                singleLogFileCacheDays
            )
        }
    }

    /** @since 1.3.4 */
    fun close() {
        Log.appenderClose()
    }

}