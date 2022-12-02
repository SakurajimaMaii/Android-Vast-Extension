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

package cn.govast.vastutils.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.govast.vasttools.activity.VastVbVmActivity;
import cn.govast.vasttools.utils.DateUtils;
import cn.govast.vasttools.utils.LogUtils;
import cn.govast.vastutils.databinding.ActivityApiCallBinding;
import cn.govast.vastutils.network.NetworkRetrofitBuilder;
import cn.govast.vastutils.network.Service;
import cn.govast.vastutils.viewmodel.ApiCallVM;


public class ApiCallActivity extends VastVbVmActivity<ActivityApiCallBinding, ApiCallVM> {

    private final NetworkRetrofitBuilder networkRetrofitBuilder = new NetworkRetrofitBuilder();
    private final Service userService = networkRetrofitBuilder.create(Service.class);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService.generateQRCode(DateUtils.getCurrentTime(DateUtils.FORMAT_MM_DD_HH_MM)).request(RspListener -> {
            RspListener.setOnSuccess(qrCodeKey -> {
                getViewModel().qRCodeKey.postValueAndSuccess(qrCodeKey); // 自动切换状态
                return null;
            });
            return null;
        });

        getViewModel().getQRCode();

        getViewModel().qRCodeKey.getState().observeState(this, stateListener -> {
            stateListener.setOnSuccess(() -> {
                LogUtils.i(getDefaultTag(),"请求成功");
                return null;
            });
            return null;
        });
    }

}