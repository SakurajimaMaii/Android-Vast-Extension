package com.ave.vastgui.tools.view.breadcrumb

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.util.forEach
import androidx.core.view.children
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.databinding.LayoutBreadcrumbBinding
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import com.ave.vastgui.tools.utils.drawable
import com.google.android.material.textview.MaterialTextView
import java.util.Stack
import kotlin.math.roundToInt
import kotlin.properties.Delegates
import androidx.core.util.size

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/7
// Documentation:

data class BreadCrumb(
    val title: String
)

class BreadCrumbLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_ITEM_MAX_WIDTH
        get() = dimension(R.dimen.default_breadcrumb_item_max_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_TEXT_COLOR
        get() = color(R.color.md_theme_onSurface)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_TEXT_SIZE
        get() = dimension(R.dimen.default_breadcrumb_text_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_ICON_SIZE
        get() = dimension(R.dimen.default_breadcrumb_icon_size)

    /** @since 1.5.2 */
    private var binding by Delegates.notNull<LayoutBreadcrumbBinding>()

    private val container: LinearLayout
        get() = binding.breadcrumbContainer

    private val breadcrumbListeners: SparseArray<BreadCrumbItemClickListener> = SparseArray()

    private val breadcrumbIds = Stack<Int>()

    private var _itemMaxWidth: Float by Delegates.notNull()

    val itemMaxWidth: Float
        get() = _itemMaxWidth

    private var _intervalIcon: Drawable? = null

    val intervalIcon: Drawable?
        get() = _intervalIcon

    /**
     * The color-int of text.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    private var _textColor: Int = DEFAULT_TEXT_COLOR

    /**
     * The color-int of text.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    val textColor: Int
        get() = _textColor

    /**
     * The size of text(in pixels).
     *
     * @since 1.5.2
     */
    private var _textSize: Float = DEFAULT_TEXT_SIZE

    /**
     * The size of text(in pixels).
     *
     * @since 1.5.2
     */
    val textSize: Float
        get() = _textSize

    var intervalIconWidth: Float by Delegates.notNull()
        private set

    var intervalIconHeight: Float by Delegates.notNull()
        private set

    /** @since 1.5.2 */
    fun setTextColor(@ColorInt colorInt: Int) {
        if (_textColor == colorInt) return
        check(ColorUtils.isColorInt(colorInt)) {
            "The color-int(current=${colorInt.toUInt().toString(16)}) is invalid value."
        }
        _textColor = colorInt
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setTextColor(colorInt)
        }
    }

    /**
     * Set the size of text(in pixels).
     *
     * @since 1.5.2
     */
    fun setTextSize(textSize: Float) {
        if (_textSize == textSize) return
        _textSize = textSize.coerceAtLeast(0f)
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize)
        }
    }

    fun addItem(item: BreadCrumb): Int {
        val breadcrumb = generateBreadCrumbItem(item.title, _intervalIcon)
        val lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        container.addView(breadcrumb, lp)
        return breadcrumb.id
    }

    fun removeItem(id: Int) {
        if (breadcrumbIds.peek() != id) return
        val removedId = breadcrumbIds.pop()
        val view = container.findViewById<MaterialTextView>(removedId) ?: return
        container.removeView(view)
    }

    fun setItemWidth(width: Float) {
        _itemMaxWidth = width
        invalidate()
    }

    fun setIntervalIcon(@DrawableRes id: Int) {
        _intervalIcon = drawable(id)
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setCompoundDrawablesWithIntrinsicBounds(null, null, _intervalIcon, null)
        }
    }

    fun setIntervalIcon(icon: Drawable) {
        _intervalIcon = icon
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setCompoundDrawablesWithIntrinsicBounds(null, null, _intervalIcon, null)
        }
    }

    fun setIntervalIconSize(width: Float, height: Float) {
        intervalIconWidth = width
        intervalIconHeight = height
    }

    fun addOnBreadCrumbClickListener(listener: BreadCrumbItemClickListener) {
        breadcrumbListeners.append(breadcrumbListeners.size, listener)
    }

    private fun generateBreadCrumbItem(name: String, icon: Drawable? = null): MaterialTextView {
        return MaterialTextView(context).apply {
            id = generateViewId().also { breadcrumbIds.push(it) }
            text = name
            setTextColor(_textColor)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, _textSize)
            maxWidth = _itemMaxWidth.roundToInt()
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
            setOnClickListener { breadcrumb ->
                breadcrumbListeners.forEach { _, listener ->
                    listener.onItemClick(breadcrumb as MaterialTextView, this.id)
                }
            }
            if (null != icon) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
            }
        }
    }

    @FunctionalInterface
    fun interface BreadCrumbItemClickListener {
        fun onItemClick(item: MaterialTextView, id: Int)
    }

    init {
        binding = LayoutBreadcrumbBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        context.withStyledAttributes(attrs, R.styleable.BreadCrumbView, defStyleAttr, defStyleRes) {
            _itemMaxWidth = getDimension(R.styleable.BreadCrumbView_breadcrumb_item_max_width, DEFAULT_ITEM_MAX_WIDTH)
            _textColor = getColor(R.styleable.BreadCrumbView_breadcrumb_text_color, DEFAULT_TEXT_COLOR)
            _textSize = getDimensionPixelSize(R.styleable.BreadCrumbView_breadcrumb_text_size, DEFAULT_TEXT_SIZE.roundToInt()).toFloat()
            _intervalIcon = drawable(getResourceId(R.styleable.BreadCrumbView_breadcrumb_interval_icon, -1))
            intervalIconWidth = getDimension(R.styleable.BreadCrumbView_breadcrumb_interval_icon_width, DEFAULT_ICON_SIZE)
            intervalIconHeight = getDimension(R.styleable.BreadCrumbView_breadcrumb_interval_icon_height, DEFAULT_ICON_SIZE)
        }
    }
}