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

package com.ave.vastgui.tools.view.viewpager2.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.collections.toList

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/10 19:47
// Description: Base fragment adapter for viewpager2 when activity is [AppCompatActivity].
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/viewpager2/adapter/base-fragment-state-adapter/

/**
 * Base [androidx.viewpager2.adapter.FragmentStateAdapter] for [androidx.viewpager2.widget.ViewPager2].
 *
 * ```kotlin
 * // Use in activity
 * // vp2 is viewpager2
 * vp2.adapter = BaseFragmentStateAdapter(this,ArrayList<Fragment>().apply {
 *      add(BaseVbFragment())
 *      add(BaseVmFragment())
 *      add(BaseVbVmFragment())
 * })
 * ```
 *
 * @since 1.5.2
 */
open class BaseFragmentStateAdapter : FragmentStateAdapter {

    protected val fragments: MutableList<Fragment> = ArrayList()

    /** @since 1.5.2 */
    constructor(activity: FragmentActivity, fragments: List<Fragment>) : super(activity) {
        this.fragments.addAll(fragments)
    }

    /** @since 1.5.2 */
    constructor(fragment: Fragment, fragments: List<Fragment>) : super(fragment) {
        this.fragments.addAll(fragments)
    }

    /** @since 1.5.2 */
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle, fragments: List<Fragment>) : super(fragmentManager, lifecycle) {
        this.fragments.addAll(fragments)
    }

    override fun getItemCount() = fragments.toList().size

    override fun createFragment(position: Int) = fragments.toList()[position]

}