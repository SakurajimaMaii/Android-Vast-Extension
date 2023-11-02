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

package com.ave.vastgui.tools.log.plugin

import android.os.Build
import com.ave.vastgui.core.extension.NotNullOrDefault
import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogInfo
import com.ave.vastgui.tools.log.base.LogLevel
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.base.LogSp
import com.ave.vastgui.tools.log.base.cutStr
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DateUtils
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogStorage.
 *
 * @property mConfiguration The configuration of the [LogStorage].
 * @property mFileNameDateSuffix The date suffix string of the log file
 *     name.
 * @property mFileName The name of the log file.
 * @property mMaxBytesSize The max bytes size of the log file.
 * @property mStorageFormat The content format in log file.
 * @property mLevel The level corresponding to the log currently being
 *     saved to the log file.
 * @property mLogSp LogSp is used to save the log file name of the last
 *     operation.
 * @property mCurrentFile Current file which will save log.
 * @since 0.5.3
 */
class LogStorage private constructor(private val mConfiguration: Configuration) {

    class Configuration internal constructor() {
        /**
         * Folder to store log files, default value is
         * [FileMgr.appInternalFilesDir].
         *
         * @since 0.5.3
         */
        var fileRoot by NotNullOrDefault(FileMgr.appInternalFilesDir())

        /**
         * File name prefix. Default value is the app name.
         *
         * @see AppUtils.getAppName
         * @since 0.5.3
         */
        var fileNamePrefix by NotNullOrDefault(AppUtils.getAppName())

        /**
         * File max size
         *
         * @since 0.5.3
         */
        var fileMaxSize by NotNullOrDefault(1024L)

        /**
         * The log level that will be recorded
         *
         * @see LogLevel
         * @since 0.5.3
         */
        var level by NotNullOrDefault(LogLevel.DEBUG)

        /**
         * Current date format.
         *
         * @see DateUtils
         * @since 0.5.3
         */
        var currentDateFormat by NotNullOrDefault(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS)

        /**
         * Storage format
         *
         * @since 0.5.3
         */
        var storageFormat: (Time, LogLevel, String) -> String by
        NotNullOrDefault { time, logLevel, s -> "$time || $logLevel || $s" }
    }

    /**
     * Time of the log.
     *
     * @since 0.5.3
     */
    data class Time internal constructor(private val mills: Long, private val format: String) {
        override fun toString(): String =
            SimpleDateFormat(format, Locale.getDefault()).format(mills)
    }

    private val mFileNameDateSuffix = System.currentTimeMillis().toString()

    private val mFileName: String
        get() = "${mConfiguration.fileNamePrefix}_${mFileNameDateSuffix}.log"

    private val mMaxBytesSize: Long
        get() = mConfiguration.fileMaxSize

    private val mStorageFormat
        get() = mConfiguration.storageFormat

    private val mLevel
        get() = mConfiguration.level

    private val mLogSp by lazy { LogSp() }

    private var mCurrentFile = getCurrentFile()

    /**
     * Storage the [logInfo] to file.
     *
     * @since 0.5.3
     */
    internal fun storage(logInfo: LogInfo) {
        if (logInfo.mLevel < mLevel) return
        val time = logInfo.getCurrentTime()
        storage(time, logInfo.mLevel, "Thread: ${logInfo.mThreadName}")
        storage(time, logInfo.mLevel, "${logInfo.mMethodStackTrace}")
        if (logInfo.mContentLength <= logInfo.mTraceLength) {
            storage(time, logInfo.mLevel, logInfo.mContent)
        } else {
            var bytes = logInfo.mContent.toByteArray()
            while (logInfo.mTraceLength * 4 < bytes.size) {
                val subStr = bytes.cutStr(logInfo.mTraceLength)
                storage(time, logInfo.mLevel, String.format("%s", subStr))
                bytes = bytes.copyOfRange(subStr.toByteArray().size, bytes.size)
            }
            storage(time, logInfo.mLevel, String.format("%s", String(bytes)))
        }
        logInfo.throwable?.apply {
            storage(time, logInfo.mLevel, "$this")
            for (item in this.stackTrace) {
                storage(time, logInfo.mLevel, "  at $item")
            }
        }
    }

    /**
     * Storage the [logInfo] to file.
     *
     * @since 0.5.3
     */
    internal fun storageJson(logInfo: LogInfo) {
        if (logInfo.mLevel < mLevel) return
        val time = logInfo.getCurrentTime()
        storage(time, logInfo.mLevel, "Thread: ${logInfo.mThreadName}")
        storage(time, logInfo.mLevel, "${logInfo.mMethodStackTrace}")
        for (line in logInfo.mContent.split("\n")) {
            storage(time, logInfo.mLevel, line)
        }
    }

    /** @since 0.5.3 */
    @Throws(RuntimeException::class)
    private fun storage(time: Time, level: LogLevel, content: String) {
        val message = mStorageFormat(time, level, content)
        val currentNeedSize = mCurrentFile.getCurrentSize() + message.toByteArray().size.toLong()
        if (currentNeedSize > mMaxBytesSize) {
            mCurrentFile = getCurrentFile(true)
        }
        mCurrentFile.storage(message)
    }

    /**
     * Get file.
     *
     * @since 0.5.3
     */
    private fun getCurrentFile(appendFile: Boolean = false): File {
        if (mLogSp.mCurrentFileName == LogSp.DEFAULT_FILE_NAME) {
            mLogSp.mCurrentFileName = mFileName
        }
        if (appendFile) {
            mLogSp.mCurrentFileName = mFileName
        }
        val file = File(mConfiguration.fileRoot, mLogSp.mCurrentFileName)
        if (!file.exists()) {
            FileMgr.saveFile(file).result
                .onSuccess { return file }.onFailure { throw it }
        }
        return file
    }

    /**
     * Get current save time for the log.
     *
     * @since 0.5.3
     */
    private fun LogInfo.getCurrentTime() = Time(mTimeMillis, mConfiguration.currentDateFormat)

    /**
     * Save the [message] to the specified file.
     *
     * @since 0.5.3
     */
    private fun File.storage(message: String) {
        val fileWriter = FileWriter(this, true)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(message)
        bufferedWriter.newLine()
        bufferedWriter.close()
    }

    /**
     * Get current size of the specified file.
     *
     * @since 0.5.3
     */
    private fun File.getCurrentSize(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Files.readAttributes(toPath(), BasicFileAttributes::class.java).size()
    } else length()

    companion object : LogPlugin<Configuration, LogStorage> {

        override val key: String
            get() = LogStorage::class.java.simpleName

        override fun install(plugin: LogStorage, scope: LogUtil) {
            scope.mLogStorage = plugin
        }

        override fun configuration(config: Configuration.() -> Unit): LogStorage {
            val configuration = Configuration().also(config)
            return LogStorage(configuration)
        }

    }

}