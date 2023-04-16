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
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.databinding.ActivityMediaBinding
import com.ave.vastgui.app.model.Singleton
import com.ave.vastgui.app.sharedpreferences.EncryptedSp
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.manager.mediafilemgr.MusicMgr
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.viewbinding.reflexViewBinding
import java.io.File


class MediaActivity : AppCompatActivity() {

    private val mBindings by reflexViewBinding(ActivityMediaBinding::bind)

    private val mSp by lazy { EncryptedSp(defaultLogTag()) }

    private val singleton by lazy {
        Singleton.getInstance(defaultLogTag())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSp.count = 1f
        val count = mSp.count
        val file = File(FileMgr.appInternalFilesDir().path, "save.txt")
//        val res = FileMgr.saveFile(file)
//        val uri = FileProvider.getUriForFile(
//            this,
//            AppUtils.getPackageName() ?: "",
//            file
//        )
//        LogUtils.i(defaultLogTag(), res.toString())
        LogUtils.d(
            defaultLogTag(),
            FileMgr.getMimeType(file)
        )
        LogUtils.d(
            defaultLogTag(),
            ImageMgr.getDefaultFileName(".jpg")
        )
        LogUtils.d(
            defaultLogTag(),
            MusicMgr.getDefaultFileName(".mp3")
        )
    }

}