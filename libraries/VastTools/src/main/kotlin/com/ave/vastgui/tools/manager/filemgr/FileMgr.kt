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
import android.os.Environment
import androidx.security.crypto.EncryptedFile
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.helper.ContextHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/5/30
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/FileMgr/

/**
 * File path scope
 *
 * @since 0.5.1
 */
class FilePathScope internal constructor() {
    infix fun String.f(path: String) = "$this${File.separator}$path"
}

object FileMgr : FileProperty by FilePropertyMgr() {

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
     *     only. Throw IllegalStateException if shared storage is not currently
     *     available.
     * @throws IllegalStateException
     * @see [Context.getExternalFilesDir]
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    fun appExternalFilesDir(path: String?): File = if (isExternalStorageReadable()) {
        ContextHelper.getAppContext().getExternalFilesDir(path)!!
    } else throw IllegalStateException("Shared storage is not currently available.")


    /**
     * @return The File which from external storage, meant for your app's use
     *     only. Throw IllegalStateException if shared storage is not currently
     *     available.
     * @see [Context.getExternalCacheDir]
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    fun appExternalCacheDir(): File = if (isExternalStorageReadable()) {
        ContextHelper.getAppContext().externalCacheDir!!
    } else throw IllegalStateException("Shared storage is not currently available.")

    /**
     * Get file path.
     *
     * @param endWithSeparator If true, the path will end with
     *     [File.separator], false otherwise.
     * @param path file path item.
     */
    @Deprecated(
        message = "Please consider using getPath(scope: FilePathScope.() -> Unit)",
        level = DeprecationLevel.WARNING
    )
    @JvmStatic
    fun getPath(endWithSeparator: Boolean, vararg path: String): String {
        var finalPath = ""
        for (p in path) {
            finalPath += (p + File.separator)
        }
        return if (!endWithSeparator) finalPath.replaceFirst(".$".toRegex(), "") else finalPath
    }

