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

@file:JvmName("FileUtils")

package com.ave.vastgui.tools.io

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.security.crypto.EncryptedFile
import com.ave.vastgui.core.ResultCompat
import com.ave.vastgui.core.onFailure
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.os.extension.fromApi24
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/5/30
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/app-data-and-files/file-manager/file-mgr/

// region Basic CURD and extension function

/**
 * File path scope
 *
 * @since 0.5.1
 */
class FilePathScope internal constructor() {
    infix fun String.f(path: String) = "$this${File.separator}$path"
}

/** @since 1.5.2 */
enum class Policy {
    /**
     * Overwrite existing destination file.
     *
     * @since 1.5.2
     */
    OVERWRITE,

    /**
     * Skip and return existing destination file.
     *
     * @since 1.5.2
     */
    SKIP,

    /**
     * Throws an exception directly.
     *
     * @since 1.5.2
     */
    EXCEPTION
}

/**
 * @return The File which from internal storage, meant for your app's use
 * only.
 * @see [Context.getFilesDir]
 * @since 1.5.2
 */
fun appInternalFilesDir(): File = ContextHelper.getAppContext().filesDir

/**
 * @return The File which from internal storage, meant for your app's use
 * only.
 * @see [Context.getCacheDir]
 * @since 1.5.2
 */
fun appInternalCacheDir(): File = ContextHelper.getAppContext().cacheDir

/**
 * @return The File which from external storage, meant for your app's use
 * only. Throw IllegalStateException if shared storage is not currently
 * available.
 * @throws IllegalStateException
 * @see [Context.getExternalFilesDir]
 * @since 1.5.2
 */
@Throws(IllegalStateException::class)
fun appExternalFilesDir(path: String?): File {
    if (isExternalStorageReadable()) {
        return ContextHelper.getAppContext().getExternalFilesDir(path)!!
    } else {
        throw IllegalStateException("Shared storage is not currently available.")
    }
}

/**
 * @return The File which from external storage, meant for your app's use
 * only. Throw IllegalStateException if shared storage is not currently
 * available.
 * @see [Context.getExternalCacheDir]
 * @since 1.5.2
 */
@Throws(IllegalStateException::class)
fun appExternalCacheDir(): File {
    if (isExternalStorageReadable()) {
        return ContextHelper.getAppContext().externalCacheDir!!
    } else {
        throw IllegalStateException("Shared storage is not currently available.")
    }
}

/**
 * Get file path.
 *
 * ```kotlin
 * // /data/user/0/{PackageName}/files/dir1/dir2
 * val path = getPath { appInternalFilesDir().path f "dir1" f "dir2" }
 * ```
 *
 * @param scope The scope that is used to build file path.
 * @since 1.5.2
 */
fun getPath(scope: FilePathScope.() -> String): String {
    return FilePathScope().let(scope)
}

/**
 * Create new file with [policy].
 *
 * @since 1.5.2
 */
fun File.mkFile(policy: Policy = Policy.EXCEPTION): ResultCompat<File> {
    if (exists()) {
        if (policy == Policy.SKIP) {
            return ResultCompat.success(this)
        } else if (policy == Policy.EXCEPTION) {
            return ResultCompat.failure(RuntimeException("$absolutePath already exists."))
        } else if (policy == Policy.OVERWRITE) {
            val delResult = destroy()
            if (delResult.isFailure) {
                return ResultCompat.failure(RuntimeException(delResult.exceptionOrNull()))
            }
        }
    }

    return try {
        if (createNewFile()) {
            ResultCompat.success(this)
        } else {
            ResultCompat.failure(RuntimeException("$absolutePath creation failed."))
        }
    } catch (ex: Exception) {
        ResultCompat.failure(RuntimeException("$absolutePath creation failed.", ex))
    }
}

/**
 * Creates the dir , including any necessary but nonexistent parent
 * directories.
 *
 * @since 1.5.2
 */
fun File.mkDirs(policy: Policy = Policy.EXCEPTION): ResultCompat<File> {
    if (exists()) {
        when (policy) {
            Policy.SKIP -> return ResultCompat.success(this)
            Policy.EXCEPTION ->
                return ResultCompat.failure(RuntimeException("$absolutePath exists."))

            Policy.OVERWRITE -> {
                val delResult = destroy()
                if (delResult.isFailure) {
                    return ResultCompat.failure(RuntimeException(delResult.exceptionOrNull()))
                }
            }
        }
    }

    return try {
        if (mkdirs()) {
            ResultCompat.success(this)
        } else {
            ResultCompat.failure(RuntimeException("$absolutePath create failed."))
        }
    } catch (ex: Exception) {
        ResultCompat.failure(RuntimeException("$absolutePath create failed.", ex))
    }
}

/**
 * Delete file.
 *
 * @see deleteRecursively
 * @since 1.5.2
 */
fun File.destroy(): ResultCompat<Unit> {
    return if (deleteRecursively()) {
        ResultCompat.success(Unit)
    } else {
        ResultCompat.failure(RuntimeException("$absolutePath delete failed."))
    }
}

/**
 * Copies this file to the given [dst] file.
 *
 * @return If the [policy] is [Policy.SKIP] ，[ResultCompat] return this
 * file as success value.
 * @see copyTo
 * @since 1.5.2
 */
