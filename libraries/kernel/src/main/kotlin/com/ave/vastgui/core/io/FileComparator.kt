/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.core.io

import com.ave.vastgui.core.annotation.ExperimentalApi
import java.io.File

/**
 * @since 0.1.4
 */
@ExperimentalApi
class FileComparator : Comparator<File> {
    /**
     * @since 0.1.4
     */
    override fun compare(self: File?, other: File?): Int {
        var value1 = 0
        var value2 = 0
        if (self?.isDirectory() == true) {
            value1 = 1
        }
        if (other?.isDirectory() == true) {
            value2 = 1
        }
        return if (value1 != value2) {
            value2 - value1
        } else {
            self?.getName().toString().compareTo(other?.getName().toString())
        }
    }

}