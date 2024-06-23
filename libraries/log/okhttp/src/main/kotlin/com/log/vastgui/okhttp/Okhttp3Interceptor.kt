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

package com.log.vastgui.okhttp

import com.ave.vastgui.core.extension.NotNullOrDefault
import com.ave.vastgui.core.extension.SingletonHolder
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.okhttp.base.ContentLevel
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.promisesBody
import okio.Buffer
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Locale
import java.util.concurrent.TimeUnit

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/24 0:01
// Description: Log interceptor of Okhttp3.
// Documentation: https://ave.entropy2020.cn/documents/log/log-okhttp/usage/
// Reference: https://square.github.io/okhttp/features/interceptors/

/**
 * Log interceptor of Okhttp3.
 *
 * The following is an example of usage, you can click
 * [link](https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/app/src/main/kotlin/com/ave/vastgui/app/net/OpenApi.kt)
 * to view the complete code.
 *
 * ```kotlin
 * // OpenApi.kt
 * class OpenApi : RequestBuilder("https://api.apiopen.top") {
 *     ...
 *
 *     override fun okHttpConfiguration(builder: OkHttpClient.Builder) {
 *         super.okHttpConfiguration(builder)
 *         mLogger = mLogFactory.getLog(OpenApi::class.java)
 *         mOkhttp3Interceptor = Okhttp3Interceptor.getInstance(mLogger)
 *         builder.addInterceptor(mOkhttp3Interceptor)
 *     }
 * }
 * ```
 *
 * @see <img
 *     src="https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/log/okhttp/image/log.png?raw=true">
 * @since 1.3.3
 */
class Okhttp3Interceptor private constructor(private val logger: LogCat) : Interceptor {
    /**
     * The filter function allows you to filter log messages for requests
     * matching the specified predicate. Return true directly by default.
     *
     * @since 1.3.3
     */
    var filter: ((Request) -> Boolean) by NotNullOrDefault { _ -> true }

    /**
     * Different levels represent different printing contents.
     *
     * @since 1.3.3
     */
    var contentLevel: ContentLevel by NotNullOrDefault(ContentLevel.ALL)

    /**
     * The [requestLevel] function allows you to set the log level it prints
     * based on [Request], and returns [LogLevel.DEBUG] by default.
     *
     * @since 1.3.3
     */
    var requestLevel: ((Request) -> LogLevel) by NotNullOrDefault { _ -> LogLevel.DEBUG }

