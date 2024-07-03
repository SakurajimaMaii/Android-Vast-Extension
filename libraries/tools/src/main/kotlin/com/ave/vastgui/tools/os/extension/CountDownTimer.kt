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

@file:JvmName("CountDownTimerExt")

package com.ave.vastgui.tools.os.extension

import android.os.CountDownTimer
import android.util.Log

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/4/15
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/os/extension/countdowntimer/

/**
 * @since 1.5.0
 */
const val TAG = "CountDownTimerExt"

/**
 * Gets [CountDownTimer] , or null if an exception is encountered.
 *
 * @param millisInFuture The number of millis in the future from the call
 *     to [CountDownTimer.start] until the countdown is done and
 *     [CountDownTimer.onFinish] is called.
 * @param countDownInterval The interval along the way to receive
 *     [CountDownTimer.onTick] callbacks.
 * @param onTick Callback fired on regular interval.
 * @param onFinish Callback fired when the time is up.
 * @since 1.5.0
 */
inline fun getCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long,
    crossinline onTick: (Long) -> Unit = {},
    crossinline onFinish: () -> Unit = {}
): CountDownTimer? = runCatching {
    object : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            onFinish()
        }
    }
}.onFailure {
    Log.e(TAG, it.stackTraceToString())
}.getOrNull()