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

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.splashscreen.SplashScreen;

import cn.govast.vasttools.activity.VastVbActivity;
import cn.govast.vasttools.utils.DownloadUtils;
import cn.govast.vasttools.utils.FileUtils;
import cn.govast.vasttools.utils.LogUtils;
import cn.govast.vastutils.Constant;
import cn.govast.vastutils.databinding.ActivityTestBinding;
import cn.govast.vastutils.network.NetworkRetrofitBuilder;


public class TestActivity extends VastVbActivity<ActivityTestBinding> {

    private SplashScreen splashScreen;
    private final String saveDir = FileUtils.appInternalFilesDir().getPath();
    private final NetworkRetrofitBuilder networkRetrofitBuilder = new NetworkRetrofitBuilder();;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        DownloadUtils.createConfig()
                .setDownloadUrl(Constant.DOWNLOAD_URL)
                .setSaveDir(saveDir)
                .setDownloading(progressInfo -> {
                    LogUtils.i(getDefaultTag(), "downloading" + progressInfo.getPercent());
                    return null;
                })
                .setDownloadFailed(it -> {
                    LogUtils.i(getDefaultTag(), "download failed:" + it.getMessage());
                    return null;
                })
                .setDownloadSuccess(() -> {
                    LogUtils.i(getDefaultTag(), "download success.");
                    return null;
                })
                .download();

    }

}