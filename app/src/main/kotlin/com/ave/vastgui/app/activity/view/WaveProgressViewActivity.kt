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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityWaveProgressViewBinding
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/progress/wave-progress-view/

class WaveProgressViewActivity : AppCompatActivity() {

    private val mBinding by viewBinding(ActivityWaveProgressViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.waveProgressView.apply {
            mProgressBackgroundColor = ColorUtils.colorHex2Int("#e74c3c")
            mProgressColor = ColorUtils.colorHex2Int("#27ae60")
            mTextColor = ColorUtils.colorHex2Int("#000000")
            mRadius = 100f.DP
            mStrokeColor = ColorUtils.colorHex2Int("#8e44ad")
            mShowText = false
            setImage(R.drawable.ic_github)
            mTextSize = 30f.SP
        }

        mBinding.imageProgressView.apply {
            mTextSize = 30f.SP
        }

        mBinding.progressSlider.addOnChangeListener { _, value, _ ->
            mBinding.waveProgressView.mCurrentProgress = value
            mBinding.imageProgressView.mCurrentProgress = value
        }

        mBinding.strokeSlider.addOnChangeListener { _, value, _ ->
            mBinding.waveProgressView.mStrokeWidth = 20f.DP * (value / 100f)
        }

        mBinding.spaceSlider.addOnChangeListener { _, value, _ ->
            mBinding.waveProgressView.mSpaceWidth = 20f.DP * (value / 100f)
        }
    }

}