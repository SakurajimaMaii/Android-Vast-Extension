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

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityBadgeViewBinding
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.viewbinding.viewBinding
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/badge/description/

class BadgeViewActivity : ComponentActivity(R.layout.activity_badge_view) {

    private val logger = mLogFactory.getLog(BadgeViewActivity::class.java)

    private val mBinding by viewBinding(ActivityBadgeViewBinding::bind)

    private var mCount by NotNUllVar<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCount = 0
        mBinding.numbadge.mBubbleTextNum = mCount
        mBinding.add.setOnClickListener {
            mBinding.numbadge.mBubbleTextNum = ++mCount
        }

        mBinding.reset.setOnClickListener {
            mCount = 0
        }

        mBinding.showDot.setOnClickListener {
            mBinding.dotbadge.showDot()
            BmpUtils.saveBitmapAsFile(
                bitmap = viewSnapshot(mBinding.dotbadge),
                File(FileMgr.appInternalFilesDir(),"Dot_Badge_Show.png")
            )
        }

        mBinding.hideDot.setOnClickListener {
            mBinding.dotbadge.hideDot()
        }
    }

    private fun viewSnapshot(view: View): Bitmap {
        //使控件可以进行缓存
        view.isDrawingCacheEnabled = true
        //获取缓存的 Bitmap
        var drawingCache = view.drawingCache
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache!!)
        //关闭视图的缓存
        view.isDrawingCacheEnabled = false
        return drawingCache
    }


}