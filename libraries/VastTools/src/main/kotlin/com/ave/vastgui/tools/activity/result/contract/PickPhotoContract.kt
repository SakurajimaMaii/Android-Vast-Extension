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

package com.ave.vastgui.tools.activity.result.contract

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/23
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/ActivityResult/

class PickPhotoContract : ActivityResultContract<Any?, Uri?>() {

    override fun createIntent(context: Context, input: Any?): Intent {
        return Intent(Intent.ACTION_PICK, null).apply {
            setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }

}