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

package cn.govast.vasttools.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/24
// Description: 
// Documentation:

object QRCodeUtil {

    /**
     * Create QR code bitmap (support custom configuration and custom style).
     *
     * @param content QRCode string content.
     * @param width bitmap width(in pixel).
     * @param height bitmap height(in pixel).
     * @param character_set character set/character transcoding format
           (supported format: [CharacterSetECI]). When value is null, the zxing
           source code uses "ISO-8859-1" by default.
     * @param error_correction fault tolerance level (support level:
           [ErrorCorrectionLevel]). When value is null, the zxing source code
           uses "L" by default.
     * @param margin bitmap margin (modifiable, required: integer and >=0),
     *     when null is passed, the zxing source code uses "4" by default.
     * @param color_black Custom color value for black patch.
     * @param color_white Custom color value for the white patch.
     * @return QRCode bitmap.
     * @since 0.0.9
     */
    @JvmOverloads
    fun createQRCodeBitmap(
        content: String?,
        @IntRange(from = 0) width: Int,
        @IntRange(from = 0) height: Int,
        character_set: String? = "UTF-8",
        error_correction: String? = "H",
        margin: String? = "2",
        @ColorInt color_black: Int = Color.BLACK,
        @ColorInt color_white: Int = Color.WHITE
    ): Bitmap? {
        /**
         * 1.参数合法性判断
         */
        if (TextUtils.isEmpty(content)) { // 字符串内容判空
            return null
        }
        if (width < 0 || height < 0) { // 宽和高都需要>=0
            return null
        }
        try {
            /**
             * 2.设置二维码相关配置,生成BitMatrix(位矩阵)对象
             */
            val hints = Hashtable<EncodeHintType, String?>()
            if (!TextUtils.isEmpty(character_set)) {
                hints[EncodeHintType.CHARACTER_SET] = character_set // 字符转码格式设置
            }
            if (!TextUtils.isEmpty(error_correction)) {
                hints[EncodeHintType.ERROR_CORRECTION] = error_correction // 容错级别设置
            }
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin // 空白边距设置
            }
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            /**
             * 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值
             */
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix[x, y]) {
                        pixels[y * width + x] = color_black // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white // 白色色块像素设置
                    }
                }
            }
            /**
             * 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,之后返回Bitmap对象
             */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

}