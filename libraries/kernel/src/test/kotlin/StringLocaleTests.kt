import com.ave.vastgui.core.text.isChinese
import kotlin.test.Asserter
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/3/9
// Documentation: 
// Reference:

class StringLocaleTests {

    @Test
    fun simplifiedChinese() {
        assertTrue(isChinese("你好，世界"))
    }

    @Test
    fun traditionalChinese() {
        assertTrue(isChinese("你好，世界，這是繁體"))
    }

    @Test
    fun english() {
        assertFalse(isChinese("Hello World!"))
    }

}