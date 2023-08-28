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

package com.ave.vastgui.app.activity.reflectexample

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.app.activity.vbexample.VbAdapter2
import com.ave.vastgui.app.activity.vbexample.persons
import com.ave.vastgui.app.databinding.ActivityMyBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/6
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/Reflect/
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/lifecycle-aware-components/Reflect/

class ReflectActivity1 : ReflectBaseActivity1<ActivityMyBinding, ReflectViewModel1>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter2(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

}

class ReflectActivity2 : ReflectBaseActivity2<ActivityMyBinding, ReflectViewModel2>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter2(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ReflectViewModel2 {
        return ReflectViewModel2("这是一个参数")
    }

}