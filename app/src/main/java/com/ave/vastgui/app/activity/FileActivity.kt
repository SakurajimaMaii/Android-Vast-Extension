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

package com.ave.vastgui.app.activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.tools.activity.VastVbActivity


// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Description: 
// Documentation:

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 删除文件
//        val delete = deleteFile(File(appExternalFilesDir(null).path, "save.txt"))
//        LogUtils.i(getDefaultTag(), "文件删除结果${delete.successOrNull() ?: "失败"}")

        // 保存文件
//        val save = saveFile(File(appExternalFilesDir(null).path, "save.txt"))
//        LogUtils.i(getDefaultTag(), "文件保存结果${save.successOrNull() ?: "失败"}")

        // 重命名文件
        // val rename = rename(File(appInternalFilesDir().path, "save.txt"),"newname.txt")
        // LogUtils.i(getDefaultTag(), "文件重命名结果${rename.successOrNull() ?: "失败"}")

        // 创建文件夹
        // val saveDir = makeDir(File(appInternalFilesDir().path + File.separator + "newDir"))
        // LogUtils.i(getDefaultTag(), "文件夹创建结果${saveDir.successOrNull() ?: "失败"}")

        // 删除文件夹
        // val deleteDir = deleteDir(File(appInternalFilesDir().path + File.separator + "newDir"))
        // LogUtils.i(getDefaultTag(), "文件夹创建结果${deleteDir.successOrNull() ?: "失败"}")

        // 复制文件
        // val copyFile = copyFile(
        //     File(appInternalFilesDir().path, "newname.txt"),
        //     File(appInternalFilesDir().path, "newname_copy.txt")
        // )
        // LogUtils.i(getDefaultTag(), "文件复制结果${copyFile.successOrNull() ?: "失败"}")

        // 复制文件夹
        // val copyDir = copyDir(
        //     File(appInternalFilesDir().path),
        //     File(appInternalFilesDir().path + File.separator + "newDir2")
        // )
        // LogUtils.i(getDefaultTag(), "文件夹复制结果${copyDir.successOrNull() ?: "失败"}")

        // 移动文件夹
        // val moveDir = moveDir(
        //     File(appInternalFilesDir().path),
        //     "${appInternalFilesDir().path}${File.separator}moveDir"
        // )
        // LogUtils.i(getDefaultTag(), "文件夹移动结果${moveDir.successOrNull() ?: "失败"}")

        // val moveFile = moveFile(
        //     File("${appInternalFilesDir().path}${File.separator}moveDir${File.separator}1.txt"),
        //     appInternalFilesDir().path
        // )
        // LogUtils.i(getDefaultTag(), "文件移动结果${moveFile.exceptionOrNull() ?: "成功"}")
    }

}