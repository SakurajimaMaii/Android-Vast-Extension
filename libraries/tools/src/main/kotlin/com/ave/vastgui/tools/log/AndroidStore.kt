/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.tools.log

import android.os.Build
import androidx.annotation.IntRange
import com.ave.vastgui.tools.log.base.LogScope
import com.ave.vastgui.tools.log.base.LogSp
import com.ave.vastgui.tools.log.base.fileNameTimeSdf
import com.ave.vastgui.tools.log.base.timeSdf
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.google.gson.JsonParser
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.core.base.TEXT_TYPE
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 23:36
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/store/

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
fun LogStore.Companion.android(
    fileRoot: File = File(FileMgr.appInternalFilesDir(), "log"),
    fileNamePrefix: String = AppUtils.getAppName(),
    fileNameDateSuffixSdf: SimpleDateFormat = fileNameTimeSdf,
    @IntRange(from = 0L, to = Long.MAX_VALUE) fileMaxSize: Long = 1000 * 1024L,
    storageFormat: (LogInfo) -> String = { info ->
        """
        ${timeSdf.format(info.mTime)} ${info.mLevel} [${info.mThreadName}] ${info.mTag} 
         (${info.mStackTrace?.fileName}:${info.mStackTrace?.lineNumber}) ${info.mContent}
        """.trimIndent().replace("\n", "")
    }
): AndroidStore =
    AndroidStore(fileRoot, fileNamePrefix, fileNameDateSuffixSdf, fileMaxSize, storageFormat)

/**
 * Android log store.
 *
 * @property fileRoot Folder to store log files.
 * @property fileNamePrefix File name prefix.
 * @property fileNameDateSuffixSdf Date format of file name date suffix.
 * @property fileMaxSize The size of a single log file(in bytes).
 * @property storageFormat The content format in log file.
 * @property mFileName The name of the log file.
 * @property mLogSp LogSp is used to save the log file name of the last
 *     operation.
 * @property mCurrentFile Current file which will save log.
 * @since 1.3.1
 */
class AndroidStore internal constructor(
    val fileRoot: File,
    val fileNamePrefix: String,
    val fileNameDateSuffixSdf: SimpleDateFormat,
    val fileMaxSize: Long,
    val storageFormat: (LogInfo) -> String
) : LogScope(), LogStore {

    private val mFileName: String
        get() = "${fileNamePrefix}_${fileNameDateSuffixSdf.format(System.currentTimeMillis())}.log"

    private val mLogSp by lazy { LogSp() }

    private var mCurrentFile = getCurrentFile()

    override fun store(info: LogInfo) {
        mLogScope.launch { mLogChannel.send(info) }
    }

    /**
     * Storage the [logInfo] to file.
     *
     * @since 0.5.3
     */
    private fun storage(logInfo: LogInfo) {
        val message = if (JSON_TYPE == logInfo.mType) {
            val info = LogInfo(
                logInfo.mThreadName,
                logInfo.mStackTrace,
                logInfo.mLevel,
                logInfo.mTag,
                logInfo.mTime,
                JsonParser.parseString(logInfo.mContent).asJsonObject.toString(),
                logInfo.mType,
                logInfo.mThrowable
            )
            storageFormat(info)
        } else {
            storageFormat(logInfo)
        }
        val currentNeedSize = mCurrentFile.getCurrentSize() + message.toByteArray().size.toLong()
        if (currentNeedSize > fileMaxSize) {
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
        if(!fileRoot.exists()){
            FileMgr.makeDir(fileRoot).result.onFailure { throw it }
        }
        val file = File(fileRoot, mLogSp.mCurrentFileName)
        if (!file.exists()) {
            FileMgr.saveFile(file).result
                .onSuccess { return file }.onFailure { throw it }
        }
        return file
    }

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
    } else {
        length()
    }

    override fun handleCoroutineExceptionHandler(context: CoroutineContext, exception: Throwable) {
        val thread = Thread.currentThread()
        val stackTrace =
            thread.stackTrace.findLast { it.className == AndroidStore::class.java.name }
        val logInfo = LogInfo(
            thread.name,
            stackTrace,
            LogLevel.ERROR,
            "AndroidStore",
            System.currentTimeMillis(),
            content,
            TEXT_TYPE,
            exception
        )
        storage(logInfo)
    }

    init {
        mLogScope.launch {
            while (isActive) {
                val info = mLogChannel.receive()
                storage(info)
            }
        }
    }

}