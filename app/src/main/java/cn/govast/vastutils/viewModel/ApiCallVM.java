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

package cn.govast.vastutils.viewModel;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/19
// Description: 
// Documentation:
// Reference:

import cn.govast.vasttools.lifecycle.StateLiveData;
import cn.govast.vasttools.utils.DateUtils;
import cn.govast.vasttools.viewModel.VastViewModel;
import cn.govast.vastutils.network.NetworkRetrofitBuilder;
import cn.govast.vastutils.network.Service;
import cn.govast.vastutils.network.service.QRCodeKey;

public class ApiCallVM extends VastViewModel {

    private final NetworkRetrofitBuilder networkRetrofitBuilder = new NetworkRetrofitBuilder();
    private final Service userService = networkRetrofitBuilder.create(Service.class);
    public StateLiveData<QRCodeKey> qRCodeKey;

    /**
     * 请求二维码信息
     */
    public void getQRCode(){
        userService.generateQRCode(DateUtils.getCurrentTime(DateUtils.FORMAT_MM_DD_HH_MM)).request(qRCodeKey);
    }

}
