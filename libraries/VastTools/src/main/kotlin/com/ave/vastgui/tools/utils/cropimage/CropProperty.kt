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

package com.ave.vastgui.tools.utils.cropimage

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/23
// Description: 
// Documentation:
// Reference:

interface CropProperty {

    /** Set the data uri. */
    fun setData(uri: Uri): CropProperty

    /** Set crop. */
    fun setCorp(value: Boolean): CropProperty

    /**
     * Sets the ratio of the length and width of the initial cropped preview
     * frame. By default, the width of the initial preview frame is the width
     * of the image to be cropped.
     *
     * @param aspectX The value of **aspectX**.
     * @param aspectY The value of **aspectY**.
     */
    fun setAspect(@IntRange(from = 0) aspectX: Int, @IntRange(from = 0) aspectY: Int): CropProperty

    /**
     * Set the value of [outputX] , [outputY].
     *
     * @param outputX The width of output image in pixels.
     * @param outputY The height of output image in pixels.
     */
    fun setOutput(@IntRange(from = 0) outputX: Int, @IntRange(from = 0) outputY: Int): CropProperty

    /** Set the [uri] for the output image. */
    fun setOutputUri(uri: Uri?): CropProperty

    /**
     * Set the output image format.
     *
     * @param format For example: [Bitmap.CompressFormat.JPEG.toString]
     */
    fun setOutputFormat(format: String): CropProperty

    /**
     * Set the return-data.
     *
     * @param value True if you want to save the return data in the [Bitmap],
     *     false otherwise.
     */
    fun setReturnData(value: Boolean): CropProperty

    /**
     * Set the noFaceDetection.
     *
     * @param value True if you want to remove face detection, false otherwise.
     */
    fun setNoFaceDetection(value: Boolean): CropProperty

}