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

package com.ave.vastgui.tools.view.cropview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.exifinterface.media.ExifInterface
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.databinding.CropLayoutBinding
import com.ave.vastgui.tools.utils.ScreenSizeUtils
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.viewbinding.viewBinding
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.math.sqrt


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/cropview/CropView/

/**
 * Crop View Layout.
 *
 * @property mImageView The imageview is used to show original image.
 * @property mCropView The crop view.
 * @property mMatrix The matrix that is used to record state of the
 *     original image.
 * @property mSavedMatrix The matrix that is used to record last time state
 *     of the original image.
 * @property mode Currently gesture.
 * @property mStart The coordinate when [MotionEvent.getAction] is
 *     [MotionEvent.ACTION_DOWN].
 * @property mMid The coordinate of the middle point of the two fingers
 *     when zooming.
 * @property mOldDist The distance of the two fingers last time.
 * @property mMatrixValues The value of [mMatrix].
 * @property mMinScale The minimum allowable scale value.
 * @property mMaxScale The maximum allowable scale value.
 * @property mCurrentlyScale The currently scale value of original image.
 * @property mCropMaskColor See [CropView.mCropMaskColor].
 * @property mCropFrameType See [CropView.mCropFrameType].
 * @property mCropFrameStrokeColor See [CropView.mCropFrameStrokeColor].
 * @since 0.5.0
 */
class CropViewLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_CropViewLayout_Style,
    defStyleRes: Int = R.style.BaseCropViewLayout
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultCropFrameWidth
        get() = context.resources.getDimension(R.dimen.default_crop_frame_width)
    private val mDefaultCropFrameHeight
        get() = context.resources.getDimension(R.dimen.default_crop_frame_height)

    private val mBinding by viewBinding(CropLayoutBinding::bind, R.id.crop_layout_root)

    private val mImageView
        get() = mBinding.cropLayoutImage
    private val mCropView
        get() = mBinding.cropLayoutCrop
    private val mMatrix: Matrix = Matrix()
    private val mSavedMatrix: Matrix = Matrix()
    private var mode = CropViewLayoutGesture.NONE
    private val mStart = PointF()
    private val mMid = PointF()
    private var mOldDist = 1f
    private val mMatrixValues = FloatArray(9)
    private var mMinScale = 0f
    private val mMaxScale = 4f
    private val mWidthPixels
        get() = ScreenSizeUtils.getMobileScreenWidth(context)
    private val mHeightPixels
        get() = ScreenSizeUtils.getMobileScreenHeight(context)

    val mCurrentlyScale: Float
        get() {
            mMatrix.getValues(mMatrixValues)
            return mMatrixValues[Matrix.MSCALE_X]
        }

    /**
     * @see CropView.setCropMaskColor
     * @since 0.5.0
     */
    var mCropMaskColor: Int
        set(value) {
            mCropView.refreshWithInvalidate {
                setCropMaskColor(value)
            }
        }
        get() = mCropView.mCropMaskColor

    /**
     * @see CropView.setCropFrameType
     * @since 0.5.0
     */
    var mCropFrameType: CropFrameType
        set(value) {
            mCropView.refreshWithInvalidate {
                setCropFrameType(value)
            }
        }
        get() = mCropView.mCropFrameType

    /**
     * @see CropView.setCropFrameStrokeColor
     * @since 0.5.0
     */
    var mCropFrameStrokeColor: Int
        set(value) {
            mCropView.refreshWithInvalidate {
                setCropFrameStrokeColor(value)
            }
        }
        get() = mCropView.mCropFrameStrokeColor

    val mCropFrameWidth: Float
        get() = mCropView.mCropFrameWidth

    val mCropFrameHeight: Float
        get() = mCropView.mCropFrameHeight

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var needWidth = 0
        var needHeight = 0
        for (child in children) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            needWidth = needWidth.coerceAtLeast(child.measuredWidth)
            needHeight = needHeight.coerceAtLeast(child.measuredHeight)
        }
        val width = resolveSize(needWidth, widthMeasureSpec)
        val height = resolveSize(needHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mSavedMatrix.set(mMatrix)
                mStart[event.x] = event.y
                mode = CropViewLayoutGesture.DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                mOldDist = getSpacing(event)
                if (mOldDist > 10f) {
                    mSavedMatrix.set(mMatrix)
                    getMidPoint(mMid, event)
                    mode = CropViewLayoutGesture.ZOOM
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = CropViewLayoutGesture.NONE
            }

            MotionEvent.ACTION_MOVE -> {
                if (mode == CropViewLayoutGesture.DRAG) {
                    mMatrix.set(mSavedMatrix)
                    val dx = event.x - mStart.x
                    val dy = event.y - mStart.y
                    mMatrix.postTranslate(dx, dy)
                    checkBorder()
                } else if (mode == CropViewLayoutGesture.ZOOM) {
                    val newDist = getSpacing(event)
                    if (newDist > 10f) {
                        var scale = newDist / mOldDist
                        if (scale < 1) {
                            if (mCurrentlyScale > mMinScale) {
                                mMatrix.set(mSavedMatrix)
                                mMatrix.postScale(scale, scale, mMid.x, mMid.y)
                                while (mCurrentlyScale < mMinScale) {
                                    scale = 1 + 0.01f
                                    mMatrix.postScale(scale, scale, mMid.x, mMid.y)
                                }
                            }
                            checkBorder()
                        } else {
                            if (mCurrentlyScale <= mMaxScale) {
                                mMatrix.set(mSavedMatrix)
                                mMatrix.postScale(scale, scale, mMid.x, mMid.y)
                            }
                        }
                    }
                }
                mImageView.imageMatrix = mMatrix
            }
        }
        return true
    }

    /**
     * Set image src
     *
     * @see initSrcPic
     * @since 0.5.0
     */
    fun setImageSrc(file: File) {
        val observer = mImageView.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initSrcPic(file)
                mImageView.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }

    /**
     * Get cropped image above api28
     *
     * @since 0.5.0
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun getCroppedImageAboveApi28(
        requireWidth: Int,
        requireHeight: Int,
    ): Bitmap? {
        var cropBitmap: Bitmap? = null
        var scaleCropBitmap: Bitmap? = null
        val rect: Rect = mCropView.getCropFrameRect()
        try {
            val origin = Bitmap.createBitmap(
                mImageView.width, mImageView.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(origin)
            mImageView.draw(canvas)
            cropBitmap = Bitmap.createBitmap(
                origin, rect.left, rect.top, rect.width(), rect.height()
            )
            scaleCropBitmap =
                BmpUtils.scaleBitmap(cropBitmap, requireWidth, requireHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cropBitmap?.recycle()
        return scaleCropBitmap
    }

    /**
     * Get cropped image under api28
     *
     * @since 0.5.0
     */
    @Suppress("DEPRECATION")
    fun getCroppedImageUnderApi28(requireWidth: Int, requireHeight: Int): Bitmap? {
        var cropBitmap: Bitmap? = null
        var scaleCropBitmap: Bitmap? = null
        mImageView.isDrawingCacheEnabled = true
        mImageView.buildDrawingCache()
        val rect: Rect = mCropView.getCropFrameRect()
        try {
            cropBitmap = Bitmap.createBitmap(
                mImageView.drawingCache, rect.left, rect.top, rect.width(), rect.height()
            )
            scaleCropBitmap =
                BmpUtils.scaleBitmap(cropBitmap, requireWidth, requireHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cropBitmap?.recycle()
        mImageView.destroyDrawingCache()
        return scaleCropBitmap
    }

    /**
     * @see CropView.setCropFrameSize
     * @since 0.5.0
     */
    fun setCropFrameSize(width: Float, height: Float) {
        mCropView.setCropFrameSize(width, height)
    }

    /**
     * Get the range of the image according to the [ImageView.getDrawable].
     *
     * @since 0.5.0
     */
    private fun getMatrixRectF(matrix: Matrix): RectF {
        val rect = RectF()
        mImageView.drawable?.let {
            rect.set(0f, 0f, it.intrinsicWidth.toFloat(), it.intrinsicHeight.toFloat())
            matrix.mapRect(rect)
        }
        return rect
    }

    /**
     * When multi-touch, calculate the distance between the first two fingers
     * put down.
     *
     * @since 0.5.0
     */
    private fun getSpacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y))
    }

    /**
     * When multi-touch, calculate the center coordinates of the first two
     * fingers put down.
     *
     * @since 0.5.0
     */
    private fun getMidPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    /**
     * Get image orientation by [ExifInterface].
     *
     * @param path The name of the file of the image data.
     * @since 0.5.0
     */
    private fun getExifOrientation(path: String): Int {
        var degree = 0
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(path)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        if (exif != null) {
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            if (orientation != -1) {
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            }
        }
        return degree
    }

    /**
     * Check border
     *
     * @since 0.5.0
     */
    private fun checkBorder() {
        val rect = getMatrixRectF(mMatrix)
        var deltaX = 0f
        var deltaY = 0f
        val frame = mCropView.getCropFrameRect()
        if (rect.width() + 0.01 >= frame.width()) {
            // Image left side > Crop frame left side.
            if (rect.left > frame.left) {
                deltaX = -(rect.left - frame.left)
            }
            // Image right side < Crop frame right side.
            if (rect.right < frame.right) {
                deltaX = frame.right - rect.right
            }
        }
        if (rect.height() + 0.01 >= frame.height()) {
            // Image top side > Crop frame top side.
            if (rect.top > frame.top) {
                deltaY = -(rect.top - frame.top)
            }
            // Image bottom side < Crop frame bottom side.
            if (rect.bottom < frame.bottom) {
                deltaY = frame.bottom - rect.bottom
            }
        }
        mMatrix.postTranslate(deltaX, deltaY)
    }

    /**
     * Initialize the image file that needs to be cropped.
     *
     * @since 0.5.0
     */
    private fun initSrcPic(file: File) {
        val path: String = file.path
        if (TextUtils.isEmpty(path)) {
            return
        }
        val (w, h) = BmpUtils.getBitmapWidthHeight { options ->
            BitmapFactory.decodeFile(path, options)
        }
        var bitmap: Bitmap = getBitmapWithRequireSize(
            path,
            if (w > mWidthPixels) mWidthPixels else w,
            if (h > mHeightPixels) mHeightPixels else h
        ) ?: return

        val rotation = getExifOrientation(path)
        val m = Matrix()
        m.setRotate(rotation.toFloat())
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)

        // First, get the scale value of the longer side.
        // Second, get the scale value of the crop image.
        // Finally, Ensure that the image scale value is
        // not smaller than the cropping frame scale value.
        var scaleX: Float
        if (bitmap.width >= bitmap.height) {
            scaleX = mImageView.width.toFloat() / bitmap.width
            val rect: Rect = mCropView.getCropFrameRect()
            mMinScale = rect.height() / bitmap.height.toFloat()
            if (scaleX < mMinScale) {
                scaleX = mMinScale
            }
        } else {
            scaleX = mImageView.height.toFloat() / bitmap.height
            val rect: Rect = mCropView.getCropFrameRect()
            mMinScale = rect.width() / bitmap.width.toFloat()
            if (scaleX < mMinScale) {
                scaleX = mMinScale
            }
        }
        val scaleY: Float = scaleX
        mMatrix.postScale(scaleX, scaleY)
        val midX = mImageView.width / 2
        val midY = mImageView.height / 2
        val imageMidX = (bitmap.width * scaleX / 2).toInt()
        val imageMidY = (bitmap.height * scaleY / 2).toInt()
        mMatrix.postTranslate((midX - imageMidX).toFloat(), (midY - imageMidY).toFloat())
        mImageView.scaleType = ImageView.ScaleType.MATRIX
        mImageView.imageMatrix = mMatrix
        mImageView.setImageBitmap(bitmap)
    }

    /**
     * Get bitmap with require size.
     *
     * @since 0.5.0
     */
    private fun getBitmapWithRequireSize(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // bitmap is null
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        val inSampleSize: Int = calculateRequireSize(options, reqWidth, reqHeight)
        options.inSampleSize = inSampleSize
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    /**
     * Calculate require size
     *
     * @since 0.5.0
     */
    private fun calculateRequireSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            val ratio = if (heightRatio < widthRatio) heightRatio else widthRatio
            inSampleSize =
                if (ratio < 3) ratio else if (ratio < 6.5) 4 else if (ratio < 8) 8 else ratio
        }
        return inSampleSize
    }

    init {
        inflate(context, R.layout.crop_layout, this)
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CropViewLayout,
            defStyleAttr,
            defStyleRes
        )
        mCropMaskColor = typeArray.getColor(
            R.styleable.CropViewLayout_crop_mask_layer_color,
            context.getColor(R.color.default_crop_frame_mask_color)
        )
        mCropFrameType =
            when (typeArray.getInt(R.styleable.CropViewLayout_crop_frame_type, 0)) {
                CropFrameType.CIRCLE.ordinal -> CropFrameType.CIRCLE
                CropFrameType.SQUARE.ordinal -> CropFrameType.SQUARE
                CropFrameType.GRID9.ordinal -> CropFrameType.GRID9
                CropFrameType.RECTANGLE.ordinal -> CropFrameType.RECTANGLE
                else -> CropFrameType.CIRCLE
            }
        mCropFrameStrokeColor = typeArray.getColor(
            R.styleable.CropViewLayout_crop_frame_stroke_color,
            context.getColor(R.color.md_theme_primaryFixedDim)
        )
        val mCropFrameWidth = typeArray.getDimension(
            R.styleable.CropView_crop_frame_width, mDefaultCropFrameWidth
        )
        val mCropFrameHeight = typeArray.getDimension(
            R.styleable.CropView_crop_frame_height, mDefaultCropFrameHeight
        )
        setCropFrameSize(mCropFrameWidth, mCropFrameHeight)
        typeArray.recycle()
        if(isInEditMode){
            mImageView.setImageResource(R.drawable.bg_demo_crop_view_image)
        }
    }

}