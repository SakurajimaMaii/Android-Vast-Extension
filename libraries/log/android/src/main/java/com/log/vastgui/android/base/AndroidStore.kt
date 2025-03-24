/*
 * Copyright 2021-2025 VastGui
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

package com.log.vastgui.android.base

import android.os.Build
import androidx.annotation.IntRange
import com.ave.vastgui.core.io.FileComparator
import com.log.vastgui.android.base.AndroidStore.Companion.LOG_EXTENSION
import com.log.vastgui.android.base.LogScope.ExceptionStorage
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.format.DEFAULT_MAX_PRINT_TIMES
import com.log.vastgui.core.format.DEFAULT_MAX_SINGLE_LOG_LENGTH
import com.log.vastgui.core.format.TableFormat
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 23:36
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/log/store/

/**
 * Android LogStore.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogStorage) {
 *         logStore = LogStore.android()
 *     }
 * }
 * ```
 *
 * @since 1.3.11
 */
@JvmOverloads
fun LogStore.Companion.android(
    fileRoot: File,
    @IntRange(from = 0L, to = Long.MAX_VALUE) fileMaxSize: Long = 1000 * 1024L,
    logFormat: LogFormat = TableFormat.LogHeader.default.let {
        TableFormat(DEFAULT_MAX_SINGLE_LOG_LENGTH, DEFAULT_MAX_PRINT_TIMES, it)
    },
    fileGenerator: (File?) -> File
): AndroidStore = AndroidStore(fileRoot, null, null, fileMaxSize, logFormat, fileGenerator)

/**
 * Android LogStore.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogStorage) {
 *         logStore = LogStore.android()
 *     }
 * }
 * ```
 *
 * @since 1.3.1
 */
@JvmOverloads
@Deprecated(
    message = "Using fileGenerator as replacement.",
    replaceWith = ReplaceWith("LogStore.android(fileRoot, fileMaxSize, logFormat){ _ -> }", "com.log.vastgui.android.base"),
    level = DeprecationLevel.WARNING
)
fun LogStore.Companion.android(
    fileRoot: File,
    fileNamePrefix: String = LOG_EXTENSION,
    fileNameDateSuffixSdf: SimpleDateFormat = AndroidStore.fileNameTimeSdf,
    @IntRange(from = 0L, to = Long.MAX_VALUE) fileMaxSize: Long = 1000 * 1024L,
    logFormat: LogFormat = TableFormat.LogHeader.default.let {
        TableFormat(DEFAULT_MAX_SINGLE_LOG_LENGTH, DEFAULT_MAX_PRINT_TIMES, it)
    }
): AndroidStore =
    AndroidStore(fileRoot, fileNamePrefix, fileNameDateSuffixSdf, fileMaxSize, logFormat)

/**
 * Android log store.
 *
 * @property fileRoot Folder to store log files.
 * @property fileNamePrefix File name prefix.
 * @property fileNameDateSuffixSdf Date format of file name date suffix.
 * @property fileMaxSize The size of a single log file(in bytes).
 * @property logFormat The log format in file.
 * @property currentFile Current file which will save log.
 * @since 1.3.1
 */
class AndroidStore internal constructor(
    private val fileRoot: File,
    private val fileNamePrefix: String?,
    private val fileNameDateSuffixSdf: SimpleDateFormat?,
    private val fileMaxSize: Long,
    override val logFormat: LogFormat,
    private val fileGenerator: (File?) -> File = { _ ->
        File(fileRoot, "${fileNamePrefix}_${fileNameDateSuffixSdf?.format(System.currentTimeMillis())}.$LOG_EXTENSION")
    }
) : LogScope(), LogStore {

    /** @since 1.3.11 */
    private val currentFile: File
        get() = getLastFileOrCreate()

    override fun store(logInfo: LogInfo) {
        logScope.launch { mLogChannel.send(logInfo) }
    }

    /**
     * Storage the [logInfo] to file.
     *
     * @since 1.3.11
     */
    private fun storage(logInfo: LogInfo) {
        val message = logFormat.format(logInfo)
        var file = currentFile
        val currentNeedSize = file.getCurrentSize() + message.toByteArray().size.toLong()
        if (currentNeedSize > fileMaxSize) {
            file = createFile()
        }
        file.storage(message)
    }

    /**
     * Get the last log file or create.
     *
     * @since 1.3.11
     */
    private fun getLastFileOrCreate(): File {
        val lastFile = getLastFile()
        if (lastFile != null)
            return lastFile
        return createFile()
    }

    /**
     * Get the last log file in [fileRoot].
     *
     * @since 1.3.11
     */
    private fun getLastFile(): File? {
        val listFiles: Array<File> = fileRoot
            .listFiles { file -> file.isFile && file.extension == LOG_EXTENSION } ?: emptyArray()
        listFiles.sortWith(FileComparator())
        return listFiles.lastOrNull()
    }

    /** @since 1.3.11 */
    private fun createFile(): File {
        val newFile = fileGenerator(getLastFile())
        if (!newFile.exists() && !newFile.createNewFile())
            throw RuntimeException("${newFile.absoluteFile} create failed!")
        return newFile
    }

    /**
     * Save the [message] to the specified file.
     *
     * @since 1.3.11
     */
    private fun File.storage(message: String) {
        BufferedWriter(FileWriter(this, true)).use { writer ->
            writer.write(message)
            writer.newLine()
        }
    }

    /**
     * Get current size of the specified file.
     *
     * @since 1.3.11
     */
    private fun File.getCurrentSize(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Files.readAttributes(toPath(), BasicFileAttributes::class.java).size()
    } else {
        length()
    }

    init {
        logScope.launch {
            while (isActive) {
                val info = mLogChannel.receive()
                storage(info)
            }
        }

        exceptionStorage = ExceptionStorage { _, exception ->
            val threadName = Thread.currentThread().name
            val info = LogInfo(threadName, exception.stackTrace[0], LogLevel.ERROR, TAG,
                System.currentTimeMillis(), exception.stackTraceToString(), exception)
            storage(info)
        }
    }

    companion object {
        const val TAG = "AndroidStore"

        const val LOG_EXTENSION = "log"

        /** @since 1.3.1 */
        internal val fileNameTimeSdf = SimpleDateFormat("yyyy-MM-dd(HH:mm:ss:SSS)", Locale.ENGLISH)
    }

}