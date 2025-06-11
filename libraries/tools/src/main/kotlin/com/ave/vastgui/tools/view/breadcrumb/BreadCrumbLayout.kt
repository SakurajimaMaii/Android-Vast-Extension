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
import androidx.core.view.isEmpty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/7
// Documentation:

/**
 * [BreadCrumbLayout]
 *
 * @since 1.5.2
 */
class BreadCrumbLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BreadCrumbLayout_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBreadCrumbLayout
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

    /** @since 1.5.2 */
    private val container: LinearLayout
        get() = binding.breadcrumbContainer

    /** @since 1.5.2 */
    private val breadcrumbListeners: SparseArray<BreadCrumbClickListener> = SparseArray()

    /** @since 1.5.2 */
    private val breadcrumbIds = Stack<Int>()

    /** @since 1.5.2 */
    private var _itemMaxWidth: Float by Delegates.notNull()

    /** @since 1.5.2 */
    val itemMaxWidth: Float
        get() = _itemMaxWidth

    /** @since 1.5.2 */
    private var _intervalIcon: Drawable? = null

    /** @since 1.5.2 */
    val intervalIcon: Drawable?
        get() = _intervalIcon

    /**
     * The color-int of breadcrumb-text.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    private var _textColor: Int = DEFAULT_TEXT_COLOR

    /**
     * The color-int of breadcrumb-text.
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

    /**
     * The width of interval-icon(in pixels).
     *
     * @since 1.5.2
     */
    var intervalIconWidth: Float by Delegates.notNull()
        private set

    /**
     * The height of interval-icon(in pixels).
     *
     * @since 1.5.2
     */
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

    /**
     * Add [breadCrumb] to the path and return its corresponding unique index.
     *
     * @since 1.5.2
     */
    fun addItem(breadCrumb: BreadCrumb): Int {
        val breadcrumb = generateBreadCrumbItem(breadCrumb, _intervalIcon)
        val lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        container.addView(breadcrumb, lp)
        return breadcrumb.id
    }

    /**
     * Add [breadCrumbs] to the path and return the unique index corresponding
     * to each node in its order.
     *
     * @since 1.5.2
     */
    fun addItems(breadCrumbs: Array<BreadCrumb>): Array<Int> {
        return breadCrumbs.map(::addItem).toTypedArray()
    }

    /**
     * Removes the last node on the path.
     *
     * @since 1.5.2
     */
    fun removeLastItem() {
        if (breadcrumbIds.isEmpty()) return
        val removedId = breadcrumbIds.pop()
        val view = container.findViewById<MaterialTextView>(removedId) ?: return
        container.removeView(view)
    }

    /**
     * If [id] specifies the last node on the path, it is removed from the
     * path.
     *
     * @param id Get by [addItem] or [addItems].
     * @since 1.5.2
     */
    fun removeLastItem(id: Int) {
        if (breadcrumbIds.peek() != id) return
        val removedId = breadcrumbIds.pop()
        val view = container.findViewById<MaterialTextView>(removedId) ?: return
        container.removeView(view)
    }

    /**
     * Remove the nodes on the path after [id]. If [includeSelf] is 'true', the
     * node corresponding to [id] itself will also be removed.
     *
     * @param id Get by [addItem] or [addItems].
     * @since 1.5.2
     */
    @JvmOverloads
    fun removeAfterItem(id: Int, includeSelf: Boolean = false) {
        if (!breadcrumbIds.contains(id)) return
        repeat(breadcrumbIds.search(id) - (if (includeSelf) 0 else 1)) {
            if (!isEmpty()) {
                val removedId = breadcrumbIds.pop()
                val view = container.findViewById<MaterialTextView>(removedId) ?: return
                container.removeView(view)
            }
        }
    }

    /**
     * Remove all nodes in the path.
     *
     * @since 1.5.2
     */
    fun removeAllItem() {
        while (!breadcrumbIds.isEmpty()) {
            val removedId = breadcrumbIds.pop()
            val view = container.findViewById<MaterialTextView>(removedId) ?: return
            container.removeView(view)
        }
    }

    /**
     * Set the max width of breadcrumb(in pixels).
     *
     * @since 1.5.2
     */
    fun setItemWidth(width: Float) {
        if (_itemMaxWidth == width) return
        _itemMaxWidth = width.coerceAtLeast(0f)
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.maxWidth = _itemMaxWidth.roundToInt()
        }
    }

    /**
     * Set the interval-icon.
     *
     * @since 1.5.2
     */
    fun setIntervalIcon(@DrawableRes id: Int) {
        _intervalIcon = drawable(id)
        _intervalIcon?.setBounds(0, 0, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setCompoundDrawablesRelative(it.compoundDrawablesRelative[0], null, _intervalIcon, null)
        }
    }

    /**
     * Set the interval-icon.
     *
     * @since 1.5.2
     */
    fun setIntervalIcon(icon: Drawable) {
        _intervalIcon = icon
        _intervalIcon?.setBounds(0, 0, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setCompoundDrawablesRelative(it.compoundDrawablesRelative[0], null, _intervalIcon, null)
        }
    }

    /**
     * Set the width and height of [intervalIcon] (in pixels).
     *
     * @since 1.5.2
     */
    fun setIntervalIconSize(width: Float, height: Float) {
        intervalIconWidth = width
        intervalIconHeight = height
        _intervalIcon?.setBounds(0, 0, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())
        container.children.forEach pointer@{
            if (it !is MaterialTextView) return@pointer
            it.setCompoundDrawablesRelative(it.compoundDrawablesRelative[0], null, _intervalIcon, null)
        }
    }

    /** @since 1.5.2 */
    fun addBreadCrumbClickListener(listener: BreadCrumbClickListener) {
        breadcrumbListeners.append(breadcrumbListeners.size, listener)
    }

    /** @since 1.5.2 */
    fun removeAllBreadCrumbClickListener() {
        breadcrumbListeners.clear()
    }

    /** @since 1.5.2 */
    private fun generateBreadCrumbItem(breadCrumb: BreadCrumb, internalIcon: Drawable? = null): MaterialTextView {
        return MaterialTextView(context).apply {
            id = generateViewId().also { breadcrumbIds.push(it) }
            text = breadCrumb.path
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
            setCompoundDrawablesRelative(breadCrumb.icon, null, internalIcon, null)
        }
    }

    /** @since 1.5.2 */
    @FunctionalInterface
    fun interface BreadCrumbClickListener {
        /** @since 1.5.2 */
        fun onItemClick(item: MaterialTextView, id: Int)
    }

    init {
        binding = LayoutBreadcrumbBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
        context.withStyledAttributes(attrs, R.styleable.BreadCrumbLayout, defStyleAttr, defStyleRes) {
            _itemMaxWidth = getDimension(R.styleable.BreadCrumbLayout_breadcrumb_item_max_width, DEFAULT_ITEM_MAX_WIDTH)
            _textColor = getColor(R.styleable.BreadCrumbLayout_breadcrumb_text_color, DEFAULT_TEXT_COLOR)
            _textSize = getDimensionPixelSize(R.styleable.BreadCrumbLayout_breadcrumb_text_size, DEFAULT_TEXT_SIZE.roundToInt()).toFloat()
            _intervalIcon = drawable(getResourceId(R.styleable.BreadCrumbLayout_breadcrumb_interval_icon, R.drawable.ic_breadcrumb_default_interval_icon))
            intervalIconWidth = getDimension(R.styleable.BreadCrumbLayout_breadcrumb_interval_icon_width, DEFAULT_ICON_SIZE)
            intervalIconHeight = getDimension(R.styleable.BreadCrumbLayout_breadcrumb_interval_icon_height, DEFAULT_ICON_SIZE)
            _intervalIcon?.setBounds(0, 0, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())
        }
    }
}