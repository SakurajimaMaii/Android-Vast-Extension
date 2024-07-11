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

package com.ave.vastgui.app.adapter.entity

import android.provider.Telephony
import com.ave.vastgui.adapter.base.ItemDiffUtil

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4

/**
 * 从手机获取到的短信。
 *
 * @param address [Telephony.Sms.ADDRESS]
 * @param date [Telephony.Sms.DATE]
 * @param type [Telephony.Sms.TYPE]
 * @param address [Telephony.Sms.BODY]
 */
open class Message(
    val name: String,
    val address: String,
    val date: Long,
    val type: Int,
    val body: String
)

object MessageDiffUtil : ItemDiffUtil<Message>() {
    override fun newAreContentsTheSame(oldItem: Message?, newItem: Message?): Boolean {
        return oldItem?.body == newItem?.body
    }

    override fun newAreItemsTheSame(oldItem: Message?, newItem: Message?): Boolean {
        return oldItem?.address == newItem?.address && oldItem?.date == newItem?.date
    }
}