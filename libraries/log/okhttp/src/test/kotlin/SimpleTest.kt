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

import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.format.TableFormat
import com.log.vastgui.core.getLogFactory
import com.log.vastgui.core.json.GsonConverter
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogSwitch
import com.log.vastgui.okhttp.Okhttp3Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources.createFactory
import org.junit.Test
import java.util.concurrent.CountDownLatch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/9/18

val logFactory = getLogFactory {
    install(LogSwitch) {
        open = true
    }
    install(LogPrinter) {
        logger = object : Logger {
            override val logFormat: LogFormat
                get() = TableFormat(1000, Int.MAX_VALUE, TableFormat.LogHeader.default)

            override fun log(logInfo: LogInfo) {
                println(logFormat.format(logInfo))
            }
        }
    }
}
val logcat = logFactory("global")
val gson = GsonConverter.getInstance(true)
val okhttp = OkHttpClient
    .Builder()
    .addInterceptor(Okhttp3Interceptor(logcat).apply {
        bodyJsonConverter = { gson.parseString(it) }
    })
    .build()

class Stream {
    @Test
    fun simpleRequest() {
        val request: Request = Request.Builder()
            .url("http://127.0.0.1:7777")
            .build()
        okhttp.newCall(request).execute()
    }

    @Test
    fun sseRequest() {
        val request: Request = Request.Builder()
            .url("http://127.0.0.1:7777/stream")
            .build()
        val countDownLatch = CountDownLatch(1)
        val eventSourceListener: EventSourceListener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {

            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String
            ) {
                println(String.format("id=%s type=%s data=%s", id, type, data))
            }

            override fun onClosed(eventSource: EventSource) {
                countDownLatch.countDown()
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                println(t?.message)
            }
        }
        val source = createFactory(okhttp)
            .newEventSource(request, eventSourceListener)
        try {
            countDownLatch.await()
            source.cancel()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}