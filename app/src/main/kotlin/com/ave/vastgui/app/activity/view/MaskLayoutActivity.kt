/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.view

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.ContactAdapter
import com.ave.vastgui.app.databinding.ActivityMaskLayoutBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.app.sharedpreferences.ThemeSp
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.annotation.ExperimentalView
import com.ave.vastgui.tools.io.getAssetsFile
import com.ave.vastgui.tools.utils.IntentUtils
import com.ave.vastgui.tools.utils.permission.requestPermission
import com.ave.vastgui.tools.view.masklayout.MaskAnimation
import com.ave.vastgui.tools.view.masklayout.MaskLayout
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding
import org.alee.component.skin.service.ThemeSkinService

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/masklayout/masklayout/

/** [MaskLayoutActivity]. */
@OptIn(ExperimentalView::class)
class MaskLayoutActivity : AppCompatActivity(R.layout.activity_mask_layout),
    MaskLayout.MaskAnimationListener {

    private val logcat = logFactory(MaskLayoutActivity::class.java)

    private val binding: ActivityMaskLayoutBinding by viewBinding(ActivityMaskLayoutBinding::bind)

    private val adapter: ContactAdapter = ContactAdapter(this)

    private val maskLayout
        get() = binding.maskLayout

    private val darKBtn
        get() = binding.changeDark

    private val contentRv
        get() = binding.recyclerView

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 从 assets 中读取皮肤包
        getAssetsFile("app-skin.skin")

        // 申请读取通讯录权限
        requestPermission(Manifest.permission.READ_CONTACTS) {
            granted = {
                SimpleToast.showShortMsg("已获取通讯录权限")
            }
        }

        // 初始化 UI
        maskLayout.updateCoordinate(binding.changeDark)
        maskLayout.maskDuration = 1000L

        adapter.setOnItemClickListener { _, _, item ->
            if (null == item) return@setOnItemClickListener
            requestPermission(Manifest.permission.CALL_PHONE) {
                granted = { IntentUtils.dialPhoneNumber(this@MaskLayoutActivity, item.number) }
            }
        }

        contentRv.apply {
            this.adapter = this@MaskLayoutActivity.adapter
            layoutManager = LinearLayoutManager(context)
        }

        darKBtn.setOnClickListener {
            binding.maskLayout.activeMask(MaskAnimation.COLLAPSED, this)
        }

        // 读取通讯录
        readContacts()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMaskComplete() {
        logcat.d { "当前的主题模式：${ThemeSp.isDark}" }
        if (ThemeSp.isDark) {
            ThemeSkinService.getInstance().switchThemeSkin(0)
        } else {
            ThemeSkinService.getInstance().switchThemeSkin(1)
        }
        ThemeSp.isDark = !ThemeSp.isDark
        adapter.notifyDataSetChanged()
    }

    override fun onMaskFinished() {
        nothing_to_do()
    }

    // 读取通讯录
    private fun readContacts() {
        val cursor: Cursor? = contentResolver
            .query(Phone.CONTENT_URI, arrayOf(Phone.DISPLAY_NAME, Phone.NUMBER), null, null, checkPhonebooLabel())
        while (cursor?.moveToNext() == true) {
            val name: String =
                cursor.getStringOrNull(cursor.getColumnIndex(Phone.DISPLAY_NAME)).toString()
            val number: String =
                cursor.getStringOrNull(cursor.getColumnIndex(Phone.NUMBER)).toString()
            adapter.addContact(name.first().toString(), "${number.first()}**********")
        }
        cursor?.close()
    }

    /** 检查数据库是否包含 **phonebook_label** 字段， 如果没有则用 [Phone.SORT_KEY_PRIMARY] 替代。 */
    private fun checkPhonebooLabel(): String {
        var cursor: Cursor? = null
        var key = "phonebook_label"
        try {
            cursor = contentResolver
                .query(Phone.CONTENT_URI, arrayOf("phonebook_label"), null, null, null)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            key = Phone.SORT_KEY_PRIMARY
        } finally {
            cursor?.close()
        }
        return key
    }

}