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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityRatingBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.ratingview.RatingView
import com.ave.vastgui.tools.view.ratingview.StarOrientation
import com.ave.vastgui.tools.view.ratingview.StarSelectMethod
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/rating/rating-view/

class RatingActivity : AppCompatActivity(R.layout.activity_rating) {

    private val binding by viewBinding(ActivityRatingBinding::bind)
    private val logcat = logFactory(RatingActivity::class.java)

    private val orientation
        get() = StarOrientation.entries

    private var orientationIndex = 0

    private val selectBmp: Array<Int>
        get() = arrayOf(R.drawable.ic_star_selected, com.ave.vastgui.tools.R.drawable.ic_star_default_selected)

    private val unselectBmp: Array<Int>
        get() = arrayOf(R.drawable.ic_star_unselected, com.ave.vastgui.tools.R.drawable.ic_star_default_unselected)

    private var bmpIndex = 0

    private val method = StarSelectMethod.entries

    private var methodIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ratingView.refreshWithInvalidate {
            setStarCountNumber(5)
            setStarBitmapSize(40F.DP, 40F.DP)
            setStarIntervalWidth(10F.DP)
            setStarRating(0.8f)
            setOnStarRatingChangeListener(object : RatingView.OnStarRatingChangeListener {
                override fun onRatingChanged(rating: Float) {
                    logcat.d("当前星星评级为 $rating")
                }
            })
        }

        binding.addStarBtn.setOnClickListener {
            with(binding.ratingView) {
                setStarCountNumber(starCountNumber + 1)
                logcat.d { "当前星星总数 $starCountNumber" }
            }
        }

        binding.removeStarBtn.setOnClickListener {
            with(binding.ratingView) {
                setStarCountNumber(starCountNumber - 1)
                logcat.d { "当前星星总数 $starCountNumber" }
            }
        }

        binding.switchStarOrientationBtn.setOnClickListener {
            binding.ratingView.setStarOrientation(orientation[(++orientationIndex) % orientation.size])
        }

        binding.switchStarBitmapBtn.setOnClickListener {
            val index = (++bmpIndex) % selectBmp.size
            binding.ratingView.setStarSelectedBitmap(selectBmp[index])
            binding.ratingView.setStarUnselectedBitmap(unselectBmp[index])
        }

        binding.switchSelectMethodBtn.text = String.format(getString(R.string.rating_switch_method_fmt), binding.ratingView.starSelectMethod)
        binding.switchSelectMethodBtn.setOnClickListener {
            binding.ratingView.setStarSelectMethod(method[(++methodIndex) % method.size])
            binding.switchSelectMethodBtn.text = String.format(getString(R.string.rating_switch_method_fmt), binding.ratingView.starSelectMethod)
        }

        // 切换图标尺寸
        binding.changeStarSizeSlider.setLabelFormatter { String.format(getString(R.string.rating_size_fmt), it) }
        binding.changeStarSizeSlider.addOnChangeListener { _, value, _ ->
            binding.ratingView.setStarBitmapSize(value.DP, value.DP)
        }

        // 切换图标间距
        binding.changeStarInternalSlider.setLabelFormatter { String.format(getString(R.string.rating_internal_fmt), it) }
        binding.changeStarInternalSlider.addOnChangeListener { _, value, _ ->
            binding.ratingView.setStarIntervalWidth(value.DP)
        }

        binding.changeRatingSlider.value = binding.ratingView.rating
        binding.changeRatingSlider.addOnChangeListener { _, value, _ ->
            binding.ratingView.setStarRating(value)
        }

        binding.ratingView.setOnStarRatingChangeListener(object : RatingView.OnStarRatingChangeListener {
            override fun onRatingChanged(rating: Float) {
                binding.changeRatingSlider.value = rating
            }
        })

        binding.ratingView.setOnClickListener {
            SimpleToast.showShortMsg("这是一个点击事件")
        }
    }

}