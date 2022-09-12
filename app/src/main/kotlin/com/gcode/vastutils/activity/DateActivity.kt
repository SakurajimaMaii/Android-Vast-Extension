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

package com.gcode.vastutils.activity

import android.os.Bundle
import android.util.Log
import com.gcode.vasttools.activity.VastVbActivity
import com.gcode.vasttools.utils.DateUtils
import com.gcode.vasttools.utils.EncryptionUtils
import com.gcode.vasttools.utils.LogUtils
import com.gcode.vasttools.utils.StrUtils
import com.gcode.vastutils.databinding.ActivityDateBinding
import java.util.*


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/8 1:48
// Description: 
// Documentation:

class DateActivity: VastVbActivity<ActivityDateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i(defaultTag,DateUtils.getCurrentTime(DateUtils.FORMAT_HH_MM))
        LogUtils.i(defaultTag,DateUtils.getDayBeforeOrAfterCurrentTime(DateUtils.FORMAT_MM_DD_HH_MM_SS,-2))
        LogUtils.i(defaultTag,DateUtils.currentTimeZone)
        LogUtils.i(defaultTag,DateUtils.minDateToString())
        LogUtils.i(defaultTag,DateUtils.minDateToString(DateUtils.FORMAT_HH_MM))
        val date: Date? = DateUtils.datetimeFromString("14:18", DateUtils.FORMAT_HH_MM)
        LogUtils.i(defaultTag,DateUtils.datetimeToString(date!!, DateUtils.FORMAT_HH_MM_SS))
        LogUtils.i(defaultTag,DateUtils.dateTimeToGMT())
        LogUtils.i(defaultTag,DateUtils.dateTimeToGMT(DateUtils.GMT_PLUS_SIX,DateUtils.FORMAT_YYYY2MM2DD_HH_MM))
        LogUtils.i(defaultTag,DateUtils.dateTimeFromGMT("07:01",DateUtils.FORMAT_HH_MM))
        LogUtils.i(defaultTag,DateUtils.weekStartTime())
        LogUtils.i(defaultTag,DateUtils.weekEndTime())
        LogUtils.i(defaultTag,DateUtils.getWeekStartTime())
        LogUtils.i(defaultTag,DateUtils.getWeekEndTime())
    }

}