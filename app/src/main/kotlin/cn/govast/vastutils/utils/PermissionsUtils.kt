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

package cn.govast.vastutils.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import cn.govast.vasttools.base.BaseActive
import cn.govast.vasttools.base.BaseActivity
import cn.govast.vasttools.base.BaseFragment
import cn.govast.vasttools.utils.ActivityUtils.getActivity
import cn.govast.vasttools.utils.ActivityUtils.getCurrentActivity
import cn.govast.vasttools.utils.ToastUtils

// Author: lt VastGui
// Email: lt.dygzs@qq.com guihy2019@gmail.com
// Date: 2022/9/14 12:16
// Description: 
// Documentation:

object PermissionsUtils {
    /**
     * Allows an application to read the user's calendar data.
     *
     * @since 0.0.9
     */
    const val DATE = Manifest.permission.READ_CALENDAR

    /**
     * Required to be able to access the camera device.
     *
     * @since 0.0.9
     */
    const val CAMERA = Manifest.permission.CAMERA

    /**
     * Allows an application to read the user's contacts data.
     *
     * @since 0.0.9
     */
    const val PEOPLE = Manifest.permission.READ_CONTACTS

    /**
     * Allows an app to access precise location.
     *
     * @since 0.0.9
     */
    const val LOCATION =
        Manifest.permission.ACCESS_FINE_LOCATION

    /**
     * Allows an application to record audio.
     *
     * @since 0.0.9
     */
    const val AUDIO = Manifest.permission.RECORD_AUDIO

    /**
     * Allows read only access to phone state,
     * including the current cellular network information,
     * the status of any ongoing calls,
     * and a list of any android.telecom.
     * PhoneAccounts registered on the device.
     *
     * @since 0.0.9
     */
    const val PHONE = Manifest.permission.READ_PHONE_STATE

    /**
     * Allows an application to read SMS messages.
     *
     * @since 0.0.9
     */
    const val SMS = Manifest.permission.READ_SMS

    /**
     * Allows an application to write to external storage.
     *
     * @since 0.0.9
     */
    const val SD = Manifest.permission.WRITE_EXTERNAL_STORAGE

    /**
     * Allows an application to access data from sensors
     * that the user uses to measure what is happening
     * inside their body, such as heart rate.
     *
     * @since 0.0.9
     */
    const val SENSORS = Manifest.permission.BODY_SENSORS

    private const val REQUEST_CODE_PERMISSION = 221

    private var mExecuteCallBack: OnRequestPermissionCallBack<*>? = null
    private var permissions: Array<out String>? = null //上次请求的权限

    abstract class OnRequestPermissionCallBack<T : BaseActive> {
        var baseActivityClass: Class<out BaseActivity>? = null
        var baseFragmentClass: Class<out BaseFragment>? = null

        abstract fun T.onPermissionOk()

        /**
         * Failed to apply for permission.
         *
         * @param permission Permission name, you can use the [permission] to apply directly.
         */
        abstract fun onPermissionError(permission: String)

        /**
         * Some of the authority to apply was "not inquired".
         * You can use the show Remind Dialog method of this
         * class to enter a description of why the permission
         * is required, and let the user manually grant the permission.
         *
         * @param permission the name of permission.
         */
        abstract fun onPermissionNotAsking(permission: String)
    }

    abstract class OnOkPermissionCallBack<T : BaseActive> : OnRequestPermissionCallBack<T>() {
        override fun onPermissionError(permission: String) {
            PrivateOnErrorPermissionCallBack.onPermissionError(permission)
        }

        override fun onPermissionNotAsking(permission: String) {
            PrivateOnErrorPermissionCallBack.onPermissionNotAsking(permission)
        }
    }

    object PrivateOnErrorPermissionCallBack : OnRequestPermissionCallBack<BaseActive>() {
        override fun BaseActive.onPermissionOk() {

        }

        override fun onPermissionError(permission: String) {
            ToastUtils.showShortMsg(
                "The operation failed, please agree to enable ${
                    permissionToString(permission)
                } then continue "
            )
        }

        override fun onPermissionNotAsking(permission: String) {
            val baseActivityClass = baseActivityClass
            val a = if (baseActivityClass != null) getActivity(baseActivityClass)
                ?: getCurrentActivity() else getCurrentActivity()
            if (a != null){

            }
//                showRemindDialog(
//                    a, "Setting-App-${AppUtils.getAppName()}-Permission Setting-open ${
//                        permissionToString(permission)
//                    }, to use the function normally"
//                )
        }

        private fun permissionToString(permission: String) = when (permission) {
            DATE -> "读写日程"
            CAMERA -> "相机"
            PEOPLE -> "读写联系人"
            LOCATION -> "位置信息"
            AUDIO -> "麦克风"
            PHONE -> "访问电话状态"
            SMS -> "读取短信"
            SD -> "存储"
            SENSORS -> "传感器"
            else -> "相应"
        }
    }

    /**
     * Determine if permission authorized.
     *
     * @since 0.0.9
     */
    fun isHavePermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}