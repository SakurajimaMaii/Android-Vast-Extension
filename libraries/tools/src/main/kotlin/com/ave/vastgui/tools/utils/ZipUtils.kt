package com.ave.vastgui.tools.utils

import android.util.Log
import androidx.annotation.WorkerThread
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

const val TAG = "ZipUtils"

/**
 * Compress files under [src] to [dest].
 *
 * @since 1.5.0
 */
@WorkerThread
fun zip(src: File, dest: File): Boolean = runCatching {
    check("zip" == FileMgr.getExtension(dest)) { "Currently only zip are supported" }
    ZipOutputStream(FileOutputStream(dest)).use { out ->
        // src is file
        if (src.isFile) {
            zipFileOrDirectory(out, src, "")
        }
        // src is directory
        else {
            src.listFiles()?.forEach {
                zipFileOrDirectory(out, it, "")
            }
        }
    }
    return true
}.onFailure {
    Log.e(TAG, it.stackTraceToString())
}.getOrDefault(false)

/**
 * @since 1.5.0
 */
@WorkerThread
@Throws(IOException::class)
private fun zipFileOrDirectory(out: ZipOutputStream, fileOrDirectory: File, curPath: String) {
    if (fileOrDirectory.isFile) {
        FileInputStream(fileOrDirectory).use { input ->
            val entry = ZipEntry("$curPath${fileOrDirectory.name}")
            out.putNextEntry(entry)
            input.copyTo(out)
            out.closeEntry()
        }
    } else {
        fileOrDirectory.listFiles()?.forEach {
            zipFileOrDirectory(out, it, "$curPath${fileOrDirectory.name}${File.separator}")
        }
    }
}

/**
 * Extract the file specified by [src] to the [dest] directory.
 *
 * @since 1.5.0
 */
@Throws(IOException::class, RuntimeException::class, IllegalArgumentException::class)
@WorkerThread
fun unzip(src: File, dest: File) {
    check(dest.isDirectory) { "${dest.name} must be a directory and exists." }
    ZipFile(src.path).use {
        var zipEntry: ZipEntry
        val entries = it.entries()
        while (entries.hasMoreElements()) {
            zipEntry = cast(entries.nextElement())
            if (zipEntry.isDirectory) {
                val directory = File(dest, zipEntry.name)
                if (!directory.exists()) {
                    FileMgr.makeDir(directory).result.onFailure { ex -> throw ex }
                }
            } else {
                val entryName = zipEntry.name
                val index = entryName.lastIndexOf("/")
                if (index != -1) {
                    val df = File(dest, entryName.substring(0, index))
                    if (!df.exists()) {
                        FileMgr.makeDirs(df).result.onFailure { ex -> throw ex }
                    }
                }
                val out = FileOutputStream(File(dest, zipEntry.name))
                it.getInputStream(zipEntry).copyTo(out)
            }
        }
    }
}