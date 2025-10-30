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

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ave.vastgui.app.databinding.ActivityArcProgressViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.core.annotation.ExperimentalApi
import com.ave.vastgui.tools.activity.BaseVbActivity
import com.ave.vastgui.tools.io.appInternalFilesDir
import com.ave.vastgui.tools.io.destroy
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.download.DownloadTask
import com.ave.vastgui.tools.utils.download.core.DownloadState
import com.ave.vastgui.tools.utils.download.interfaces.OnDownloadListener
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.log.vastgui.okhttp.Okhttp3Interceptor
import okhttp3.OkHttpClient
import java.io.File

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:42
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/arc-progress-view/

class ArcProgressViewActivity : BaseVbActivity<ActivityArcProgressViewBinding>() {

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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        enableActionBar = false

        binding.switchProgressColorBtn.setOnClickListener {
            enableSystemBar = !enableSystemBar
            // binding.arcProgressView.progressColor = colors[(progressColorIndex++) % colors.size]
        }

        binding.switchProgressBackgroundColorBtn.setOnClickListener {
            binding.arcProgressView.progressBackgroundColor = colors[(progressBackgroundColorIndex++) % colors.size]
        }

        binding.switchProgressShaderBtn.setOnClickListener {
            binding.arcProgressView.progressShader = shaders[(++shaderIndex) % shaders.size]
        }

        binding.switchTextShowBtn.setOnClickListener {
            binding.arcProgressView.showText = !binding.arcProgressView.showText
        }

        binding.switchTextColorBtn.setOnClickListener {
            binding.arcProgressView.textColor = colors[(textColorIndex++) % colors.size]
        }

        binding.switchStartpointColorBtn.setOnClickListener {
            binding.arcProgressView.startpointCircleColor = colors[(startpointColorIndex++) % colors.size]
        }

        binding.switchEndpointColorBtn.setOnClickListener {
            binding.arcProgressView.endpointCircleColor = colors[(endpointColorIndex++) % colors.size]
        }

        binding.progressTextSizeSlider.addOnChangeListener { _, value, _ ->
            binding.arcProgressView.textSize = value.SP
        }

        binding.progressRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.arcProgressView.progressRadius = value.DP
        }

        binding.progressWidthSlider.addOnChangeListener { _, value, _ ->
            binding.arcProgressView.progressWidth = value.DP
        }

        binding.progressEndpointRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.arcProgressView.endpointCircleRadius = value.DP
        }

        binding.progressSlider.valueTo = binding.arcProgressView.maximumProgress
        binding.progressSlider.value = binding.arcProgressView.currentProgress
        binding.progressSlider.addOnChangeListener { _, value, _ ->
            binding.arcProgressView.currentProgress = value
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
                    binding.arcProgressView.refreshWithInvalidate {
                        currentProgress = binding.arcProgressView.maximumProgress
                    }
                }

                override fun onDownload(state: DownloadState.Download) = runOnUiThread {
                    binding.arcProgressView.refreshWithInvalidate {
                        currentProgress =
                            state.rate * binding.arcProgressView.maximumProgress
                    }
                }

                override fun onFailure(state: DownloadState.Failure) = runOnUiThread {
                    logger.e("任务下载失败" + state.exception.stackTraceToString())
                }

                override fun onTerminate() = runOnUiThread {
                    logger.i("任务被取消")
                    binding.arcProgressView.refreshWithInvalidate {
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