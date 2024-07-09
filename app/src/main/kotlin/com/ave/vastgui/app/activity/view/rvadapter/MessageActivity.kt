/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.activity.view.rvadapter

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.view.View
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.BaseBindListAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Message
import com.ave.vastgui.app.adapter.entity.MessageDiffUtil
import com.ave.vastgui.app.databinding.ActivityMessageBinding
import com.ave.vastgui.app.fragment.MessageBottomSheet
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.permission.requestMultiplePermissions

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

class MessageActivity : VastVbActivity<ActivityMessageBinding>(),
    OnItemClickListener<Message> {

    private val mMessageRv by lazy { getBinding().messageRv }
    private val mAdapter by lazy {
        BaseBindListAdapter(this, BR.message, MessageDiffUtil)
    }
    private val mSmsColumns =
        arrayOf(
            Telephony.Sms.ADDRESS,
            Telephony.Sms.PERSON,
            Telephony.Sms.BODY,
            Telephony.Sms.TYPE,
            Telephony.Sms.DATE
        )
    private val mPermissions = arrayOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMessageRv.apply {
            layoutManager = LinearLayoutManager(this@MessageActivity)
            adapter = mAdapter
        }

        requestMultiplePermissions(mPermissions) {
            allGranted = {
                readSms()
            }
            denied = {
                getSnackbar().setText("权限${it}已被拒绝").show()
            }
            noMoreAsk = {
                getSnackbar().setText("权限${it}已被拒绝，且不会再询问").show()
            }
        }

        mAdapter.setOnItemClickListener(this)
    }

    @SuppressLint("Range")
    private fun readSms() {
        val cursor: Cursor = contentResolver
            .query(Telephony.Sms.CONTENT_URI, mSmsColumns, null, null, null) ?: return
        val messages: MutableList<ItemWrapper<Message>> = ArrayList()
        with(cursor) {
            while (moveToNext()) {
                val address = getString(getColumnIndex(Telephony.Sms.ADDRESS))
                val person = getString(getColumnIndex(Telephony.Sms.PERSON))
                val date = getLong(getColumnIndex(Telephony.Sms.DATE))
                val type = getInt(getColumnIndex(Telephony.Sms.TYPE))
                val body = getString(getColumnIndex(Telephony.Sms.BODY))
                var name = getNameFromAddress(address)
                if (null == name) {
                    name = getNameFromPerson(person)
                }
                if (null == name) {
                    name = address
                }
                val message = Message("$name", address, date, type, body)
                // messages.add(ItemWrapper(message, layoutId = R.layout.item_message))
            }
        }
        cursor.close()
        mAdapter.submitList(messages)
    }

    // 通过查询电话查找表从地址中获取姓名
    private fun getNameFromAddress(address: String?): String? {
        if (address == null) {
            return null
        }
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(address)
        )
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null) ?: return null
        var name: String? = null
        if (cursor.moveToFirst()) {
            name =
                cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
        }
        cursor.close()
        return name
    }

    // 通过查询联系人表获取此人的姓名
    private fun getNameFromPerson(person: String?): String? {
        if (person == null) {
            return null
        }
        val uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, person)
        val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null) ?: return null
        var name: String? = null
        if (cursor.moveToFirst()) {
            name =
                cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        }
        cursor.close()
        return name
    }

    override fun onItemClick(view: View, pos: Int, item: Message?) {
        if(null == item) return
        val mMessageBottomSheet = MessageBottomSheet()
        mMessageBottomSheet.show(supportFragmentManager, MessageBottomSheet.TAG)
        mMessageBottomSheet.setMessage(item)
    }

}