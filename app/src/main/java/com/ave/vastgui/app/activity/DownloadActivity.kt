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

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.databinding.ActivityDownloadBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DownloadUtils
import com.ave.vastgui.tools.utils.LogUtils

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Description:
// Documentation:

class DownloadActivity : VastVbActivity<ActivityDownloadBinding>() {

    private val saveDir = FileMgr.appInternalFilesDir().path

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val colors = intArrayOf(
//            ColorUtils.colorHex2Int("#F60C0C"),
//            ColorUtils.colorHex2Int("#F3B913"),
//            ColorUtils.colorHex2Int("#E7F716"),
//            ColorUtils.colorHex2Int("#3DF30B"),
//            ColorUtils.colorHex2Int("#0DF6EF"),
//            ColorUtils.colorHex2Int("#0829FB"),
//            ColorUtils.colorHex2Int("#B709F4")
//        )
//        val pos = floatArrayOf(
//            1f / 7,
//            2f / 7,
//            3f / 7,
//            4f / 7,
//            5f / 7,
//            6f / 7,
//            1f
//        )

        val colors = intArrayOf(
            ColorUtils.colorHex2Int("#ED4C67"),
            ColorUtils.colorHex2Int("#FFFFFF"),
        )
        val pos = floatArrayOf(
            0.3f,
            1f
        )

        getBinding().downloadCv.apply {
            setProgressShader(
                LinearGradient(
                    -700f, 0f, 700f, 0f,
                    colors, pos,
                    Shader.TileMode.CLAMP
                )
            )
            setProgressStrokeCap(Paint.Cap.ROUND)
            setProgressEndColor(ColorUtils.colorHex2Int("#4286f4"))
            setProgressStartAndEndEnabled(false)
        }

        getBinding().download.setOnClickListener {
            downloadApk()
        }

//        val uri = CropImageUtils.createCropFile(this,true)
//        LogUtils.d(getDefaultTag(), uri.toString())
//        getContext().contentResolver.delete(Uri.parse("content://media/external/images/media/326808"),null)
//        if (uri != null) {
//            getContext().contentResolver.delete(uri,null)
//        }
//        getContext().contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DISPLAY_NAME + "=?",
//            arrayOf("IMG_20221021_170812_CROP.jpg")
//        )
//        val proj = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
//        val cursor: Cursor? = getContext().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null)
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
//                val path: String = cursor.getString(columnIndex)
//                LogUtils.d(getDefaultTag(), path.toString())
//            }
//            cursor.close()
//        }
    }

    private fun downloadApk() {
        DownloadUtils
            .createConfig()
            .setDownloadUrl(com.ave.vastgui.app.Constant.DOWNLOAD_URL)
            .setSaveDir(saveDir)
            .setDownloading { progress->
                LogUtils.i(getDefaultTag(), "downloading ${progress?.percent?.toFloat()}")
                getBinding().downloadCv.setProgress((progress?.percent?.toFloat() ?: 0f) / 100f)
            }
            .setDownloadFailed {
                LogUtils.i(getDefaultTag(), "download failed:" + it?.message)
            }
            .setDownloadSuccess {
                LogUtils.i(getDefaultTag(), "download success.")
                getBinding().downloadCv.setProgress(1f)
            }
            .download()
    }

}