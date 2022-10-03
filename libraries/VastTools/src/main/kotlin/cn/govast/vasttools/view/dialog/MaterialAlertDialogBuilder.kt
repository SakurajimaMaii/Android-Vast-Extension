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

package cn.govast.vasttools.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import cn.govast.vasttools.extension.NotNUllVar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/3
// Description: 
// Documentation:
// Reference:

class MaterialAlertDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {

    private var mView by NotNUllVar<View>()
    private var viewListener: ((view: View) -> Unit)? = null

    /**
     * Get the layout of the Dialog by [layoutId].
     *
     * @since 0.0.9
     */
    @JvmOverloads
    fun setView(
        @LayoutRes layoutId: Int,
        context: Context = getContext(),
        root: ViewGroup? = null
    ) = apply {
        mView = LayoutInflater.from(context).inflate(layoutId, root)
        viewListener?.invoke(mView)
        super.setView(mView)
    }

    /**
     * Get the layout of the Dialog. And you can get the view from the [layout].
     *
     * @since 0.0.9
     */
    fun getView(l: (layout: View) -> Unit) = apply {
        viewListener = l
    }

    /**
     * Get the layout of the Dialog.
     *
     * @since 0.0.9
     */
    fun getView() = mView

    interface ViewListener {
        fun setViewListener(view: View)
    }

}