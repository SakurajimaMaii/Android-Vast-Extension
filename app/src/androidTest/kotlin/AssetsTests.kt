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

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.FileActivity
import com.google.android.material.imageview.ShapeableImageView
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssetsTests {

    @Test
    fun assetsFile() {
        val scenario = ActivityScenario.launch(FileActivity::class.java)

        scenario.onActivity { activity ->
            activity.initImage()
        }

        onView(withId(R.id.image)).check { view, _ ->
            Assert.assertNotNull((view as ShapeableImageView).drawable)
        }
    }

    companion object {
        const val TAG = "FileTests"
    }

}