fun File.copyFile(dst: File, policy: Policy = Policy.EXCEPTION, bufferSize: Int = DEFAULT_BUFFER_SIZE): ResultCompat<File> {
    try {
        val file = copyTo(dst, policy == Policy.OVERWRITE, bufferSize)
        return ResultCompat.success(file)
    } catch (ex: Exception) {
        if (policy == Policy.SKIP) return ResultCompat.success(this)
        return ResultCompat.failure(RuntimeException(ex))
    }
}

/**
 * Copy to the directory [dst].
 *
 * @since 1.5.2
 */
fun File.copyDirs(dst: File, policy: Policy = Policy.EXCEPTION): ResultCompat<File> {
    try {
        if (dst.startsWith(this)) {
            throw RuntimeException("Copy $absolutePath to ${dst.absolutePath} can cause infinite loop replication.")
        }

        copyRecursively(dst, policy == Policy.OVERWRITE) { file, ex ->
            if (ex is FileAlreadyExistsException && policy == Policy.SKIP) {
                return@copyRecursively OnErrorAction.SKIP
            }
            throw RuntimeException("${file.absolutePath} copy failed.", ex)
        }

        return ResultCompat.success(File(dst, name))
    } catch (ex: Exception) {
        return ResultCompat.failure(ex)
    }
}

/**
 * Move [src] to the directory [dst].
 *
 * @param policy If there is a file with the same name as [src] in [dst],
 * it will be processed according to [policy].
 * @since 1.5.2
 */
fun File.move(dst: File, policy: Policy = Policy.EXCEPTION): ResultCompat<File> {
    val copyResult = if (isFile) {
        copyFile(dst, policy)
    } else if (isDirectory) {
        copyDirs(dst, policy)
    } else {
        throw IllegalArgumentException("This file should be a file or a directory.")
    }

    if (copyResult.isFailure) {
        return ResultCompat.failure(RuntimeException(copyResult.exceptionOrNull()))
    }

    val delResult = destroy()
    if (delResult.isFailure) {
        return ResultCompat.failure(RuntimeException(delResult.exceptionOrNull()))
    }

    return copyResult
}

/**
 * Rename file or directory.
 *
 * @param newName The new name.
 * @since 1.5.2
 */
fun File.rename(newName: String): ResultCompat<File> {
    if (newName == name) {
        return ResultCompat.success(this)
    } else if (!isFile) {
        return ResultCompat.failure(IllegalArgumentException("$absolutePath isn't a normal file or doesn't exist."))
    } else {
        val newFile = File(parent, newName)
        return if (renameTo(newFile)) {
            ResultCompat.success(newFile)
        } else {
            ResultCompat.failure(RuntimeException("$absolutePath rename failed."))
        }
    }
}

/**
 * Get encrypted file.
 *
 * @since 1.5.2
 */
fun File.encrypted(): EncryptedFile {
    return EncryptedFile
        .Builder(ContextHelper.getAppContext(), this, ToolsConfig.getMasterKey(), EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB)
        .build()
}

/**
 * Save [fileName] in assets to the folder specified by [dir].
 *
 * @since 1.5.2
 */
fun getAssetsFile(fileName: String, dir: File = appInternalFilesDir(), bufferSize: Int = DEFAULT_BUFFER_SIZE): ResultCompat<File> {
    try {
        val duplicate = File(dir, fileName)
        val context = ContextHelper.getAppContext()
        context.assets.open(fileName).use { input ->
            FileOutputStream(duplicate).use { output ->
                input.copyTo(output, bufferSize)
            }
        }
        return ResultCompat.success(duplicate)
    } catch (exception: Exception) {
        return ResultCompat.failure(RuntimeException("Get assets file failed.", exception))
    }
}

/**
 * Checks if a volume containing external storage is available for read and
 * write.
 *
 * @since 1.5.2
 */
fun isExternalStorageWritable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}

/**
 * Checks if a volume containing external storage is available to at least
 * read.
 *
 * @since 1.5.2
 */
fun isExternalStorageReadable(): Boolean {
    return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}

/**
 * Get the mimetype of the file, [fallback] otherwise. For more
 * information, please refer to
 * [How to determine MIME type of file in android?](https://stackoverflow.com/questions/8589645/how-to-determine-mime-type-of-file-in-android)
 *
 * @since 1.5.2
 */
fun File.mimeType(fallback: String? = null): String {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase(Locale.ROOT))
        ?: fallback
        ?: throw RuntimeException("Can't get the mine type of $absolutePath")
}

/**
 * Get [Uri] of the file.
 *
 * @param authority The authority of a [FileProvider] defined
 * in a <provider> element in your app's manifest.(Start with
 * [Build.VERSION_CODES.N]). Please register a provider in
 * AndroidManifest.xml. For example:
 * ```xml
 * <provider
 *      android:name="androidx.core.content.FileProvider"
 *      android:authorities="${applicationId}"
 *      android:exported="false"
 *      android:grantUriPermissions="true">
 *      <meta-data
 *          android:name="android.support.FILE_PROVIDER_PATHS"
 *          android:resource="@xml/file_paths" />
 * </provider>
 * ```
 *
 * @since 1.5.2
 */
fun File.uri(authority: String = ""): Uri {
    fromApi24 {
        return FileProvider.getUriForFile(ContextHelper.getAppContext(), authority, this)
    }
    return Uri.fromFile(this)
}

// endregion