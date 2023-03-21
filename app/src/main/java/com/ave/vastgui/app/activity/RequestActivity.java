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

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ave.vastgui.adapter.widget.AdapterClickListener;
import com.ave.vastgui.adapter.widget.AdapterItemWrapper;
import com.ave.vastgui.adapter.widget.AdapterLongClickListener;
import com.ave.vastgui.app.R;
import com.ave.vastgui.app.activity.adpexample.adapter.Adapter1;
import com.ave.vastgui.app.activity.adpexample.adapter.BindAdapter1;
import com.ave.vastgui.app.activity.adpexample.model.Person;
import com.ave.vastgui.app.activity.adpexample.model.PersonHolder;
import com.ave.vastgui.app.activity.adpexample.model.PersonWrapper;
import com.ave.vastgui.app.activity.adpexample.model.Picture;
import com.ave.vastgui.app.activity.adpexample.model.PictureHolder;
import com.ave.vastgui.app.activity.adpexample.model.PictureWrapper;
import com.ave.vastgui.app.databinding.ActivityRequestBinding;
import com.ave.vastgui.app.network.NetworkRetrofitBuilder;
import com.ave.vastgui.app.network.service.QRService;
import com.ave.vastgui.tools.activity.VastVbActivity;
import com.ave.vastgui.tools.utils.DateUtils;
import com.ave.vastgui.tools.utils.LogUtils;
import com.ave.vastgui.tools.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RequestActivity extends VastVbActivity<ActivityRequestBinding> {

    private final ArrayList<AdapterItemWrapper<?>> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        new NetworkRetrofitBuilder().create(QRService.class).generateQRCode(DateUtils.getCurrentTime()).request(listener -> {
            listener.setOnSuccess(qrCodeKey -> {
                LogUtils.i(getDefaultTag(), Objects.requireNonNull(qrCodeKey.getData()).getUnikey());
                return null;
            });
            return null;
        });

        AdapterClickListener click = (view, pos) -> {
            ToastUtils.showShortMsg("Click event and pos is " + pos);
        };

        AdapterLongClickListener longClick = (view, pos) -> {
            ToastUtils.showShortMsg("Long click event and pos is " + pos);
            return true;
        };

        for (int i = 0; i < 10; i++) {
            Picture picture = new Picture(R.drawable.ic_knots);
            PictureWrapper pictureWrapper = new PictureWrapper(picture,click,longClick);
            datas.add(pictureWrapper);
            Person person = new Person(String.valueOf(i),String.valueOf(i));
            PersonWrapper personWrapper = new PersonWrapper(person);
            datas.add(personWrapper);
        }

        // 设置给RecyclerView
        Adapter1 adapter1 = new Adapter1(datas, Arrays.asList(new PersonHolder.Factory(),new PictureHolder.Factory()));
        BindAdapter1 adapter = new BindAdapter1(datas, this);

        adapter.registerClickEvent((view, pos) -> {

        });
        adapter.registerLongClickEvent((view, pos) -> {

            return true;
        });

        getBinding().dataRv.setAdapter(adapter);
        getBinding().dataRv.setLayoutManager(new LinearLayoutManager(this));
    }

}