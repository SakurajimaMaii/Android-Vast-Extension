/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.app.activity.reflection

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.app.activity.vbdelegate.ArticleAdpt
import com.ave.vastgui.app.databinding.ActivityReflectBinding
import com.ave.vastgui.app.net.WanAndroidApi
import com.ave.vastgui.app.net.WanAndroidApiService
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.network.request.create
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/6
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/vb-reflection/
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/lifecycle-aware-components/vm-reflection/

class ReflectActivity1 : VastVbVmActivity<ActivityReflectBinding, ReflectViewModel1>() {

    private lateinit var mAdpt: ArticleAdpt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        // 初始化文章列表
        mAdpt = ArticleAdpt()
        getBinding().articles.apply {
            adapter = mAdpt
            layoutManager = LinearLayoutManager(this@ReflectActivity1)
        }
    }

    private fun initData() {
        // 初始化文章数据
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val articles = WanAndroidApi().create(WanAndroidApiService::class.java)
                    .getArticles()
                mAdpt.addArticles(articles.data.datas)
            }
        }
    }

}

class ReflectActivity2 : ReflectBaseActivity2<ActivityReflectBinding, ReflectViewModel2>() {

    override fun createViewModel(modelClass: Class<out ViewModel>): ReflectViewModel2 {
        return ReflectViewModel2("这是一个参数")
    }

}