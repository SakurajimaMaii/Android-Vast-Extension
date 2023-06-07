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
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityNetStateBinding
import com.ave.vastgui.app.network.service.Request2Service
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.netstatelayout.view.VastNetStateMgr
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.network.request.Request2
import com.ave.vastgui.tools.network.request.RequestBuilder
import com.ave.vastgui.tools.network.request.getApi
import com.ave.vastgui.tools.utils.DateUtils
import kotlinx.coroutines.launch


class NetStateActivity : VastVbVmActivity<ActivityNetStateBinding, NetVM>() {

    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            RequestBuilder("www.yourapi.com")
                .getApi(Request2Service::class.java) {
                    generateQRCode(DateUtils.getCurrentTime())
                }.collect {
                    with(getBinding().netStateLayout){
                        when(it){
                            is Request2.Success ->text =  it.data.data?.unikey.toString()
                            is Request2.Empty -> this.showEmptyData()
                            is Request2.Exception -> this.showLoadingError()
                            is Request2.Failure -> this.showLoadingError()
                        }
                    }
                }
        }

        val vastNetStateMgr = VastNetStateMgr(this)
            .setNetErrorListener {
                // Something to do when network error
            }
            .setLoadingListener {
                // Something to do when loading
            }
            .setLoadingErrorListener {
                // Something to do when loading error
            }
            .setEmptyDataListener {
                // Something to do when empty data
            }
            .setNetErrorView(R.layout.error_page)

        getBinding().netStateLayout.setVastNetStateMgr(vastNetStateMgr)

    }
}