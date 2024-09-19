package com.devenv.vastgui.streamapp.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {
    @GetMapping("/")
    suspend fun index() =
        "{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true}"

    @GetMapping("stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun stream() = flow<String> {
        repeat(3) {
            emit("{\"name\":\"BeJson\",\"url\":\"http://www.bejson.com\",\"page\":88,\"isNonProfit\":true}")
            delay(50)
        }
    }

    @GetMapping("multi", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun multi() = flow<String> {
        emit("{")
        delay(50)
        emit("\"name\": \"zhangsan\",")
        delay(50)
        emit("\"age\", 25")
        delay(50)
        emit("}")
    }
}