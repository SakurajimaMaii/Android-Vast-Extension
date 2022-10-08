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
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/3
// Description: 
// Documentation:
// Reference:

class MaterialAlertDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {

    private var mView:View? = null

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.0.9
     */
    override fun setView(layoutResId: Int) = apply {
        setView(layoutResId, context, null)
    }

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.0.9
     */
    override fun setView(view: View?) = apply {
        mView = view
        super.setView(view)
    }

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.0.9
     */
    fun setView(
        @LayoutRes layoutId: Int,
        context: Context,
        root: ViewGroup?
    ) = apply {
        mView = LayoutInflater.from(context).inflate(layoutId, root)
        super.setView(mView)
    }

    /**
     * Get the layout of the Dialog.
     *
     * @since 0.0.9
     */
    fun getView() = mView

    /**
     * Get the layout of the Dialog.
     *
     * @return the not null layout.
     * @throws IllegalStateException
     * @since 0.0.9
     */
    fun requireView():View{
        if(null == mView){
            throw IllegalStateException("View is null.")
        }
        return mView!!
    }

    /**
     * Find view from the custom view of the Dialog.
     *
     * @param id view id.
     * @param T view class.
     * @since 0.0.9
     */
    fun <T : View> findViewById(@IdRes id: Int): T {
        val view = requireView().findViewById<T>(id)
        if (null == view) {
            throw NullPointerException("Can't find the view by id.")
        } else return view
    }

}