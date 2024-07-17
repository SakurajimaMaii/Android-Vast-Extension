/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.utils

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/19
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/security/encryption-utils/

object EncryptionUtils {

    /**
     * Encrypt [text] with MD5.
     *
     * @return encode text by MD5.
     */
    @JvmStatic
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "The MD5 algorithm and its successor, SHA-1, are no longer considered secure, because it is too easy to create hash collisions with them."
    )
    fun getMD5(text: String): String {
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            val digest: ByteArray = instance.digest(text.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                var hexString = Integer.toHexString(b.toInt() and 0xff)
                if (hexString.length < 2) {
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()
        } catch (exception: NoSuchAlgorithmException) {
            throw exception
        }
    }

}

/**
 * Returns a byte array of the calculated hash of [file].
 *
 * @since 1.2.0
 */
internal fun getFileMD5(file: File): String = runCatching {
    return@runCatching MessageDigest.getInstance("MD5").run {
        FileInputStream(file).use { input ->
            val dataBytes = ByteArray(1024)
            var nread: Int
            while ((input.read(dataBytes).also { nread = it }) != -1) {
                update(dataBytes, 0, nread)
            }
        }
        digest().bytesToHex()
    }
}.getOrElse {
    it.printStackTrace()
    ""
}

/** @since 1.2.0 */
internal fun ByteArray.bytesToHex(): String = StringBuilder().let { builder ->
    forEach { builder.append(String.format("%02x", it)) }
    builder.toString()
}