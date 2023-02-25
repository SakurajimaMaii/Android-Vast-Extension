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

package com.ave.vastgui.tools.manager.mediafilemgr

import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DateUtils
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/2
// Description: 
// Documentation:
// Reference:

sealed class MediaFileMgr: MediaFileProvider {

    /**
     * Get default file directory name.
     *
     * @param type [SupportMediaType]
     */
    override fun getDefaultRootDirPath(type: SupportMediaType):String? {
        return when(type){
            SupportMediaType.Images -> {
                FileMgr.appExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
            }
            SupportMediaType.Music ->{
                FileMgr.appExternalFilesDir(Environment.DIRECTORY_MUSIC)?.path
            }
        }
    }

    /**
     * Get default file name.
     *
     * @return For example, IMG_20221022_170455_com_govast_vasttools.jpg
     */
    override fun getDefaultFileName(type: SupportMediaType): String {
        val fileType = type.name
        val timeStamp: String = DateUtils.getCurrentTime("yyyyMMdd_HHmmss")
        return "${fileType}_${timeStamp}_${AppUtils.getPackageName()?.replace(".", "_")}.jpg"
    }

    override fun getFileUriAboveApi24(file: File, authority: String?): Uri {
        return authority?.let {
            FileProvider.getUriForFile(ContextHelper.getAppContext(), it, file)
        } ?: FileProvider.getUriForFile(ContextHelper.getAppContext(), AppUtils.getPackageName() ?: "" , file)
    }

    override fun getFileUriOnApi23(file: File): Uri {
        return Uri.fromFile(file)
    }

}