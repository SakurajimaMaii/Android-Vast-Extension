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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.govast.vasttools.activity.delegate.ActivityVbVmDelegate
import cn.govast.vasttools.adapter.VastFragmentAdapter
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vastutils.databinding.ActivityFragmentsBinding
import cn.govast.vastutils.fragment.SampleVbFragment
import cn.govast.vastutils.fragment.SampleVbVmFragment
import cn.govast.vastutils.fragment.SampleVmFragment
import cn.govast.vastutils.viewmodel.SampleSharedVM

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 19:45
// Description:
// Documentation:

class FragmentsActivity : AppCompatActivity() {

    private inner class AVVD:ActivityVbVmDelegate<ActivityFragmentsBinding, SampleSharedVM>(this)

    private var mActivityDelegate by NotNUllVar<AVVD>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityDelegate = AVVD()
        mActivityDelegate.getBinding().vp2.apply {
            adapter = VastFragmentAdapter(this@FragmentsActivity,ArrayList<Fragment>().apply {
                add(SampleVbVmFragment())
                add(SampleVmFragment())
                add(SampleVbFragment())
            })
        }
    }

}