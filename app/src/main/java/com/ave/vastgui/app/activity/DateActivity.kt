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
import com.ave.vastgui.app.databinding.ActivityDateBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.DateUtils
import com.ave.vastgui.tools.utils.LogUtils
import java.util.Date


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/8 1:48
// Description: 
// Documentation:

class DateActivity: VastVbActivity<ActivityDateBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.i(getDefaultTag(), DateUtils.getCurrentTime(DateUtils.FORMAT_HH_MM))
        LogUtils.i(getDefaultTag(),DateUtils.getDayBeforeOrAfterCurrentTime(DateUtils.FORMAT_MM_DD_HH_MM_SS,-2))
        LogUtils.i(getDefaultTag(),DateUtils.currentTimeZone)
        LogUtils.i(getDefaultTag(),DateUtils.minDateToString())
        LogUtils.i(getDefaultTag(),DateUtils.minDateToString(DateUtils.FORMAT_HH_MM))
        val date: Date? = DateUtils.datetimeFromString("14:18", DateUtils.FORMAT_HH_MM)
        LogUtils.i(getDefaultTag(),DateUtils.datetimeToString(date!!, DateUtils.FORMAT_HH_MM_SS))
        LogUtils.i(getDefaultTag(),DateUtils.dateTimeToGMT())
        LogUtils.i(getDefaultTag(),DateUtils.dateTimeToGMT(DateUtils.GMT_PLUS_SIX,DateUtils.FORMAT_YYYY2MM2DD_HH_MM))
        LogUtils.i(getDefaultTag(),DateUtils.dateTimeFromGMT("07:01",DateUtils.FORMAT_HH_MM))
        LogUtils.i(getDefaultTag(),DateUtils.weekStartTime())
        LogUtils.i(getDefaultTag(),DateUtils.weekEndTime())
        LogUtils.i(getDefaultTag(),DateUtils.getWeekStartTime())
        LogUtils.i(getDefaultTag(),DateUtils.getWeekEndTime())
    }

}