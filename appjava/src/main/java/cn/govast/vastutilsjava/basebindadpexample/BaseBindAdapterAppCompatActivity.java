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

package cn.govast.vastutilsjava.basebindadpexample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cn.govast.vastadapter.interfaces.VAapClickEventListener;
import cn.govast.vastadapter.interfaces.VAdpLongClickEventListener;
import cn.govast.vastadapter.interfaces.VastBindAdapterItem;
import cn.govast.vasttools.activity.VastVbActivity;
import cn.govast.vasttools.utils.ToastUtils;
import cn.govast.vastutilsjava.R;
import cn.govast.vastutilsjava.basebindadpexample.model.Picture;
import cn.govast.vastutilsjava.databinding.ActivityBaseBindAdapterBinding;

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