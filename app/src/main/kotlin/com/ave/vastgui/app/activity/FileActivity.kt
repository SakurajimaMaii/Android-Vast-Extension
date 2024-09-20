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
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.net.LocalApi
import com.ave.vastgui.app.net.LocalApiService
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.utils.drawable
import com.ave.vastgui.tools.view.extension.hideKeyBroad
import com.ave.vastgui.tools.view.extension.isShouldHideKeyBroad
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlinx.coroutines.launch

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : AppCompatActivity(R.layout.activity_file) {

    private val logcat = logFactory(FileActivity::class.java)
    private val binding by viewBinding(ActivityFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            LocalApi().create(LocalApiService::class.java)
                .getLocalData()
        }

        binding.button.setOnClickListener {
            logcat.d("这是一条日志")
            logcat.e(NullPointerException("this object is null."))
        }
        val drawable = drawable(R.drawable.ic_github).also {
            logcat.d(it!!::class.java.simpleName)
        }
        binding.icon1.run {  }
        null.isNullOrBlank()
        binding.icon1.setImageDrawable(drawable)
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