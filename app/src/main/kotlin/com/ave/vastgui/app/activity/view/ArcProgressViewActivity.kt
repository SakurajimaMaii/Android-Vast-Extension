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

package com.ave.vastgui.app.activity.view

import android.Manifest
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityArcProgressViewBinding
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.download.DLManager
import com.ave.vastgui.tools.utils.download.DLTask
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.view.extension.viewSnapshot
import com.ave.vastgui.tools.view.toast.SimpleToast
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : VastVbActivity<ActivityArcProgressViewBinding>() {

    private val mLogger = mLogFactory.getLogCat(ArcProgressViewActivity::class.java)
    private lateinit var downloadTask: DLTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate")

        requestPermissions(arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), 1)

        val colors = intArrayOf(
            ColorUtils.colorHex2Int("#F60C0C"),
            ColorUtils.colorHex2Int("#F3B913"),
            ColorUtils.colorHex2Int("#E7F716"),
            ColorUtils.colorHex2Int("#3DF30B"),
            ColorUtils.colorHex2Int("#0DF6EF"),
            ColorUtils.colorHex2Int("#0829FB"),
            ColorUtils.colorHex2Int("#B709F4")
        )
        val pos = floatArrayOf(1f / 7, 2f / 7, 3f / 7, 4f / 7, 5f / 7, 6f / 7, 1f)

        getBinding().arcProgressView.apply {
            mProgressShader = LinearGradient(
                -700f, 0f, 700f, 0f,
                colors, pos,
                Shader.TileMode.CLAMP
            )
            mEndpointCircleRadius = 15f.DP.coerceAtLeast(recommendedRadius())
            mProgressWidth = 15f.DP
            mEndpointCircleColor = ColorUtils.colorHex2Int("#eb4d4b")
        }

        getBinding().arcProgressView.setOnClickListener {
            val bitmap = viewSnapshot(it)
            BmpUtils.saveBitmapAsFile(
                bitmap = bitmap,
                File(FileMgr.appInternalFilesDir(), "arc_progress_endpoint_radius.jpg")
            )?.apply {
                SimpleToast.showShortMsg("截图${name}已保存")
            }
        }

        getBinding().download.setOnClickListener {
            mLogger.i("开始下载")
            downloadApk()
        }

        getBinding().pause.setOnClickListener {
            downloadTask.pause()
        }

        getBinding().resume.setOnClickListener {
            downloadTask.resume()
        }

        getBinding().cancel.setOnClickListener {
            downloadTask.cancel()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPause")
    }

    private fun downloadApk() {
        downloadTask = DLManager
            .createTaskConfig()
            .setDownloadUrl("https://d1.music.126.net/dmusic/NeteaseCloudMusic_Music_official_3.0.0.Beta.03.12.202664.64.exe")
            .setSaveDir(FileMgr.appInternalFilesDir().path)
            .setListener {
                onDownloading = {
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress =
                            it.rate * getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onFailure = {
                    mLogger.e("download failed:" + it.exception.stackTraceToString())
                }
                onSuccess = {
                    mLogger.i("download success.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress = getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onCancel = {
                    mLogger.i("download cancel.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        resetProgress()
                    }
                }
            }
            .build()

        downloadTask.start()

//        val url = "https://d1.music.126.net/dmusic/NeteaseCloudMusic_Music_official_3.0.0.Beta.03.12.202664.64.exe"
//        val request = OkGo.get<File>(url)
//        OkDownload.request(url, request)
//            .folder(FileMgr.appInternalFilesDir().path)
//            .save()
//            .register(object : DownloadListener("DOWNLOAD") {
//                override fun onStart(progress: Progress?) {
//
//                }
//
//                override fun onProgress(progress: Progress?) {
//                    getBinding().arcProgressView.refreshWithInvalidate {
//                        mCurrentProgress =
//                            progress!!.fraction * getBinding().arcProgressView.mMaximumProgress
//                    }
//                }
//
//                override fun onError(progress: Progress?) {
//
//                }
//
//                override fun onFinish(t: File?, progress: Progress?) {
//                    mLogger.i("download success.")
//                    getBinding().arcProgressView.refreshWithInvalidate {
//                        mCurrentProgress = getBinding().arcProgressView.mMaximumProgress
//                    }
//                }
//
//                override fun onRemove(progress: Progress?) {
//
//                }
//            })
//            .start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (1 == requestCode) {
            nothing_to_do()
        }
    }

}