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
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.view.extension.hideKeyBroad
import com.ave.vastgui.tools.view.extension.isShouldHideKeyBroad
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.log.vastgui.android.lifecycle.LogLifecycle
import kotlin.concurrent.thread

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/app-data-and-files/file-manager/file-mgr/

@LogLifecycle
class FileActivity : ComponentActivity(R.layout.activity_file) {

    private val logcat = logFactory("FileActivity")
    private val binding by viewBinding(ActivityFileBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://developer.android.com/develop/ui/views/layout/edge-to-edge?hl=zh-cn
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onResume() {
        super.onResume()

        if (false) {
            thread {
                repeat(10) {
                    logcat.d {
                        "苏州园林里都有假山和池沼。假山的堆叠，可以说是一项艺术而不仅是技术。" +
                                "或者是重峦叠嶂，或者是几座小山配合着竹子花木，全在乎设计者和匠师们生平多阅历，胸" +
                                "中有邱壑，才能使游览者攀登的时候忘却苏州城市，只觉得身在山间。至于池沼，大多引用" +
                                "活水。有些园林池沼宽敞，就把池沼作为全园的中心，其他景物配合着布置。水面假如成河" +
                                "道模样，往往安排桥梁。假如安排两座以上的桥梁，那就一座一个样，决不雷同。池沼或河" +
                                "道的边沿很少砌齐整的石岸，总是高低屈曲任其自然。还在那儿布置几块玲珑的石头，或者" +
                                "种些花草：这也是为了取得从各个角度看都成一幅画的效果。池沼里养着金鱼或各色鲤鱼，" +
                                "夏秋季节荷花或睡莲开放，游览者看“鱼戏莲叶间”，又是入画的一景。"
                    }
                }
            }
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