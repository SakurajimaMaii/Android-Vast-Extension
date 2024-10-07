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

package com.log.vastgui.slf4j

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.NotNullOrDefault
import com.log.vastgui.core.LogFactory
import com.log.vastgui.core.annotation.LogExperimental
import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.helpers.NOPMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/7
// Documentation:
// Reference:

@LogExperimental
class Slf4jProvider internal constructor(): SLF4JServiceProvider {
    private var slf4jFactory: ILoggerFactory by Delegates.notNull()
    private var slf4jMarkerFactory: IMarkerFactory by Delegates.notNull()
    private var slf4jMdcAdapter: MDCAdapter by Delegates.notNull()

    override fun getLoggerFactory() = slf4jFactory

    override fun getMarkerFactory() = slf4jMarkerFactory

    override fun getMDCAdapter() = slf4jMdcAdapter

    override fun getRequestedApiVersion() = REQUESTED_API_VERSION

    override fun initialize() {
        slf4jFactory = Options.slf4jFactory
        slf4jMarkerFactory = Options.slf4jMarkerFactory
        slf4jMdcAdapter = Options.slf4jMDCAdapter
    }

    object Options {

        internal var slf4jFactory: Slf4jFactory by NotNUllVar(true)
        internal var slf4jMDCAdapter: MDCAdapter by NotNullOrDefault(NOPMDCAdapter())
        internal var slf4jMarkerFactory: IMarkerFactory by NotNullOrDefault(BasicMarkerFactory())

        @JvmStatic
        fun setFactory(logFactory: LogFactory) {
            slf4jFactory = Slf4jFactory(logFactory)
        }

        @JvmStatic
        fun setMDCAdapter(mdcAdapter: MDCAdapter) {
            slf4jMDCAdapter = mdcAdapter
        }

        @JvmStatic
        fun setMarkerFactory(markerFactory: IMarkerFactory) {
            slf4jMarkerFactory = markerFactory
        }

    }

    companion object {
        const val REQUESTED_API_VERSION: String = "2.0.16"
    }
}