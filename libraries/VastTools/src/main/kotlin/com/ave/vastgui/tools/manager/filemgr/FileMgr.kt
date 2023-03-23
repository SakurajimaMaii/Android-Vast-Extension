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

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.security.crypto.EncryptedFile
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr.FileResult.FLAG_EXISTS
import com.ave.vastgui.tools.manager.filemgr.FileMgr.FileResult.FLAG_FAILED
import com.ave.vastgui.tools.manager.filemgr.FileMgr.FileResult.FLAG_NOT_EXISTS
import com.ave.vastgui.tools.manager.filemgr.FileMgr.FileResult.FLAG_PARENT_NOT_EXISTS
import com.ave.vastgui.tools.manager.filemgr.FileMgr.FileResult.FLAG_SUCCESS
import java.io.File
import java.io.FileOutputStream

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/5/30
// Description: 
// Documentation:

object FileMgr : FileProperty by FilePropertyMgr() {

    private val mimeTypeMap: MimeTypeMap? = MimeTypeMap.getSingleton()

    /**
     * File operations result.
     *
     * @property FLAG_SUCCESS means running successful.
     * @property FLAG_PARENT_NOT_EXISTS means the parent of the file is not
     *     exist.
     * @property FLAG_EXISTS means the file or directory is exist.
     * @property FLAG_NOT_EXISTS means the file or directory is not exist.
     * @property FLAG_FAILED means running failed.
     */
    enum class FileResult {
        FLAG_SUCCESS,
        FLAG_PARENT_NOT_EXISTS,
        FLAG_EXISTS,
        FLAG_NOT_EXISTS,
        FLAG_FAILED
    }

    /**
     * @return The File which from internal storage, meant for your app's use
     *     only.
     * @see [Context.getFilesDir]
     */
    @JvmStatic
    fun appInternalFilesDir(): File = ContextHelper.getAppContext().filesDir

    /**
     * @return The File which from internal storage, meant for your app's use
     *     only.
     * @see [Context.getCacheDir]
     */
    @JvmStatic
    fun appInternalCacheDir(): File = ContextHelper.getAppContext().cacheDir

    /**
     * @return The File which from external storage, meant for your app's use
     *     only.
     * @see [Context.getExternalFilesDir]
     */
    @JvmStatic
    fun appExternalFilesDir(path: String?): File? =
        ContextHelper.getAppContext().getExternalFilesDir(path)

    /**
     * @return The File which from external storage, meant for your app's use
     *     only.
     * @see [Context.getExternalCacheDir]
     */
    @JvmStatic
    fun appExternalCacheDir(): File? = ContextHelper.getAppContext().externalCacheDir

    /**
     * Get file path.
     *
     * @param endWithSeparator If true, the path will end with
     *     [File.separator], false otherwise.
     * @param path file path item.
     */
    @JvmStatic
    fun getPath(endWithSeparator: Boolean, vararg path: String): String {
        var finalPath = ""
        for (p in path) {
            finalPath += (p + File.separator)
        }
        return if (endWithSeparator) finalPath.replaceFirst(".$".toRegex(), "") else finalPath
    }

    /**
     * Save file.
     *
     * @param file The file you want to save.
     */
    @JvmStatic
    fun saveFile(
        file: File,
    ): FileResult {
        if (file.exists() && file.isFile) {
            file.delete()
        }
        if (!file.parentFile?.exists()!!)
            file.parentFile?.mkdirs()
        getEncryptedFile(file).openFileOutput().close()
        return if (file.exists()) FLAG_SUCCESS else FLAG_FAILED
    }

    /**
     * Delete [file].
     *
     * @param file the file you want to delete.
     * @return [FileResult]
     */
    @JvmStatic
    fun deleteFile(file: File): FileResult {
        return if (file.isFile) {
            if (file.delete()) {
                FLAG_SUCCESS
            } else {
                FLAG_FAILED
            }
        } else {
            FLAG_FAILED
        }
    }

    /**
     * Write to the txt file.
     *
     * @param file the txt file you want to write.
     * @param writeEventListener register a listener for writing.
     * @return [FileResult]
     */
    @JvmStatic
    fun writeTxtFile(file: File, writeEventListener: WriteEventListener): FileResult {
        return if (!file.exists())
            FLAG_FAILED
        else if ("txt" == getExtension(file)) {
            val fileOutputStream = getEncryptedFile(file).openFileOutput()
            writeEventListener.writeEvent(fileOutputStream)
            fileOutputStream.apply {
                try {
                    flush()
                    close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            FLAG_SUCCESS
        } else FLAG_FAILED
    }

    /**
     * Make directory.
     *
     * @param dir The file of the directory.
     * @return [FileResult]
     */
    @JvmStatic
    fun makeDir(dir: File): FileResult {
        if (dir.exists()) {
            return FLAG_EXISTS
        }
        val path = if (!dir.path.endsWith(File.separator)) {
            dir.path + File.separator
        } else dir.path
        if (File(path).mkdir()) {
            return FLAG_SUCCESS
        }
        return FLAG_FAILED
    }

    /**
     * Delete directory.
     *
     * @param file The directory you want to delete.
     * @return Operations result.
     */
    @JvmStatic
    fun deleteDir(file: File): FileResult {
        when{
            !file.exists() -> FLAG_FAILED
            null == file.listFiles() -> file.delete()
            else -> {
                for (f in file.listFiles()!!) {
                    if (f.isFile) {
                        f.delete()
                    } else if (f.isDirectory) {
                        deleteDir(f)
                    }
                }
            }
        }
        file.delete()
        return FLAG_SUCCESS
    }

    /**
     * Renaming file or directory.
     *
     * @param file The file or directory you want to rename.
     * @param newName The new name.
     * @return Operations result.
     */
    @JvmStatic
    fun rename(file: File, newName: String): FileResult {
        if (!file.exists()) {
            return FLAG_NOT_EXISTS
        } else if (newName == file.name) {
            return FLAG_SUCCESS
        } else {
            return if (null == file.parent) {
                FLAG_FAILED
            } else {
                val newFile = File(file.parent!! + File.separator + newName)
                if (!newFile.exists() && file.renameTo(newFile)) {
                    FLAG_SUCCESS
                } else {
                    FLAG_FAILED
                }
            }
        }
    }

    /**
     * Get encrypted file.
     *
     * @param file
     */
    @JvmStatic
    fun getEncryptedFile(file: File): EncryptedFile {
        return EncryptedFile.Builder(
            ContextHelper.getAppContext(),
            file,
            ToolsConfig.getMasterKey(),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    /** Register a listener when write to file. */
    interface WriteEventListener {
        /**
         * Define write event.
         *
         * @param fileOutputStream File output stream.
         */
        fun writeEvent(fileOutputStream: FileOutputStream)
    }

}