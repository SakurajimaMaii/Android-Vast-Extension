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

package activity

import android.content.Intent
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ave.vastgui.core.onSuccess
import com.ave.vastgui.tools.activity.app.VastCropActivity
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.io.getAssetsFile
import com.ave.vastgui.tools.io.uri
import com.ave.vastgui.tools.utils.DensityUtils.DP
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CropTests {

    @Test
    fun testCropActivity() {
        getAssetsFile("test.jpg").onSuccess { file ->
            val uri = file.uri("com.ave.vastgui.app")
            val intent = Intent(ContextHelper.getAppContext(), VastCropActivity::class.java).apply {
                data = uri
                putExtra(VastCropActivity.AUTHORITY, "com.ave.vastgui.app")
                putExtra(VastCropActivity.FRAME_TYPE, VastCropActivity.FRAME_TYPE_RECTANGLE)
                putExtra(VastCropActivity.PREVIEW_WIDTH, 300f.DP)
                putExtra(VastCropActivity.PREVIEW_HEIGHT, 300f.DP)
                putExtra(VastCropActivity.OUTPUT_X, 400f)
                putExtra(VastCropActivity.OUTPUT_Y, 400f)
            }

            ActivityScenario.launch<VastCropActivity>(intent).use { scenario ->
                val resultCode = scenario.result.resultCode
                val resultData = scenario.result.resultData
                Log.d(TAG, "resultCode=$resultCode resultData=$resultData")
            }
        }
    }

    companion object {
        const val TAG = "CropTests"
    }

}