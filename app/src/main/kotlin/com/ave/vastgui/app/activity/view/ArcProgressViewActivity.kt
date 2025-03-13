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
import com.ave.vastgui.app.databinding.ActivityArcProgressViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.io.appInternalFilesDir
import com.ave.vastgui.tools.io.destroy
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.download.DLManager
import com.ave.vastgui.tools.utils.download.DLTask
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.log.vastgui.okhttp.Okhttp3Interceptor
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : VastVbActivity<ActivityArcProgressViewBinding>() {

    private val logger = logFactory.getLogCat(ArcProgressViewActivity::class.java)
    private lateinit var downloadTask: DLTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://developer.android.com/develop/ui/views/layout/edge-to-edge?hl=zh-cn
        // ViewCompat.setOnApplyWindowInsetsListener(getBinding().root) { v, windowInsets ->
        //     val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        //         topMargin = insets.top
        //         leftMargin = insets.left
        //         bottomMargin = insets.bottom
        //         rightMargin = insets.right
        //     }
        //     WindowInsetsCompat.CONSUMED
        // }

        requestMultiplePermissions(arrayOf(Manifest.permission.ACCESS_NETWORK_STATE))

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

        getBinding().download.setOnClickListener {
            logger.i("开始下载")
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

    private fun downloadApk() {
        DLManager.setLogger(Okhttp3Interceptor(logger))

        val root = appInternalFilesDir()
        val name = "ShapeButton-0.0.5.zip"
        val file = File(root, name)
        downloadTask = DLManager
            .createTaskConfig()
            .setDownloadUrl("https://codeload.github.com/SakurajimaMaii/ShapeButton/zip/refs/tags/0.0.5")
            .setSaveDir(root.path)
            .setSaveName(name)
            .setMD5("9d9c7426b8af3e630f66492d70222339")
            .setListener {
                onDownloading = {
                    val count = (it.rate * getBinding().arcProgressView.mMaximumProgress).coerceIn(0f, 100f)
                    logger.i("当前下载进度:${count}，最大进度:${getBinding().arcProgressView.mMaximumProgress}")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress =
                            it.rate * getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onFailure = {
                    logger.e("download failed:" + it.exception.stackTraceToString())
                }
                onSuccess = {
                    logger.i("download success.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        mCurrentProgress = getBinding().arcProgressView.mMaximumProgress
                    }
                }
                onCancel = {
                    logger.i("download cancel.")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        resetProgress()
                    }
                }
            }
            .build()
        if (file.exists()) {
            if (file.destroy().isSuccess) {
                downloadTask.start()
            }
        } else {
            downloadTask.start()
        }
    }

}