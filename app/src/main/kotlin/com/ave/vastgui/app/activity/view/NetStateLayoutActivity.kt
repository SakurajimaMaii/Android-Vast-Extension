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

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityNetStateLayoutBinding
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.netstatelayout.NetStateMgr
import com.ave.vastgui.netstatelayout.listener.OnLoadingErrorListener
import com.ave.vastgui.netstatelayout.listener.OnNetErrorListener
import com.ave.vastgui.tools.activity.VastVmActivity
import com.ave.vastgui.tools.utils.NetStateUtils
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/3 11:31

class NetStateLayoutActivity(override val layoutId: Int = R.layout.activity_net_state_layout) :
    VastVmActivity<NetVM>(), OnNetErrorListener, OnLoadingErrorListener {

    private val mBinding by viewBinding(ActivityNetStateLayoutBinding::bind)
    private val mNetStateLayout by lazy {
        mBinding.netStateLayout
    }
    private val wifiSetting =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (NetStateUtils.isNetworkAvailable(this)) {
                mNetStateLayout.showSuccess()
            } else {
                mNetStateLayout.showNetError()
            }
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val netStateMgr = NetStateMgr(this).apply {
            setOnNetErrorListener(this@NetStateLayoutActivity)
            setOnLoadingErrorListener(this@NetStateLayoutActivity)
            setEmptyDataView(R.layout.page_empty_data_1)
        }
        mNetStateLayout.setNetStateMgr(netStateMgr)

        if (!NetStateUtils.isNetworkAvailable(this)) {
            mNetStateLayout.showNetError(RuntimeException("网络不可用"))
        } else {
            getSnackbar().setText("网络可用").show()
        }

        mBinding.anslBtn.setOnClickListener {
            getViewModel().getVideos(0, 10)
        }

        getViewModel().videos.observeState(this) {
            onFailed = { code, message ->
                mNetStateLayout.showLoadingError(code, message)
            }
            onError = { exception ->
                mNetStateLayout.showNetError(exception)
            }
            onEmpty = {
                mNetStateLayout.showEmptyData()
            }
        }

        getViewModel().videos.observe(this) {
            mBinding.anslSentences.text = it.message
        }
    }

    override fun onNetErrorClick() {
        wifiSetting.launch(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    override fun onLoadingErrorClick() {
        mNetStateLayout.showSuccess()
    }

}