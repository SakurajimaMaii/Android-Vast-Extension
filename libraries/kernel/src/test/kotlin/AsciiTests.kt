import com.ave.vastgui.core.text.AsciiUtil
import kotlin.test.Test
import kotlin.test.assertEquals

/*
 * Copyright 2021-2025 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/7/4
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/core/text/ascii-util/

class AsciiTests {

    @Test
    fun half2full() {
        assertEquals("你好，世界？", AsciiUtil.half2full("你好,世界?"))
    }

    @Test
    fun full2half() {
        assertEquals("你好,世界?", AsciiUtil.full2half("你好，世界？"))
    }

}