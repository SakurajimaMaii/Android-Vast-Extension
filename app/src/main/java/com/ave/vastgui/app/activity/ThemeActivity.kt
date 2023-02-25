/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.activity

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityThemeBinding
import com.ave.vastgui.app.viewmodel.ParamVM
import com.ave.vastgui.tools.activity.VastVbVmActivity

class ThemeActivity : VastVbVmActivity<ActivityThemeBinding, ParamVM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getBinding().video.apply {
            setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.start))
            start()
            setOnCompletionListener {
                getBinding().video.start()
            }
        }
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return ParamVM("This is a param")
    }

}