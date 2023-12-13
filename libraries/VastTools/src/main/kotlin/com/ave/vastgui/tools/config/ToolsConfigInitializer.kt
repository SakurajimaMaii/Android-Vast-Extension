package com.ave.vastgui.tools.config

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

class ToolsConfigInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ToolsConfig.init(context.applicationContext as Application)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}