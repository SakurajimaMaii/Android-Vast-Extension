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
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.databinding.ActivityHorizontalProgressViewBinding
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

class HorizontalProgressViewActivity : ComponentActivity() {

    private val mBinding by viewBinding(ActivityHorizontalProgressViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mBinding.lineTextProgressView.setOnClickListener {
//            val bitmap = viewSnapshot(it)
//            BmpUtils.saveBitmapAsFile(
//                bitmap = bitmap,
//                File(FileMgr.appInternalFilesDir(), "line_text_box_color.jpg")
//            )?.apply {
//                SimpleToast.showShortMsg("截图${name}已保存")
//            }
//        }

        mBinding.bottomSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.horizontalProgressView.refreshWithInvalidate {
                    mCurrentProgress = progress.toFloat()
                }
                mBinding.horizontalTextProgressView.refreshWithInvalidate {
                    mCurrentProgress = progress.toFloat()
                }
                mBinding.lineTextProgressView.refreshWithInvalidate {
                    mCurrentProgress = progress.toFloat()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

}