    /**
     * Get file path
     *
     * @param scope The scope that is used to build file path.
     * @since 0.5.1
     */
    fun getPath(scope: FilePathScope.() -> String): String {
        return FilePathScope().let(scope)
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
        return try {
            if (file.createNewFile()) {
                FileResult.success("${file.name} saved successfully")
            } else {
                FileResult.failure(IllegalArgumentException("${file.name} is already exists."))
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Delete [file].
     *
     * @param file the file you want to delete.
     * @return [FileResult]
     */
    @JvmStatic
    fun deleteFile(file: File): FileResult {
        return try {
            if (file.isFile) {
                if (file.delete()) {
                    FileResult.success("${file.name} successfully deleted.")
                } else {
                    FileResult.failure(RuntimeException("${file.name} delete failed."))
                }
            } else {
                FileResult.failure(IllegalArgumentException("${file.name} is not a normal file."))
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Copy the file to the specified location, this method will not delete the
     * original file.
     *
     * @param from File original location.
     * @param to File copy destination.
     * @since 0.4.0
     */
    fun copyFile(from: File, to: File): FileResult {
        return try {
            if (!from.exists()) {
                FileResult.failure(IllegalArgumentException("${from.name} does not exist."))
            } else {
                val result = saveFile(to)
                if (result.isSuccess) {
                    val input = FileInputStream(from)
                    val out = FileOutputStream(to)
                    val bt = ByteArray(1024)
                    var c: Int
                    while (input.read(bt).also { c = it } > 0) {
                        out.write(bt, 0, c)
                    }
                    input.close()
                    out.close()
                    FileResult.success("File ${from.name} successfully copied to file ${to.name}.")
                } else {
                    result
                }
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Move the file to the specified location.
     *
     * @param file File to be moved.
     * @param destination File move destination path. For example:
     *     appInternalFilesDir().path.
     * @since 0.4.0
     */
    fun moveFile(file: File, destination: String): FileResult {
        val path = if (!destination.endsWith(File.separator)) {
            destination + File.separator
        } else destination
        return try {
            if (!file.exists()) {
                FileResult.failure(IllegalArgumentException("${file.name} does not exist."))
            } else {
                val new = File("$path${file.name}")
                val saveResult = saveFile(new)
                if (saveResult.isSuccess) {
                    val input = FileInputStream(file)
                    val out = FileOutputStream(new)
                    val bt = ByteArray(1024)
                    var c: Int
                    while (input.read(bt).also { c = it } > 0) {
                        out.write(bt, 0, c)
                    }
                    input.close()
                    out.close()
                    FileResult.success("File ${file.name} successfully move to file ${new.name}.")
                } else saveResult
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Make directory.
     *
     * @param dir The file of the directory.
     * @return [FileResult]
     */
    @JvmStatic
    fun makeDir(dir: File): FileResult {
        return try {
            if (dir.exists()) {
                FileResult.failure(IllegalArgumentException("${dir.name} is already exists."))
            } else {
                val path = if (!dir.path.endsWith(File.separator)) {
                    dir.path + File.separator
                } else dir.path
                if (File(path).mkdir()) {
                    FileResult.success("${dir.name} is created.")
                } else {
                    FileResult.failure(RuntimeException("${dir.name} create failed."))
                }
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Delete directory.
     *
     * @param dir The directory you want to delete.
     * @return Operations result.
     */
    @JvmStatic
    fun deleteDir(dir: File): FileResult {
        return try {
            when {
                !dir.exists() ->
                    FileResult.failure(IllegalArgumentException("${dir.name} is already exists."))

                null == dir.listFiles() ->
                    dir.delete()

                else -> {
                    for (f in dir.listFiles()!!) {
                        if (f.isFile) {
                            f.delete()
                        } else if (f.isDirectory) {
                            deleteDir(f)
                        }
                    }
                }
            }
            dir.delete()
            FileResult.success("${dir.name} delete successfully.")
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Copy the folder to the specified location, this method will not delete
     * the original folder.
     *
     * @param from Folder original location.
     * @param to Folder copy destination.
     * @since 0.4.0
     */
    fun copyDir(from: File, to: File): FileResult {
        return try {
            if (from.exists()) {
                val files = from.listFiles()
                val makeResult = makeDir(to)
                if (makeResult.isSuccess) {
                    val path = if (!to.path.endsWith(File.separator)) {
                        to.path + File.separator
                    } else to.path
                    for (file in files!!) {
                        if (file.isDirectory) {
                            copyDir(file, File("${path}${file.name}"))
                        } else {
                            copyFile(file, File("${path}${file.name}"))
                        }
                    }
                    FileResult.success("${from.name} copy successful.")
                } else {
                    makeResult
                }
            } else {
                FileResult.failure(IllegalArgumentException("${from.name} is not exists."))
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
    }

    /**
     * Move the folder to the specified location.
     *
     * @param dir Folder to be moved.
     * @param destination Folder move destination path.
     * @since 0.4.0
     */
    fun moveDir(dir: File, destination: String): FileResult {
        val path = if (!destination.endsWith(File.separator)) {
            destination + File.separator
        } else destination
        return try {
            if (dir.exists()) {
                val files = dir.listFiles()
                val makeResult = makeDir(File(path))
                if (makeResult.isSuccess) {
                    for (file in files!!) {
                        if (file.isDirectory) {
                            copyDir(file, File("${path}${file.name}"))
                        } else {
                            val copyResult = copyFile(file, File("${path}${file.name}"))
                            if (copyResult.isSuccess) {
                                deleteFile(file)
                            } else {
                                return copyResult
                            }
                        }
                        deleteDir(file)
                    }
                    FileResult.success("${dir.name} successful move to $destination.")
                } else {
                    makeResult
                }
            } else {
                FileResult.failure(IllegalArgumentException("${dir.name} is not exists."))
            }
        } catch (e: Exception) {
            FileResult.failure(e)
        }
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
        return try {
            if (!file.exists()) {
                FileResult.failure(IllegalArgumentException("${file.name} is already exists."))
            } else if (newName == file.name) {
                FileResult.success("${file.name} rename successfully.")
            } else {
                return if (null == file.parent) {
                    FileResult.failure(RuntimeException("${file.name} parent is null."))
                } else {
                    val newFile = File(file.parent!! + File.separator + newName)
                    if (!newFile.exists() && file.renameTo(newFile)) {
                        FileResult.success("${file.name} rename successfully.")
                    } else {
                        FileResult.failure(RuntimeException("${file.name} rename failed."))
                    }
                }
            }
        } catch (e: Exception) {
            FileResult.failure(e)
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

    /**
     * Get assets cache file.
     *
     * @param fileName The assets file name.
     * @param dir The dir where the file saved.
     * @since 0.5.1
     */
    fun getAssetsFile(fileName: String, dir: File = appInternalFilesDir()): File {
        val saveFile = File(dir, fileName)
        try {
            ContextHelper.getAppContext().assets.open(fileName).use { inputStream ->
                FileOutputStream(saveFile).use { outputStream ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                }
            }
            return saveFile
        } catch (exception: Exception) {
            throw exception
        }
    }

    /**
     * Checks if a volume containing external storage is available for read and
     * write.
     *
     * @since 0.5.1
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * Checks if a volume containing external storage is available to at least
     * read.
     *
     * @since 0.5.1
     */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}