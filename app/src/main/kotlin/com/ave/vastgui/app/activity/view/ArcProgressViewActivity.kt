/*
 * Copyright 2021-2025 VastGui
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
import com.ave.vastgui.core.annotation.ExperimentalApi
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.io.appInternalFilesDir
import com.ave.vastgui.tools.io.destroy
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.download.DownloadTask
import com.ave.vastgui.tools.utils.download.core.DownloadState
import com.ave.vastgui.tools.utils.download.interfaces.OnDownloadListener
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.log.vastgui.okhttp.Okhttp3Interceptor
import okhttp3.OkHttpClient
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : VastVbActivity<ActivityArcProgressViewBinding>() {

    private val logger = logFactory.getLogCat(ArcProgressViewActivity::class.java)

    private val colors = intArrayOf(
        ColorUtils.colorHex2Int("#F60C0C"),
        ColorUtils.colorHex2Int("#F3B913"),
        ColorUtils.colorHex2Int("#E7F716"),
        ColorUtils.colorHex2Int("#3DF30B"),
        ColorUtils.colorHex2Int("#0DF6EF"),
        ColorUtils.colorHex2Int("#0829FB"),
        ColorUtils.colorHex2Int("#B709F4")
    )

    private var progressColorIndex = 0

    private var progressBackgroundColorIndex = 0

    private val pos = floatArrayOf(1f / 7, 2f / 7, 3f / 7, 4f / 7, 5f / 7, 6f / 7, 1f)

    private val shaders = arrayOf(null, LinearGradient(-700f, 0f, 700f, 0f, colors, pos, Shader.TileMode.CLAMP))

    private var shaderIndex = 0

    private var textColorIndex = 0

    private var startpointColorIndex = 0

    private var endpointColorIndex = 0

    private lateinit var downloadTask: DownloadTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getBinding().switchProgressColorBtn.setOnClickListener {
            getBinding().arcProgressView.progressColor = colors[(progressColorIndex++) % colors.size]
        }

        getBinding().switchProgressBackgroundColorBtn.setOnClickListener {
            getBinding().arcProgressView.progressBackgroundColor = colors[(progressBackgroundColorIndex++) % colors.size]
        }

        getBinding().switchProgressShaderBtn.setOnClickListener {
            getBinding().arcProgressView.progressShader = shaders[(++shaderIndex) % shaders.size]
        }

        getBinding().switchTextShowBtn.setOnClickListener {
            getBinding().arcProgressView.showText = !getBinding().arcProgressView.showText
        }

        getBinding().switchTextColorBtn.setOnClickListener {
            getBinding().arcProgressView.textColor = colors[(textColorIndex++) % colors.size]
        }

        getBinding().switchStartpointColorBtn.setOnClickListener {
            getBinding().arcProgressView.startpointCircleColor = colors[(startpointColorIndex++) % colors.size]
        }

        getBinding().switchEndpointColorBtn.setOnClickListener {
            getBinding().arcProgressView.endpointCircleColor = colors[(endpointColorIndex++) % colors.size]
        }

        getBinding().progressTextSizeSlider.addOnChangeListener { _, value, _ ->
            getBinding().arcProgressView.textSize = value.SP
        }

        getBinding().progressRadiusSlider.addOnChangeListener { _, value, _ ->
            getBinding().arcProgressView.progressRadius = value.DP
        }

        getBinding().progressWidthSlider.addOnChangeListener { _, value, _ ->
            getBinding().arcProgressView.progressWidth = value.DP
        }

        getBinding().progressEndpointRadiusSlider.addOnChangeListener { _, value, _ ->
            getBinding().arcProgressView.endpointCircleRadius = value.DP
        }

        getBinding().progressSlider.valueTo = getBinding().arcProgressView.maximumProgress
        getBinding().progressSlider.value = getBinding().arcProgressView.currentProgress
        getBinding().progressSlider.addOnChangeListener { _, value, _ ->
            getBinding().arcProgressView.currentProgress = value
        }

    }

    @OptIn(ExperimentalApi::class)
    private fun downloadApk() {
        val root = appInternalFilesDir()
        val name = "opencv.rar"
        val file = File(root, name)
        val client = OkHttpClient
            .Builder()
            .addInterceptor(Okhttp3Interceptor(logger))
            .build()
        downloadTask = DownloadTask.Builder()
            .setClient(client)
            .setSubTaskCount(16)
            .setDownloadUrl("http://192.168.0.109:7777/static/opencv.rar")
            .setFile(file)
            .setListener(object : OnDownloadListener {
                override fun onSuccess(state: DownloadState.Success) = runOnUiThread {
                    getBinding().arcProgressView.refreshWithInvalidate {
                        currentProgress = getBinding().arcProgressView.maximumProgress
                    }
                }

                override fun onDownload(state: DownloadState.Download) = runOnUiThread {
                    getBinding().arcProgressView.refreshWithInvalidate {
                        currentProgress =
                            state.rate * getBinding().arcProgressView.maximumProgress
                    }
                }

                override fun onFailure(state: DownloadState.Failure) = runOnUiThread {
                    logger.e("任务下载失败" + state.exception.stackTraceToString())
                }

                override fun onTerminate() = runOnUiThread {
                    logger.i("任务被取消")
                    getBinding().arcProgressView.refreshWithInvalidate {
                        resetProgress()
                    }
                }
            })
            .build()
        if (file.exists() && file.destroy().isSuccess) {
            logger.i("任务下载开始")
            downloadTask.start()
        }
    }

}