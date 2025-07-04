/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.view.dialog

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
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/dialog/material-alert-dialog-builder/

/** @since 0.1.0 */
class MaterialAlertDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {

    /** @since 1.5.2 */
    private var view: View? = null

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.1.0
     */
    override fun setView(layoutResId: Int) = apply {
        setView(layoutResId, context, null)
    }

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.1.0
     */
    override fun setView(view: View?) = apply {
        this.view = view
        super.setView(view)
    }

    /**
     * Sets a custom view to be the contents of the dialog.
     *
     * @since 0.1.0
     */
    fun setView(@LayoutRes layoutId: Int, context: Context, root: ViewGroup?) = apply {
        view = LayoutInflater.from(context).inflate(layoutId, root)
        super.setView(view)
    }

    /**
     * Get the layout of the Dialog.
     *
     * @since 0.1.0
     */
    fun getView() = view

    /**
     * Get the layout of the Dialog.
     *
     * @return the not null layout.
     * @throws IllegalStateException
     * @since 0.1.0
     */
    fun requireView(): View {
        if (null == view) {
            throw IllegalStateException("View is null.")
        }
        return view!!
    }

    /**
     * Find view from the custom view of the Dialog.
     *
     * @param T view class.
     * @param id view id.
     * @since 0.1.0
     */
    fun <T : View> findViewById(@IdRes id: Int): T {
        val view = requireView().findViewById<T>(id)
        if (null == view) {
            throw NullPointerException("Can't find the view by id.")
        } else return view
    }

}