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
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.ave.vastgui.app.databinding.ActivityNetStateBinding
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.netstatelayout.view.VastNetStateMgr
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.utils.DateUtils
import com.ave.vastgui.tools.utils.LogUtils


class NetStateActivity : VastVbVmActivity<ActivityNetStateBinding, NetVM>() {

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            // getBinding().netStateLayout.showSuccess()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel().getQRCode_3(DateUtils.getCurrentTime(DateUtils.FORMAT_HH_MM))
        getViewModel().qRCodeKey.observe(this){ value ->
            LogUtils.i(getDefaultTag(),value.data?.unikey)
        }
        getViewModel().qRCodeKey.observeState(this){
            onSuccess = {
                LogUtils.i(getDefaultTag(),"onSuccess")
            }
            onError = {
                LogUtils.i(getDefaultTag(),"onError")
            }
        }

        val vastNetStateMgr = VastNetStateMgr(this)
//            .setNetErrorListener {
//                // Something to do when network error
//            }
//            .setLoadingListener {
//                object : Handler(Looper.getMainLooper()) {
//                    override fun handleMessage(msg: Message) {
//                        super.handleMessage(msg)
//                        getBinding().netStateLayout.showSuccess()
//                    }
//                }.sendEmptyMessageDelayed(0, 1000)
//            }
//            .setLoadingErrorListener {
//                // Something to do when loading error
//            }
//            .setEmptyDataListener {
//                // Something to do when empty data
//            }
//            .setNetErrorView(R.layout.error_page)

        getBinding().netStateLayout.setVastNetStateMgr(vastNetStateMgr)
//        getBinding().netStateLayout.showNetError()
    }
}