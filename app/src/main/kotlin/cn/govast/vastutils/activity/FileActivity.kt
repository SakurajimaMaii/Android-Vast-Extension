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

package cn.govast.vastutils.activity

import android.os.Bundle
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.activity.result.contract.GetMediaActivityResultContract
import cn.govast.vasttools.manager.filemgr.FileMgr
import cn.govast.vasttools.manager.filemgr.FileMgr.appExternalCacheDir
import cn.govast.vasttools.manager.filemgr.FileMgr.appExternalFilesDir
import cn.govast.vasttools.manager.filemgr.FileMgr.appInternalCacheDir
import cn.govast.vasttools.manager.filemgr.FileMgr.appInternalFilesDir
import cn.govast.vasttools.manager.filemgr.FileMgr.saveFile
import cn.govast.vasttools.utils.LogUtils
import cn.govast.vastutils.databinding.ActivityFileBinding
import java.io.File


// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Description: 
// Documentation:

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    private val openGalleryLauncher =
        registerForActivityResult(GetMediaActivityResultContract()) {
            getBinding().video.apply {
                setVideoURI(it)
                start()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i(getDefaultTag(), appInternalFilesDir().path)
        LogUtils.i(getDefaultTag(), appInternalCacheDir().path)
        LogUtils.i(getDefaultTag(), appExternalFilesDir(null)?.path)
        LogUtils.i(getDefaultTag(), appExternalCacheDir()?.path)

        getBinding().openGallery.setOnClickListener {
            openGalleryLauncher.launch("image/*")
        }

        val res = FileMgr.deleteDir(File(appExternalFilesDir(null)?.path, "save.txt"))

        LogUtils.i(getDefaultTag(), res.toString())

        val res1 = saveFile(File(appExternalFilesDir(null)?.path,"save.txt"))

        LogUtils.i(getDefaultTag(), res1.toString())

//        makeDir(File(appInternalFilesDir().path,"makeDir"))
//
//        makeDir(File(appInternalFilesDir().path,"makeDir2"))
//
//
//        val res1 = writeFile(File(appInternalFilesDir().path, "picture.jpg"),
//            object : FileMgr.WriteEventListener {
//                override fun writeEvent(fileWriter: FileWriter) {
//                    fileWriter.write("Hello")
//                }
//            })
//
//        LogUtils.i(getDefaultTag(), res1.toString())
//
//        val res2 = writeFile(File(appInternalFilesDir().path, "save.txt"),
//            object : FileMgr.WriteEventListener {
//                override fun writeEvent(fileWriter: FileWriter) {
//                    fileWriter.write("Hello")
//                }
//            })

//        LogUtils.i(getDefaultTag(), res2.toString())
    }
}