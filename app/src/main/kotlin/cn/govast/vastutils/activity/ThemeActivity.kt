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

package cn.govast.vastutils.activity

import android.net.Uri
import android.os.Bundle
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.utils.ColorUtils
import cn.govast.vasttools.utils.LogUtils
import cn.govast.vasttools.utils.ResUtils
import cn.govast.vastutils.R
import cn.govast.vastutils.databinding.ActivityThemeBinding

class ThemeActivity : VastVbActivity<ActivityThemeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableFullScreen(true)
        super.onCreate(savedInstanceState)

        getBinding().video.apply {
            setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.start))
            start()
            setOnCompletionListener {
                getBinding().video.start()
            }
        }

        LogUtils.i(
            getDefaultTag(),
            ColorUtils.getColorTransparency(50, ResUtils.getColor(R.color.black))
        )
    }

}