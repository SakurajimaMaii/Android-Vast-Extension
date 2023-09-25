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

package com.ave.vastgui.tools.view.avatar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.graphics.withSave
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import java.io.File
import java.io.FileInputStream

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25
// Description: 
// Documentation:
// Reference: https://github.com/jhbxyz/ArticleRecord/blob/master/articles/%E8%87%AA%E5%AE%9A%E4%B9%89View/2%E5%9C%86%E5%BD%A2%E5%A4%B4%E5%83%8F.md

class Avatar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_Avatar_Style,
    @StyleRes defStyleRes: Int = R.style.BaseAvatar
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val MODE_CIRCLE = 0
        const val MODE_ROUND_CORNER = 1
    }

    @IntDef(
        flag = true, value = [
            MODE_CIRCLE,
            MODE_ROUND_CORNER
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Mode

    private val mDefaultText = "A"
    private val mDefaultAvatarSize =
        resources.getDimension(R.dimen.default_avatar_size)
    private val mDefaultTextSize =
        resources.getDimension(R.dimen.default_avatar_text_size)
    private val mDefaultStrokeWidth =
        resources.getDimension(R.dimen.default_avatar_stroke_width)
    private val mDefaultCornerRadius =
        resources.getDimension(R.dimen.default_avatar_corner_radius)

    private val mAvatarPath = Path()
    private val mAvatarRectF = RectF()
    private val mAvatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }
    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    @get:Mode
    var mMode: Int = MODE_CIRCLE
        private set

    var mAvatarSize: Float = 60f.DP
        private set

    var mAvatar: Bitmap? = null
        private set

    var mBackground: Int
        set(value) {
            mBackgroundPaint.color = value
        }
        get() = mBackgroundPaint.color

    var mText by NotNUllVar<String>()
        private set

    var mTextSize: Float
        set(value) {
            mTextPaint.textSize = value
        }
        get() = mTextPaint.textSize

    var mTextColor: Int
        set(value) {
            mTextPaint.color = value
        }
        get() = mTextPaint.color

    var mStrokeWidth: Float
        set(value) {
            mStrokePaint.strokeWidth = value
        }
        get() = mStrokePaint.strokeWidth

    var mStrokeColor: Int
        set(value) {
            mStrokePaint.color = value
        }
        get() = mStrokePaint.color

    var mCornerRadius: Float = mDefaultCornerRadius

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val neededWidth = if (mMode == MODE_ROUND_CORNER) mAvatarSize + mStrokeWidth else mAvatarSize
        val neededHeight = if (mMode == MODE_ROUND_CORNER) mAvatarSize + mStrokeWidth else mAvatarSize
        val width = resolveSize(neededWidth.toInt(), widthMeasureSpec)
        val height = resolveSize(neededHeight.toInt(), heightMeasureSpec)
        val textSize = mTextPaint.textSize
        mTextPaint.textSize = textSize.coerceAtMost(width * 0.7f)
        when (mMode) {
            MODE_CIRCLE -> mAvatarPath.addCircle(
                width / 2f,
                height / 2f,
                mAvatarSize / 2f,
                Path.Direction.CW
            )

            MODE_ROUND_CORNER -> {
                mAvatarRectF.set(
                    (width - mAvatarSize) / 2f,
                    (height - mAvatarSize) / 2f,
                    (width + mAvatarSize) / 2f,
                    (height + mAvatarSize) / 2f
                )
                mAvatarPath.addRoundRect(mAvatarRectF, mCornerRadius, mCornerRadius, Path.Direction.CW)
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (null != mAvatar) {
            canvas.drawPath(mAvatarPath, mStrokePaint)
            canvas.withSave {
                clipPath(mAvatarPath)
                drawBitmap(
                    mAvatar!!,
                    (measuredWidth - mAvatarSize) / 2f,
                    (measuredHeight - mAvatarSize) / 2f,
                    mAvatarPaint
                )
            }
        } else {
            when (mMode) {
                MODE_CIRCLE -> canvas.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    mAvatarSize / 2f,
                    mBackgroundPaint
                )

                MODE_ROUND_CORNER -> canvas.drawRoundRect(
                    mAvatarRectF, mCornerRadius, mCornerRadius, mBackgroundPaint
                )
            }

            canvas.drawText(
                mDefaultText,
                measuredWidth / 2f,
                measuredHeight / 2f + getBaseLine(),
                mTextPaint
            )
        }
    }

    /**
     * Set mode.
     *
     * @since 0.5.4
     */
    fun setMode(@Mode mode: Int) {
        mMode = mode
    }

    /**
     * Set avatar by [bitmap].
     *
     * @since 0.5.4
     */
    fun setAvatar(bitmap: Bitmap) {
        mAvatar = BmpUtils.scaleBitmap(bitmap, mAvatarSize.toInt(), mAvatarSize.toInt())
    }

    /**
     * Set avatar by [resId].
     *
     * @since 0.5.4
     */
    fun setAvatar(@DrawableRes resId: Int) {
        val avatar = try {
            BmpUtils.getBitmapFromDrawable(resId, context)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return
        }
        mAvatar = BmpUtils.scaleBitmap(avatar, mAvatarSize.toInt(), mAvatarSize.toInt())
    }

    /**
     * Set avatar by [file].
     *
     * @since 0.5.4
     */
    fun setAvatar(file: File) {
        val avatar = try {
            BitmapFactory.decodeStream(FileInputStream(file))
        } catch (exception: Exception) {
            exception.printStackTrace()
            return
        }
        mAvatar = BmpUtils.scaleBitmap(avatar, mAvatarSize.toInt(), mAvatarSize.toInt())
    }

    /**
     * Get base line
     *
     * @since 0.5.4
     */
    private fun getBaseLine(): Float {
        val fontMetrics = mTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2f - fontMetrics.bottom
    }

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.Avatar,
            defStyleAttr,
            defStyleRes
        )
        mAvatarSize =
            typeArray.getDimension(R.styleable.Avatar_avatar_size, mDefaultAvatarSize)
        mAvatar = try {
            val avatar = BmpUtils.getBitmapFromDrawable(
                typeArray.getResourceIdOrThrow(R.styleable.Avatar_avatar_src),
                context
            )
            BmpUtils.scaleBitmap(avatar, mAvatarSize.toInt(), mAvatarSize.toInt())
        } catch (exception: Exception) {
            null
        }
        mMode =
            typeArray.getInteger(R.styleable.Avatar_avatar_mode, MODE_CIRCLE)
        mBackground =
            typeArray.getColor(
                R.styleable.Avatar_avatar_background,
                context.getColor(R.color.md_theme_primary)
            )
        mText =
            typeArray.getString(R.styleable.Avatar_avatar_text) ?: mDefaultText
        mTextSize =
            typeArray.getDimension(R.styleable.Avatar_avatar_text_size, mDefaultTextSize)
        mTextColor =
            typeArray.getColor(
                R.styleable.Avatar_avatar_text_color,
                context.getColor(R.color.md_theme_onPrimary)
            )
        mStrokeWidth =
            typeArray.getDimension(R.styleable.Avatar_avatar_stroke_width, mDefaultStrokeWidth)
        mStrokeColor =
            typeArray.getColor(
                R.styleable.Avatar_avatar_stroke_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        mCornerRadius =
            typeArray.getDimension(R.styleable.Avatar_avatar_corner_radius, mDefaultCornerRadius)
        typeArray.recycle()
    }

}