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

package cn.govast.vastadapter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import cn.govast.vastadapter.AdapterItem

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Description: 
// Documentation:
// Reference:

/**
 * The ViewHolder of the BindAdapter.If you want
 * to set your own ViewHolder for Adapter,you
 * should making your ViewHolder extends the [BaseBindHolder]
 * and make **setViewHolder** return it.
 *
 * @property binding
 */
open class BaseBindHolder(protected var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindData(variableId: Int, item: AdapterItem?) {
        binding.setVariable(variableId, item)
    }
}