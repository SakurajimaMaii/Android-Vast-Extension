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

package com.ave.vastgui.tools.utils

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.StringRes
import com.ave.vastgui.tools.helper.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Toast utils.
// Documentation: [ToastUtils](https://sakurajimamaii.github.io/VastDocs/document/en/ToastUtils.html)

/**
 * ToastUtils
 *
 * Here is an example:
 * ```Java
 * ToastUtils.INSTANCE.showShortMsg(this,message);
 * ```
 */
object ToastUtils {

    private var mToast: Toast? = null

    /**
     * @param msg message of the toast.
     * @param context context.
     */
    @JvmStatic
    @JvmOverloads
    fun showShortMsg(
        msg: String,
        context: Context = ContextHelper.getAppContext()
    ) {
        if (null == mToast) {
            mToast = makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    /**
     * @param id message string id of the toast.
     * @param context context.
     */
    @JvmStatic
    @JvmOverloads
    fun showShortMsg(
        @StringRes id: Int,
        context: Context = ContextHelper.getAppContext()
    ) {
        if (null == mToast) {
            mToast = makeText(context, id, Toast.LENGTH_SHORT)
        } else {
            mToast!!.setText(id)
        }
        mToast!!.show()
    }


    /**
     * @param msg message of the toast.
     * @param context context.
     */
    @JvmStatic
    @JvmOverloads
    fun showLongMsg(
        msg: String,
        context: Context = ContextHelper.getAppContext()
    ) {
        if (null == mToast) {
            mToast = makeText(context, msg, Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    /**
     * @param id message string id of the toast.
     * @param context context.
     */
    @JvmStatic
    @JvmOverloads
    fun showLongMsg(
        @StringRes id: Int,
        context: Context = ContextHelper.getAppContext()
    ) {
        if (null == mToast) {
            mToast = makeText(context, id, Toast.LENGTH_LONG)
        } else {
            mToast!!.setText(id)
        }
        mToast!!.show()
    }
}