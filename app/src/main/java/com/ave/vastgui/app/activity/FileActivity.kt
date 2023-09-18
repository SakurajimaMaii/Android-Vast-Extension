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

import android.os.Bundle
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appExternalFilesDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appInternalFilesDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.copyDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.copyFile
import com.ave.vastgui.tools.manager.filemgr.FileMgr.deleteDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.makeDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.moveDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.moveFile
import java.io.File

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    private val logger = mLogFactory.getLog(FileActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 删除文件
        val delete = FileMgr.deleteFile(File(appExternalFilesDir(null).path, "save.txt"))
        logger.i(getDefaultTag(), "文件删除结果${delete.isSuccess ?: "失败"}")

        // 保存文件
        val save = FileMgr.saveFile(File(appExternalFilesDir(null).path, "save.txt"))
        logger.i(getDefaultTag(), "文件保存结果${save.isSuccess ?: "失败"}")

        // 重命名文件
        val rename = FileMgr.rename(File(appInternalFilesDir().path, "save.txt"), "newname.txt")
        logger.i(getDefaultTag(), "文件重命名结果${rename.isSuccess ?: "失败"}")

        // 创建文件夹
        val saveDir = makeDir(File(appInternalFilesDir().path + File.separator + "newDir"))
        logger.i(getDefaultTag(), "文件夹创建结果${saveDir.isSuccess ?: "失败"}")

        // 删除文件夹
        val deleteDir = deleteDir(File(appInternalFilesDir().path + File.separator + "newDir"))
        logger.i(getDefaultTag(), "文件夹创建结果${deleteDir.isSuccess ?: "失败"}")

        // 复制文件
        val copyFile = copyFile(
            File(appInternalFilesDir().path, "newname.txt"),
            File(appInternalFilesDir().path, "newname_copy.txt")
        )
        logger.i(getDefaultTag(), "文件复制结果${copyFile.isSuccess ?: "失败"}")

        // 复制文件夹
        val copyDir = copyDir(
            File(appInternalFilesDir().path),
            File(appInternalFilesDir().path + File.separator + "newDir2")
        )
        logger.i(getDefaultTag(), "文件夹复制结果${copyDir.isSuccess ?: "失败"}")

        // 移动文件夹
        val moveDir = moveDir(
            File(appInternalFilesDir().path),
            "${appInternalFilesDir().path}${File.separator}moveDir"
        )
        logger.i(getDefaultTag(), "文件夹移动结果${moveDir.isSuccess ?: "失败"}")

        val moveFile = moveFile(
            File("${appInternalFilesDir().path}${File.separator}moveDir${File.separator}1.txt"),
            appInternalFilesDir().path
        )
        logger.i(getDefaultTag(), "文件移动结果${moveFile.exceptionOrNull() ?: "成功"}")
    }

}