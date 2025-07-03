/*
 * Copyright 2021-2025 VastGui
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
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.exifinterface.media.ExifInterface
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.databinding.CropLayoutBinding
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.ScreenSizeUtils
import com.ave.vastgui.tools.viewbinding.viewBinding
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.math.sqrt
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import androidx.core.graphics.createBitmap

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/cropview/crop-view/

/**
 * [CropViewLayout].
 *
 * @since 0.5.0
 */
class CropViewLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_CropViewLayout_Style,
    defStyleRes: Int = R.style.BaseCropViewLayout
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_SIZE get() = dimension(R.dimen.default_crop_frame_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_WIDTH = dimension(R.dimen.default_crop_frame_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_HEIGHT = dimension(R.dimen.default_crop_frame_height)

    /** @since 1.5.2 */
    private val binding by viewBinding(CropLayoutBinding::bind, R.id.crop_layout_root)

    /**
     * The imageview is used to show original image.
     *
     * @since 1.5.2
     */
    private val srcImageView
        get() = binding.cropLayoutImage

    /** @since 1.5.2 */
    private val cropView
        get() = binding.cropLayoutCrop

    /**
     * The matrix that is used to record state of the original image.
     *
     * @since 1.5.2
     */
    private val srcMatrix: Matrix = Matrix()

    /**
     * The value of [srcMatrix].
     *
     * @since 1.5.2
     */
    private val srcMatrixValues = FloatArray(9)

    /**
     * The matrix that is used to record last time state of the original image.
     *
     * @since 1.5.2
     */
    private val savedMatrix: Matrix = Matrix()

    /**
     * Currently gesture.
     *
     * @since 1.5.2
     */
    private var gesture = CropViewLayoutGesture.NONE

    /**
     * The coordinate when [MotionEvent.getAction] is
     * [MotionEvent.ACTION_DOWN].
     *
     * @since 1.5.2
     */
    private val startCoordinate = PointF()

    /**
     * The coordinate of the middle point of the two fingers when zooming.
     *
     * @since 1.5.2
     */
    private val midCoordinate = PointF()

    /**
     * The distance of the two fingers last time.
     *
     * @since 1.5.2
     */
    private var oldDist = 1f

    /**
     * The minimum allowable scale value.
     *
     * @since 1.5.2
     */
    private var minScale = 0f

    /**
     * The maximum allowable scale value.
     *
     * @since 1.5.2
     */
    private val maxScale = 4f

    /**
     * The currently scale value of original image.
     *
     * @since 1.5.2
     */
    val currentlyScale: Float
        get() {
            srcMatrix.getValues(srcMatrixValues)
            return srcMatrixValues[Matrix.MSCALE_X]
        }

    /** @since 1.5.2 */
    private val widthPixels
        get() = ScreenSizeUtils.getMobileScreenWidth(context)

    /** @since 1.5.2 */
    private val heightPixels
        get() = ScreenSizeUtils.getMobileScreenHeight(context)

    /**
     * @see CropView.setCropMaskColor
     * @see CropView.cropMaskColor
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var cropMaskColor: Int
        set(value) = cropView.setCropMaskColor(value)
        get() = cropView.cropMaskColor

    /**
     * @see CropView.setCropFrameType
     * @see CropView.cropMaskColor
     * @since 1.5.2
     */
    var cropFrameType: CropFrameType
        set(value) = cropView.setCropFrameType(value)
        get() = cropView.cropFrameType

    /**
     * @see CropView.setCropFrameStrokeColor
     * @see CropView.cropFrameStrokeColor
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var cropFrameStrokeColor: Int
        set(value) = cropView.setCropFrameStrokeColor(value)
        get() = cropView.cropFrameStrokeColor

    /** @since 1.5.2 */
    val cropFrameWidth: Float
        get() = cropView.cropFrameWidth

    /** @since 1.5.2 */
    val cropFrameHeight: Float
        get() = cropView.cropFrameHeight

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
                savedMatrix.set(srcMatrix)
                startCoordinate[event.x] = event.y
                gesture = CropViewLayoutGesture.DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = getSpacing(event)
                if (oldDist > 10f) {
                    savedMatrix.set(srcMatrix)
                    getMidPoint(midCoordinate, event)
                    gesture = CropViewLayoutGesture.ZOOM
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                gesture = CropViewLayoutGesture.NONE
            }

            MotionEvent.ACTION_MOVE -> {
                if (gesture == CropViewLayoutGesture.DRAG) {
                    srcMatrix.set(savedMatrix)
                    val dx = event.x - startCoordinate.x
                    val dy = event.y - startCoordinate.y
                    srcMatrix.postTranslate(dx, dy)
                    checkBorder()
                } else if (gesture == CropViewLayoutGesture.ZOOM) {
                    val newDist = getSpacing(event)
                    if (newDist > 10f) {
                        var scale = newDist / oldDist
                        if (scale < 1) {
                            if (currentlyScale > minScale) {
                                srcMatrix.set(savedMatrix)
                                srcMatrix.postScale(scale, scale, midCoordinate.x, midCoordinate.y)
                                while (currentlyScale < minScale) {
                                    scale = 1 + 0.01f
                                    srcMatrix.postScale(scale, scale, midCoordinate.x, midCoordinate.y)
                                }
                            }
                            checkBorder()
                        } else {
                            if (currentlyScale <= maxScale) {
                                srcMatrix.set(savedMatrix)
                                srcMatrix.postScale(scale, scale, midCoordinate.x, midCoordinate.y)
                            }
                        }
                    }
                }
                srcImageView.imageMatrix = srcMatrix
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
        val observer = srcImageView.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initSrcPic(file)
                srcImageView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    /**
     * Get cropped image above api28
     *
     * @since 1.5.2
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun getCroppedImageApi28(requireWidth: Int, requireHeight: Int): Bitmap? {
        var cropBitmap: Bitmap? = null
        var scaleCropBitmap: Bitmap? = null
        val rect: Rect = cropView.getCropFrameRect()
        try {
            val origin = createBitmap(srcImageView.width, srcImageView.height)
            val canvas = Canvas(origin)
            srcImageView.draw(canvas)
            cropBitmap = Bitmap.createBitmap(origin, rect.left, rect.top, rect.width(), rect.height())
            scaleCropBitmap = BmpUtils.scaleBitmap(cropBitmap, requireWidth, requireHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cropBitmap?.recycle()
        return scaleCropBitmap
    }

    /**
     * Get cropped image under api28
     *
     * @since 1.5.2
     */
    @Suppress("DEPRECATION")
    fun getCroppedImage(requireWidth: Int, requireHeight: Int): Bitmap? {
        var cropBitmap: Bitmap? = null
        var scaleCropBitmap: Bitmap? = null
        srcImageView.isDrawingCacheEnabled = true
        srcImageView.buildDrawingCache()
        val rect: Rect = cropView.getCropFrameRect()
        try {
            cropBitmap = Bitmap.createBitmap(srcImageView.drawingCache, rect.left, rect.top, rect.width(), rect.height())
            scaleCropBitmap = BmpUtils.scaleBitmap(cropBitmap, requireWidth, requireHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cropBitmap?.recycle()
        srcImageView.destroyDrawingCache()
        return scaleCropBitmap
    }

    /**
     * @see CropView.setCropFrameSize
     * @since 0.5.0
     */
    fun setCropFrameSize(width: Float, height: Float) {
        cropView.setCropFrameSize(width, height)
    }

    /**
     * @see CropView.setCropFrameSize
     * @since 1.5.2
     */
    fun setCropFrameSize(@FloatRange(from = 0.0) size: Float) {
        cropView.setCropFrameSize(size)
    }

    /**
     * Get the range of the image according to the [ImageView.getDrawable].
     *
     * @since 0.5.0
     */
    private fun getMatrixRectF(matrix: Matrix): RectF {
        val rect = RectF()
        srcImageView.drawable?.let {
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
        val rect = getMatrixRectF(srcMatrix)
        var deltaX = 0f
        var deltaY = 0f
        val frame = cropView.getCropFrameRect()
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
        srcMatrix.postTranslate(deltaX, deltaY)
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
            if (w > widthPixels) widthPixels else w,
            if (h > heightPixels) heightPixels else h
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
            scaleX = srcImageView.width.toFloat() / bitmap.width
            val rect: Rect = cropView.getCropFrameRect()
            minScale = rect.height() / bitmap.height.toFloat()
            if (scaleX < minScale) {
                scaleX = minScale
            }
        } else {
            scaleX = srcImageView.height.toFloat() / bitmap.height
            val rect: Rect = cropView.getCropFrameRect()
            minScale = rect.width() / bitmap.width.toFloat()
            if (scaleX < minScale) {
                scaleX = minScale
            }
        }
        val scaleY: Float = scaleX
        srcMatrix.postScale(scaleX, scaleY)
        val midX = srcImageView.width / 2
        val midY = srcImageView.height / 2
        val imageMidX = (bitmap.width * scaleX / 2).toInt()
        val imageMidY = (bitmap.height * scaleY / 2).toInt()
        srcMatrix.postTranslate((midX - imageMidX).toFloat(), (midY - imageMidY).toFloat())
        srcImageView.scaleType = ImageView.ScaleType.MATRIX
        srcImageView.imageMatrix = srcMatrix
        srcImageView.setImageBitmap(bitmap)
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
        context.withStyledAttributes(attrs, R.styleable.CropViewLayout, defStyleAttr, defStyleRes) {
            cropMaskColor = getColor(R.styleable.CropViewLayout_crop_mask_layer_color, color(R.color.default_crop_frame_mask_color))
            cropFrameType =
                when (getInt(R.styleable.CropViewLayout_crop_frame_type, 0)) {
                    CropFrameType.CIRCLE.ordinal -> CropFrameType.CIRCLE
                    CropFrameType.SQUARE.ordinal -> CropFrameType.SQUARE
                    CropFrameType.GRID9.ordinal -> CropFrameType.GRID9
                    CropFrameType.RECTANGLE.ordinal -> CropFrameType.RECTANGLE
                    else -> CropFrameType.CIRCLE
                }
            cropFrameStrokeColor = getColor(R.styleable.CropViewLayout_crop_frame_stroke_color, color(R.color.md_theme_primaryFixedDim))
            val size = getDimension(R.styleable.CropViewLayout_crop_frame_size, DEFAULT_CROP_FRAME_SIZE)
            setCropFrameSize(size)
            val width = getDimension(R.styleable.CropViewLayout_crop_frame_width, DEFAULT_CROP_FRAME_WIDTH)
            val height = getDimension(R.styleable.CropViewLayout_crop_frame_height, DEFAULT_CROP_FRAME_HEIGHT)
            setCropFrameSize(width, height)
        }
    }

}