package com.pluginversion.vastgui

object Ktor {
    private const val ktor_version = "2.3.2"

    object Server {
        const val core_jvm = "io.ktor:ktor-server-core-jvm:$ktor_version"
        const val cio_jvm = "io.ktor:ktor-server-cio-jvm:$ktor_version"
        const val content_negotiation = "io.ktor:ktor-server-content-negotiation:$ktor_version"
        const val sessions = "io.ktor:ktor-server-sessions:$ktor_version"
        const val auth = "io.ktor:ktor-server-auth:$ktor_version"
        const val openapi = "io.ktor:ktor-server-openapi:$ktor_version"
    }

    object Client {
        val cio = "io.ktor:ktor-client-cio:$ktor_version"
        val content_negotiation = "io.ktor:ktor-client-content-negotiation:$ktor_version"
        val core = "io.ktor:ktor-client-core:$ktor_version"
        val gson = "io.ktor:ktor-client-gson:$ktor_version"
        val logging = "io.ktor:ktor-client-logging:$ktor_version"
        val okhttp = "io.ktor:ktor-client-okhttp:$ktor_version"
    }

    val serialization_kotlinx_gson = "io.ktor:ktor-serialization-gson:$ktor_version"
    val serialization_kotlinx_json = "io.ktor:ktor-serialization-kotlinx-json:$ktor_version"
}