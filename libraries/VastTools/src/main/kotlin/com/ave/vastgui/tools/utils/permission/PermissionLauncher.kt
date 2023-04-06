package com.ave.vastgui.tools.utils.permission

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/6
// Description:
// Documentation:
// Reference:

/**
 * PermissionLauncher
 *
 * @since 0.2.0
 */
class PermissionLauncher<I, O>(
    activityResultCaller: ActivityResultCaller,
    activityResultContract: ActivityResultContract<I, O>
) {

    private var activityResultCallback: ActivityResultCallback<O>? = null


    private val launcher: ActivityResultLauncher<I> =
        activityResultCaller.registerForActivityResult(activityResultContract) {
            activityResultCallback?.onActivityResult(it)
        }


    fun launch(input: I, activityResultCallback: ActivityResultCallback<O>?) {
        this.activityResultCallback = activityResultCallback
        launcher.launch(input)
    }

    fun unregister() {
        launcher.unregister()
    }

}