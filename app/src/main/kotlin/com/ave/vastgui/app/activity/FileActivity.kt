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

package com.ave.vastgui.app.activity

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.tools.view.extension.hideKeyBroad
import com.ave.vastgui.tools.view.extension.isShouldHideKeyBroad
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : AppCompatActivity(R.layout.activity_file) {

    private val logcat = mLogFactory.getLogCat(FileActivity::class.java)
    private val mBinding by viewBinding(ActivityFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.button.setOnClickListener {
            logcat.d("这是一条日志")
            logcat.e(NullPointerException("this object is null."))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_DOWN == event?.action) {
            val view = currentFocus
            if (null != view && view is EditText) {
                if (view.isShouldHideKeyBroad(event)) {
                    view.hideKeyBroad()
                }
            }
        }
        return super.onTouchEvent(event)
    }

}