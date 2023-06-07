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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityFragmentsBinding
import com.ave.vastgui.app.fragment.SampleFragment
import com.ave.vastgui.app.fragment.SampleVbFragment
import com.ave.vastgui.app.fragment.SampleVbVmFragment
import com.ave.vastgui.app.fragment.SampleVmFragment
import com.ave.vastgui.tools.activity.widget.screenConfig
import com.ave.vastgui.tools.adapter.VastFragmentAdapter
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.vp2indicator.Vp2IndicatorType
import com.ave.vastgui.tools.viewbinding.reflexViewBinding

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 19:45
// Description:
// Documentation:

class Vp2IndicatorActivity : AppCompatActivity() {

    private val mBinding by reflexViewBinding(ActivityFragmentsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenConfig(mEnableActionBar = false, mEnableFullScreen = true)
        mBinding.vp2.apply {
            adapter = VastFragmentAdapter(this@Vp2IndicatorActivity, ArrayList<Fragment>().apply {
                add(SampleVbVmFragment())
                add(SampleVmFragment())
                add(SampleVbFragment())
                add(SampleFragment())
            })
        }
        mBinding.vp2indicator.apply {
            setIndicatorStyle(Vp2IndicatorType.BITMAP)
            setBitmapSize(20f.DP.toInt(),20f.DP.toInt())
            setSelectedBitmap(R.drawable.ic_indicator_select)
            setUnSelectedBitmap(R.drawable.ic_indicator_unselect)
//            setIndicatorCircleRadius(8F.DP)
//            setSelectedColor(R.color.tomato)
//            setUnSelectedColor(R.color.limegreen)
            attachToViewPager2(mBinding.vp2)
        }
    }

}