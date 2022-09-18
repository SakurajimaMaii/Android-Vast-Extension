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

package com.gcode.vastutilsjava.basebindadpexample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gcode.vastadapter.interfaces.VAapClickEventListener;
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener;
import com.gcode.vastadapter.interfaces.VastBindAdapterItem;
import com.gcode.vasttools.activity.VastVbActivity;
import com.gcode.vasttools.utils.ToastUtils;
import com.gcode.vastutilsjava.R;
import com.gcode.vastutilsjava.basebindadpexample.model.Picture;
import com.gcode.vastutilsjava.databinding.ActivityBaseBindAdapterBinding;

import java.util.ArrayList;

public class BaseBindAdapterAppCompatActivity extends VastVbActivity<ActivityBaseBindAdapterBinding> {

    private ArrayList<VastBindAdapterItem> datas = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VAapClickEventListener click = (view, pos) -> {
            ToastUtils.showShortMsg(this, "Click event and pos is " + pos);
        };

        VAdpLongClickEventListener longClick = (view, pos) -> {
            ToastUtils.showShortMsg(this, "Long click event and pos is " + pos);
            return true;
        };

        for (int i = 0; i < 10; i++) {
            datas.add(new Picture(R.drawable.ic_knots, click, longClick));
        }

        BaseBindAdapter adapter = new BaseBindAdapter(datas, this);

        adapter.setOnItemClickListener((view, position) -> {
            // Something you want to do
        });
        adapter.setOnItemLongClickListener((view, position) -> {
            // Something you want to do
            return true;
        });

        getBinding().dataRv.setAdapter(adapter);
        getBinding().dataRv.setLayoutManager(new LinearLayoutManager(this));
    }

}