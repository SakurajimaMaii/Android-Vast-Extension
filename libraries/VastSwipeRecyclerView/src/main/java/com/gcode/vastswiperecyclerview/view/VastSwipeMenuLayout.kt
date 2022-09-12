/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastswiperecyclerview.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.gcode.vastswiperecyclerview.R

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/6/14
// Description:
// Documentation:

/**
 * Vast swipe menu layout
 */
class VastSwipeMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val defaultViewId = 0

    private var mLeftViewId = defaultViewId
    private var mContentViewId = defaultViewId
    private var mRightViewId = defaultViewId

    var mContentView: View? = null
    var mSwipeLeftMenu: VastSwipeMenuView? = null
    var mSwipeRightMenu: VastSwipeMenuView? = null

    /**
     * 布局在rv中的坐标
     */
//    var testPosition:Int = -1

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VastSwipeMenuLayout)
        mLeftViewId =
            typedArray.getResourceId(R.styleable.VastSwipeMenuLayout_leftViewId, mLeftViewId)
        mContentViewId =
            typedArray.getResourceId(R.styleable.VastSwipeMenuLayout_contentViewId, mContentViewId)
        mRightViewId =
            typedArray.getResourceId(R.styleable.VastSwipeMenuLayout_rightViewId, mRightViewId)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (defaultViewId != mLeftViewId) {
            mSwipeLeftMenu = findViewById(mLeftViewId)
        }
        if (defaultViewId != mRightViewId) {
            mSwipeRightMenu = findViewById(mRightViewId)
        }
        if (defaultViewId != mContentViewId) {
            mContentView = findViewById(mRightViewId)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureChild(getChildAt(1), widthMeasureSpec, heightMeasureSpec)
        val height = getChildAt(1).measuredHeight

        mSwipeLeftMenu?.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
        mContentView?.measure(
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
        mSwipeRightMenu?.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mSwipeLeftMenu?.layout(
            -mSwipeLeftMenu!!.measuredWidth,
            0,
            0,
            measuredHeight
        )
        mContentView?.layout(0, 0, measuredWidth, measuredHeight)
        mSwipeRightMenu?.layout(
            measuredWidth,
            0,
            measuredWidth + mSwipeRightMenu!!.measuredWidth,
            measuredHeight
        )
    }

}
