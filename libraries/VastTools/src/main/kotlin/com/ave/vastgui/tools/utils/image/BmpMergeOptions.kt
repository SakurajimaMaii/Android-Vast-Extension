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

package com.ave.vastgui.tools.utils.image

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/images/BmpUtils/

/**
 * Merge position when using [BmpUtils.mergeBitmap].
 *
 * @since 0.5.2
 */
enum class MergePosition {
    LT, LB, RT, RB, CENTER
}

/**
 * Merge scale when using [BmpUtils.mergeBitmapLR]
 *
 * @property SMALL_ENLARGE Meaning that the smaller image is stretched
 *     proportionally
 * @property BIG_REDUCE Meaning that the larger image is compressed
 *     proportionally.
 * @since 0.5.2
 */
enum class MergeScale {
    SMALL_ENLARGE, BIG_REDUCE
}