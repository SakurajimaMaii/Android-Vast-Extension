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

import androidx.core.view.isGone
import android.os.Bundle
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appInternalFilesDir
import java.io.File

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    private val logger = mLogFactory.getLog(FileActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存文件
//        val save = saveFile(File(appInternalFilesDir().path, "save.txt"))
//        logger.d("文件保存结果 $save")
        // 移动文件
//        val move = moveFile(File(appInternalFilesDir().path, "save.txt"), appInternalCacheDir().path)
//        logger.d("文件移动结果 $move")
        // 创建文件夹
//        val saveDir = makeDir(File(appInternalFilesDir().path, "newDir"))
//        logger.d("文件夹创建结果 $saveDir")
        // 复制文件夹
//        val copyDir = copyDir(File(appInternalFilesDir().path), File(appInternalFilesDir().path,"newDir2"))
//        logger.d("文件夹复制结果 $copyDir")
        // 删除文件
//        val delete = FileMgr.deleteFile(File(appInternalFilesDir().path, "save.txt"))
//        logger.d(getDefaultTag(), "文件删除结果 $delete")
        // 重命名文件
//        val rename = FileMgr.rename(File(appInternalFilesDir().path, "save.txt"), "newname.txt")
//        logger.d( "文件重命名结果 $rename")
        // 删除文件夹
//        val deleteDir = deleteDir(File(appInternalFilesDir().path, "newDir"))
//        logger.d("文件夹删除结果 $deleteDir")
        // 复制文件
//        val copyFile = copyFile(
//            File(appInternalFilesDir().path, "save.txt"),
//            File(appInternalFilesDir().path, "save_copy.txt")
//        )
//        logger.d("文件复制结果 $copyFile")
        // 移动文件夹
//        val moveDir = moveDir(
//            File(appInternalFilesDir().path),
//            "${appInternalFilesDir().path}${File.separator}moveDir"
//        )
//        logger.d("文件夹移动结果 $moveDir")
        saveBitmap()
    }

    private fun saveBitmap() {
        val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_github, this)
        getBinding().image.setImageBitmap(bitmap)
        BmpUtils.saveBitmapAsFile(bitmap, File(appInternalFilesDir(), "bitmap.jpg"))?.apply {
            logger.d("图像${name}保存成功")
        }
    }

}