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

package com.ave.vastgui.tools.graphics

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.Hashtable

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/24
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/graphics/qrcode/qrcode/

object QRCode {

    /**
     * QR code color.
     *
     * @property color_black It will replace the black color in the original QR
     *     code. Default value is [Color.BLACK].
     * @property color_white It will replace the white color in the original QR
     *     code. Default value is [Color.WHITE].
     */
    data class QRColor @JvmOverloads constructor(
        @ColorInt val color_black: Int = Color.BLACK,
        @ColorInt val color_white: Int = Color.WHITE
    )

    /**
     * Create QR code bitmap (support custom configuration and custom style).
     *
     * @param content QRCode string content.
     * @param width bitmap width(in pixel).
     * @param height bitmap height(in pixel).
     * @param characterSet character set/character transcoding format
     *     (supported format: CharacterSetECI). Using "UTF-8" by default.
     * @param errorCorrection fault tolerance level (support level:
     *     ErrorCorrectionLevel). Using ErrorCorrectionLevel.H by default.
     * @param margin bitmap margin (modifiable, required: integer and >=0),
     *     Using 2 by default.
     * @param qrColor [QRColor]
     * @return QRCode bitmap.
     * @see CharacterSetECI
     * @see ErrorCorrectionLevel
     * @since 0.5.1
     */
    @JvmOverloads
    @JvmStatic
    fun createQRCodeBitmap(
        content: String,
        @IntRange(from = 0) width: Int,
        @IntRange(from = 0) height: Int,
        characterSet: String = "UTF-8",
        errorCorrection: ErrorCorrectionLevel = ErrorCorrectionLevel.H,
        @IntRange(from = 0) margin: Int = 2,
        qrColor: QRColor = QRColor()
    ): Bitmap {
        try {
            val hints = Hashtable<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = characterSet
            hints[EncodeHintType.ERROR_CORRECTION] = errorCorrection
            hints[EncodeHintType.MARGIN] = margin
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = qrColor.color_black
                    } else {
                        pixels[y * width + x] = qrColor.color_white
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (exception: Exception) {
            throw exception
        }
    }

}