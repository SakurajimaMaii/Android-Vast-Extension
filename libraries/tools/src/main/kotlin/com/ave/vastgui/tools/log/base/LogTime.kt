/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.log.base

import com.ave.vastgui.tools.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/18 23:17
// Documentation: https://ave.entropy2020.cn/documents/tools/log

/** @since 1.3.1 */
internal val timeSdf = SimpleDateFormat(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)

/** @since 1.3.1 */
internal val fileNameTimeSdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH)