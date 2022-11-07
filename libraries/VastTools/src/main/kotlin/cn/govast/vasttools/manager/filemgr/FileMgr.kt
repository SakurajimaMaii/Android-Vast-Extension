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

package cn.govast.vasttools.manager.filemgr

import android.content.Context
import cn.govast.vasttools.helper.ContextHelper
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/5/30
// Description: 
// Documentation:

object FileMgr {

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
    fun saveFile(file: File): FileResult {
        if (file.exists() && file.isFile) {
            file.delete()
        }
        if (!file.parentFile?.exists()!!)
            file.parentFile?.mkdirs()
        FileOutputStream(file).close()
        return if (file.exists()) FileResult.FLAG_SUCCESS else FileResult.FLAG_FAILED
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
                FileResult.FLAG_SUCCESS
            } else {
                FileResult.FLAG_FAILED
            }
        } else {
            FileResult.FLAG_FAILED
        }
    }

    /**
     * Write to the [file].
     *
     * @param file the file you want to write.
     * @param writeEventListener register a listener for writing.
     * @return [FileResult]
     */
    @JvmStatic
    fun writeFile(file: File, writeEventListener: WriteEventListener): FileResult {
        return if (!file.exists())
            FileResult.FLAG_FAILED
        else if ("txt" == getFileExtension(file)) {
            val fileWriter = FileWriter(file)
            writeEventListener.writeEvent(fileWriter)
            fileWriter.close()
            FileResult.FLAG_SUCCESS
        } else FileResult.FLAG_FAILED
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
            return FileResult.FLAG_EXISTS
        }
        val path = if (!dir.path.endsWith(File.separator)) {
            dir.path + File.separator
        } else dir.path
        if (File(path).mkdir()) {
            return FileResult.FLAG_SUCCESS
        }
        return FileResult.FLAG_FAILED
    }

    /**
     * Delete directory.
     *
     * @param file The directory you want to delete.
     * @return Operations result.
     */
    @JvmStatic
    fun deleteDir(file: File): FileResult {
        if (!file.exists()) {
            return FileResult.FLAG_FAILED
        }
        if (null == file.listFiles()) {
            file.delete()
        } else {
            for (f in file.listFiles()!!) {
                if (f.isFile) {
                    f.delete()
                } else if (f.isDirectory) {
                    deleteDir(f)
                }
            }
        }
        file.delete()
        return FileResult.FLAG_SUCCESS
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
            return FileResult.FLAG_NOT_EXISTS
        } else if (newName == file.name) {
            return FileResult.FLAG_SUCCESS
        } else {
            return if (null == file.parent) {
                FileResult.FLAG_FAILED
            } else {
                val newFile = File(file.parent!! + File.separator + newName)
                if (!newFile.exists() && file.renameTo(newFile)) {
                    FileResult.FLAG_SUCCESS
                } else {
                    FileResult.FLAG_FAILED
                }
            }
        }
    }

    /** Get the extension name of the file. */
    @JvmStatic
    fun getFileExtension(file: File): String {
        return file.name.substring(file.name.lastIndexOf(".") + 1)
    }

    /** Register a listener when write to file. */
    interface WriteEventListener {
        /**
         * Define write event.
         *
         * @param fileWriter Convenience class for writing character files.
         */
        fun writeEvent(fileWriter: FileWriter)
    }

    /** Register a listener when write result. */
    interface WriteEventResultListener {

        /** On success. */
        fun onSuccess()


        /** On failed.Default print StackTrace. */
        fun onFailed(e: Exception) {
            e.printStackTrace()
        }
    }

}