package com.log.vastgui.core

import com.log.vastgui.core.LogPipeline.Companion.Before
import com.log.vastgui.core.base.LogPlugin

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/29
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/advanced/advanced/

class SysPlugin private constructor() {

    internal var isFirst = true

    companion object : LogPlugin<Unit, SysPlugin> {
        override val key: String = SysPlugin::class.java.simpleName

        override fun install(plugin: SysPlugin, scope: LogCat) {
            scope.logPipeline.intercept(Before) {
                if (plugin.isFirst) {
                    val head = """
                    .__                                              
                    |  |   ____   ____     ____  ___________   ____  
                    |  |  /  _ \ / ___\  _/ ___\/  _ \_  __ \_/ __ \ 
                    |  |_(  <_> ) /_/  > \  \__(  <_> )  | \/\  ___/ 
                    |____/\____/\___  /   \___  >____/|__|    \___  >
                               /_____/        \/                  \/ 
                    """.trimIndent()
                    println(head)
                    plugin.isFirst = false
                }
            }
        }

        override fun configuration(config: Unit.() -> Unit): SysPlugin {
            return SysPlugin()
        }
    }
}