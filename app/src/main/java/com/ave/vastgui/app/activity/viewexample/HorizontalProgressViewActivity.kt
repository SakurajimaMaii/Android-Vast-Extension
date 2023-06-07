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

package com.ave.vastgui.app.activity.viewexample

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.databinding.ActivityHorizontalProgressViewBinding
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

class HorizontalProgressViewActivity : AppCompatActivity() {

    private val mBindings by reflexViewBinding(ActivityHorizontalProgressViewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBindings.bottomSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBindings.arcProgressView.refreshWithInvalidate {
                    setCurrentProgress(progress.toFloat())
                }
                mBindings.waveProgressView.refreshWithInvalidate {
                    setCurrentProgress(progress.toFloat())
                }
                mBindings.horizontalProgressView.refreshWithInvalidate {
                    setCurrentProgress(progress.toFloat())
                }
                mBindings.horizontalTextProgressView.refreshWithInvalidate {
                    setCurrentProgress(progress.toFloat())
                }
                mBindings.lineTextProgressView.refreshWithInvalidate {
                    setCurrentProgress(progress.toFloat())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }


}