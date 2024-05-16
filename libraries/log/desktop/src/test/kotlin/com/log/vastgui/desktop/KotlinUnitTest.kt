/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.log.vastgui.desktop

import com.log.vastgui.core.base.LogLevel
import org.junit.Test


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/14 23:59

class KotlinUnitTest {
    private val mLogger = mLogFactory.getLog(KotlinUnitTest::class.java)

    @Test
    fun logTest() {
        val jsonString = """{
                "employees": [
                    {
                        "firstName": "Bill",
                        "lastName": "Gates"
                    },
                    {
                        "firstName": "George",
                        "lastName": "Bush"
                    },
                    {
                        "firstName": "Thomas",
                        "lastName": "Carter"
                    }
                ]
            }"""
        mLogger.json(LogLevel.VERBOSE, jsonString)
        val map = mapOf("name" to "Xiao Ming", "age" to 19)
        mLogger.json(LogLevel.DEBUG, map)
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        mLogger.json(LogLevel.INFO, list)
        mLogger.w("This is a log.")
        mLogger.e("This is a log.")
        mLogger.a("This is a log.")
    }

    private class User(val name: String)
}