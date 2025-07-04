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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ave.vastgui.tools.utils.DateUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/15

@RunWith(AndroidJUnit4::class)
class DateTests {

    @Test
    fun timezone() {
        Assert.assertEquals("GMT+08:00", DateUtils.getCurrentTimeZone(DateUtils.SHORT))
        Assert.assertEquals("中国夏令时间", DateUtils.getCurrentTimeZone(DateUtils.LONG))
        Assert.assertEquals("8", DateUtils.getCurrentTimeZone(DateUtils.NUMBER))
    }

}