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

package cn.govast.vastadapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Description: 
// Documentation:
// Reference:

interface AdapterItem: AdapterClickRegister {

    /**
     * @return The item layout resource id.
     */
    fun getBindType(): Int{
        TODO("getVBAdpItemType() is not implement.")
    }

    /**
     * @return A string which is same as the value you set in
     *     [VastViewHolder.BVAdpVHFactory.getVAdpVHType]. In this way, the data
     *     item can be matched to the corresponding ViewHolder.
     */
    fun getItemType(): String{
        TODO("getVAdpItemType() is not implement.")
    }

}