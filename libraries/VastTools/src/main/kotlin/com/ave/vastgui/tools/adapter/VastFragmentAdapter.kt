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

package com.ave.vastgui.tools.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/10 19:47
// Description: Base fragment adapter for viewpager2 when activity is [AppCompatActivity].
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/viewpager2/fragment-adapter/

/**
 * Base fragment adapter for viewpager2.
 *
 * ```kotlin
 * // Use in activity
 * // vp2 is viewpager2
 * vp2.adapter = VastFragmentAdapter(this,ArrayList<Fragment>().apply {
 *      add(BaseVbFragment())
 *      add(BaseVmFragment())
 *      add(BaseVbVmFragment())
 * })
 * ```
 *
 * @property activity The activity that owns the fragments.
 * @property fragments The fragments in the [activity].
 */
open class VastFragmentAdapter(
    protected val activity: FragmentActivity,
    protected val fragments: MutableList<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]

}