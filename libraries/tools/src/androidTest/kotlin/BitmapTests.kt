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

import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.io.appInternalFilesDir
import com.ave.vastgui.tools.io.getAssetsFile
import com.ave.vastgui.tools.test.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class BitmapTests {

    @Test
    fun getBitmapSize() {
        // 加载图片
        getAssetsFile("ic_android.png").also {
            if (it.isFailure) throw RuntimeException(it.exceptionOrNull())

            val file = it.getOrThrow()
            val (w, h) = BmpUtils.getBitmapWidthHeight { option ->
                BitmapFactory.decodeFile(file.path, option)
            }

            Assert.assertEquals(w, 200)
            Assert.assertEquals(h, 200)
        }
    }

    @Test
    fun saveBitmap() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_android, appContext)
        val file = File(appInternalFilesDir(), "test.png")
        BmpUtils.saveBitmapAsFile(bitmap, file)
    }

    @Test
    fun base64() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_android, appContext)
        val base64 = BmpUtils.getBase64FromBitmap(bitmap)
        println(base64)
    }

}