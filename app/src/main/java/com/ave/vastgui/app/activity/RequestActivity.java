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

package com.ave.vastgui.app.activity;


import android.os.Bundle;

import com.ave.vastgui.app.R;
import com.ave.vastgui.app.databinding.ActivityRequestBinding;
import com.ave.vastgui.app.network.NetworkRetrofitBuilder;
import com.ave.vastgui.app.network.service.QRService;
import com.ave.vastgui.tools.activity.VastVbActivity;
import com.ave.vastgui.tools.utils.DateUtils;
import com.ave.vastgui.tools.utils.LogUtils;

public class RequestActivity extends VastVbActivity<ActivityRequestBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        new NetworkRetrofitBuilder().create(QRService.class).generateQRCode(DateUtils.getCurrentTime()).request(listener -> {
            listener.setOnSuccess(qrCodeKey -> {
                LogUtils.i(getDefaultTag(),qrCodeKey.getData().getUnikey());
                return null;
            });
            return null;
        });
    }

}