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

package cn.govast.vastutils.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.govast.vasttools.viewModel.VastViewModel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/2/19
// Description:
// Documentation:

class SampleSharedVM(val tag:String): VastViewModel() {

    private val _count:MutableLiveData<Int> = MutableLiveData(0)

    val count:LiveData<Int>
        get() = _count

    fun addOne(){
        _count.postValue(_count.value?.plus(1) ?: 0)
    }

}