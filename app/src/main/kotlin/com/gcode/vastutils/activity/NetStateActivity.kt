/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastutils.activity

import android.os.*
import com.gcode.vastnetstatelayout.interfaces.VastEmptyDataListener
import com.gcode.vastnetstatelayout.interfaces.VastLoadingErrorListener
import com.gcode.vastnetstatelayout.interfaces.VastLoadingListener
import com.gcode.vastnetstatelayout.interfaces.VastNetErrorListener
import com.gcode.vastnetstatelayout.view.VastNetStateMgr
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vastutils.R
import com.gcode.vastutils.databinding.ActivityNetStateBinding


class NetStateActivity : VastVbActivity<ActivityNetStateBinding>() {

    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            mBinding.netStateLayout.showSuccess()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vastNetStateMgr = VastNetStateMgr(this)
        vastNetStateMgr.setNetErrorListener(object :VastNetErrorListener{
            override fun onNetWorkError() {
                // Something to do when network error
            }
        })
        vastNetStateMgr.setLoadingListener(object : VastLoadingListener {
            override fun onLoading() {
                object:Handler(Looper.getMainLooper()) {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        mBinding.netStateLayout.showSuccess()
                    }
                }.sendEmptyMessageDelayed(0, 1000)
            }
        })
        vastNetStateMgr.setLoadingErrorListener(object :VastLoadingErrorListener{
            override fun onLoadingError() {
                // Something to do when loading error
            }
        })
        vastNetStateMgr.setEmptyDataListener(object :VastEmptyDataListener{
            override fun onEmptyData() {
                // Something to do when empty data
            }
        })
        vastNetStateMgr.setNetErrorView(R.layout.error_page)
        mBinding.netStateLayout.setVastNetStateMgr(vastNetStateMgr)
        mBinding.netStateLayout.showNetError()

        mHandler.sendEmptyMessageDelayed(0, 1000)
    }
}