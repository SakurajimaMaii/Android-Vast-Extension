package com.devenv.vastgui.streamapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StreamApplication

fun main(args: Array<String>) {
    runApplication<StreamApplication>(*args)
}
