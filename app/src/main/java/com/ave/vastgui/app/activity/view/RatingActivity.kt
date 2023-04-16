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
import com.ave.vastgui.app.databinding.ActivityRatingBinding
import com.ave.vastgui.tools.view.ratingview.RatingView
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

class RatingActivity : AppCompatActivity() {

    private val mBinding by reflexViewBinding(ActivityRatingBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.ratingView
            .setStarCountNumber(5)
            .setStarBitMapSize(40,40)
            .setStarIntervalWidth(20)
            .setStarSelectMethod(RatingView.SelectMethod.Sliding)
            .setStarRating(3.6f)
    }

}