    /**
     * The [responseLevel] function allows you to set the log level it prints
     * based on [Request], and returns [LogLevel.DEBUG] by default.
     *
     * @since 1.3.3
     */
    var responseLevel: ((Response) -> LogLevel) by NotNullOrDefault { _ -> LogLevel.DEBUG }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        if (!filter(request) || ContentLevel.NONE == contentLevel) {
            return chain.proceed(request)
        }
        dealRequestLog(request, chain.connection())
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.e(logger.mDefaultTag, "<-- HTTP FAILED", e)
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        return dealResponseLog(response, tookMs)
    }

    /**
     * Deal request log.
     *
     * @since 1.3.3
     */
    private fun dealRequestLog(request: Request, connection: Connection?) {
        val requestLog = StringBuilder()
        val requestBody = request.body
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        try {
            if (contentLevel.info) {
                requestLog.appendLine("--> ${request.method} ${request.url} $protocol")
            }
            if (contentLevel.headers) {
                if (null != requestBody) {
                    if (requestBody.contentType() != null) {
                        requestLog.appendLine("\tContent-Type: ${requestBody.contentType()}")
                    }
                    if (requestBody.contentLength() != -1L) {
                        requestLog.appendLine("\tContent-Length: ${requestBody.contentLength()}")
                    }
                }
                val headers = request.headers
                headers.forEachIndexed { index, _ ->
                    val name = headers.name(index)
                    if ("Content-Type" != name && "Content-Length" != name) {
                        requestLog.appendLine("\t$name: ${headers.value(index)}")
                    }
                }
            }
            if (contentLevel.body && null != requestBody) {
                if (isPlaintext(requestBody.contentType())) {
                    requestLog.bodyToString(request)
                } else {
                    requestLog.appendLine("\tbody: maybe [binary body], omitted!")
                }
            }
        } catch (e: Exception) {
            logger.e(
                logger.mDefaultTag,
                "Exception encountered while processing request information",
                e
            )
        } finally {
            requestLog.append("--> END ${request.method}")
            log(requestLevel(request), logger.mDefaultTag, requestLog.toString())
        }
    }

    /**
     * Deal response log.
     *
     * @since 1.3.3
     */
    private fun dealResponseLog(response: Response, tookMs: Long): Response {
        val requestLog = StringBuilder()
        val builder = response.newBuilder()
        val clone: Response = builder.build()
        var responseBody = clone.body
        try {
            if (contentLevel.info) {
                requestLog
                    .appendLine("<-- ${clone.code} ${clone.message} ${clone.request.url} (${tookMs}ms)")
            }
            if (contentLevel.headers) {
                val headers = clone.headers
                headers.forEachIndexed { index, _ ->
                    requestLog.appendLine("\t ${headers.name(index)} : ${headers.value(index)}")
                }
            }
            if (contentLevel.body && clone.promisesBody()) {
                if (responseBody == null) return response
                if (isPlaintext(responseBody.contentType())) {
                    val bufferSize = 1024 * 8
                    val bytes = ByteArrayOutputStream().use { output ->
                        responseBody!!.byteStream().copyTo(output, bufferSize)
                        output.toByteArray()
                    }
                    val contentType = responseBody.contentType()
                    val body = String(bytes, getCharset(contentType))
                    requestLog.appendLine("\t body:$body")
                    responseBody = bytes.toResponseBody(responseBody.contentType())
                    return response.newBuilder().body(responseBody).build()
                } else {
                    requestLog.appendLine("\tbody: maybe [binary body], omitted!")
                }
            }
        } catch (e: Exception) {
            logger.e(
                logger.mDefaultTag,
                "Exception encountered while processing response information",
                e
            )
        } finally {
            requestLog.append("<-- END HTTP")
            println(requestLog.toString())
            log(responseLevel(response), logger.mDefaultTag, requestLog.toString())
        }
        return response
    }

    /**
     * Convert [Request.body] to string.
     *
     * @since 1.3.3
     */
    private fun StringBuilder.bodyToString(request: Request) {
        try {
            val copy = request.newBuilder().build()
            val body = copy.body ?: return
            val buffer = Buffer()
            body.writeTo(buffer)
            val charset = getCharset(body.contentType())
            appendLine("\tbody: ${buffer.readString(charset)}")
        } catch (e: Exception) {
            logger.e(logger.mDefaultTag, "Exception encountered while processing request body", e)
        }
    }

    /** @since 1.3.3 */
    private fun log(level: LogLevel, tag: String, content: String, tr: Throwable? = null) {
        logger.log(level, tag, content, tr)
    }

    companion object : SingletonHolder<Okhttp3Interceptor, LogCat>(::Okhttp3Interceptor) {
        private val UTF8: Charset = StandardCharsets.UTF_8

        /**
         * Get charset.
         *
         * @since 1.3.3
         */
        private fun getCharset(contentType: MediaType?): Charset {
            var charset = if (contentType != null) contentType.charset(UTF8) else UTF8
            if (charset == null) charset = UTF8
            return charset
        }

        /**
         * Returns true if the body in question probably contains human readable
         * text. Uses a small sample of code points to detect unicode control
         * characters commonly used in binary file signatures.
         *
         * @since 1.3.3
         */
        private fun isPlaintext(mediaType: MediaType?): Boolean {
            if (mediaType == null) return false
            if (mediaType.type == "text") {
                return true
            }
            var subtype = mediaType.subtype
            subtype = subtype.lowercase(Locale.getDefault())
            return subtype.contains("x-www-form-urlencoded") ||
                    subtype.contains("json") ||
                    subtype.contains("xml") ||
                    subtype.contains("html")
        }
    }
}