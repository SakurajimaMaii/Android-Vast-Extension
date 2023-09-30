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

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.adapter.ImageAdapter
import com.ave.vastgui.app.adapter.entity.StudentWrapper
import com.ave.vastgui.app.databinding.ActivityAlphabetSidebarBinding
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.view.alphabetsidebar.AlphabetSideBar
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/alphabetsidebar/alphabetsidebar/

class AlphabetSideBarActivity : ComponentActivity(R.layout.activity_alphabet_sidebar) {

    /**
     * SmoothScrollLayoutManager
     *
     * [Smooth-scroll-layout-manager](https://juejin.cn/post/6844903504310435853)
     */
    private class SmoothScrollLayoutManager(context: Context) : LinearLayoutManager(context) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: RecyclerView.State,
            position: Int
        ) {
            val smoothScroller: LinearSmoothScroller =
                object : LinearSmoothScroller(recyclerView.context) {
                    // 返回：滑过1px时经历的时间(ms)。
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return 0.2f
                    }

                    // https://blog.csdn.net/qq_16251833/article/details/81220518
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    private val mBinding by viewBinding(ActivityAlphabetSidebarBinding::bind)

    private var mAdapter by NotNUllVar<ImageAdapter>()
    private val mData: MutableList<AdapterItemWrapper<*>> = ArrayList<AdapterItemWrapper<*>>().apply {
        repeat(28 * 3) {
            add(StudentWrapper("学生编号$it", it))
        }
    }
    private var mSmoothScrollLayoutManager by NotNUllVar<SmoothScrollLayoutManager>()

    private val mLogger = mLogFactory.getLog(AlphabetSideBarActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAdapter = ImageAdapter(mData, this).apply {
            registerClickEvent(object : AdapterClickListener {
                override fun onItemClick(view: View, pos: Int) {
                    if (pos == 10) {
                        mBinding.alphabetsidebar.setIndicatorLetterTargetIndex("A", pos)
                    }
                    SimpleToast.showShortMsg("这是一个点击事件 $pos")
                }
            })
        }
        mSmoothScrollLayoutManager = SmoothScrollLayoutManager(this@AlphabetSideBarActivity)

        mBinding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = mSmoothScrollLayoutManager
        }

        mBinding.alphabetsidebar.refreshWithInvalidate {
            mBackgroundColor =
                ColorUtils.getColorIntWithTransparency(15, ColorUtils.colorHex2Int("#b2bec3"))
        }

        mBinding.alphabetsidebar.setLetterListener(object : AlphabetSideBar.LetterListener {
            override fun onIndicatorLetterUpdate(letter: String, index: Int, target: Int) {
                mBinding.recyclerView.smoothScrollToPosition(index * 3)
            }

            override fun onIndicatorLetterTargetUpdate(letter: String, target: Int) {
                mLogger.d(tag = "Gtest", "$letter 目标索引更新为 $target")
            }
        })
    }

}