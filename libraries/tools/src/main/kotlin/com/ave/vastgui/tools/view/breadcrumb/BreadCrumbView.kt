package com.ave.vastgui.tools.view.breadcrumb

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.graphics.BmpUtils.getBitmapFromDrawable
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/7
// Documentation:

data class BreadCrumb(
    val title: String
)

class BreadCrumbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

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

    private var _itemMaxWidth: Float by Delegates.notNull()

    val itemMaxWidth: Float
        get() = _itemMaxWidth

    private var _intervalIcon: Bitmap? = null

    val intervalIcon: Bitmap?
        get() = _intervalIcon

    var intervalIconWidth: Float by Delegates.notNull()
        private set

    var intervalIconHeight: Float by Delegates.notNull()
        private set

    private lateinit var recyclerView: RecyclerView
    private lateinit var breadCrumbAdapter: BreadCrumbAdapter

    private fun createAndAddRecyclerView(context: Context) {
        recyclerView = RecyclerView(context)
        val recyclerViewParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        recyclerView.layoutManager = object:LinearLayoutManager(context, HORIZONTAL, false) {
            override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                return RecyclerView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            }
        }
        breadCrumbAdapter = BreadCrumbAdapter(object : BreadCrumbItemClickListener {
            override fun onItemClick(breadCrumbItem: View, position: Int) {}
        })

        recyclerView.adapter = breadCrumbAdapter

        addView(recyclerView, recyclerViewParams)
    }

    fun addBreadCrumbItem(item: BreadCrumb) {
        breadCrumbAdapter.addBreadCrumbItem(item)
        recyclerView.smoothScrollToPosition(breadCrumbAdapter.getBreadCrumbItemsSize() - 1)
    }

    fun setListener(listener: BreadCrumbItemClickListener) {
        breadCrumbAdapter.breadCrumbItemClickListener = listener
    }

    fun setArrowDrawable(arrowDrawable: Int) = breadCrumbAdapter.setArrowDrawable(arrowDrawable)
    fun setBreadCrumbItems(items: MutableList<BreadCrumb>) {
        breadCrumbAdapter.setBreadCrumbItems(items)
        recyclerView.smoothScrollToPosition(breadCrumbAdapter.getBreadCrumbItemsSize() - 1)
    }

    fun setTextColor(textColor: Int) = breadCrumbAdapter.setTextColor(textColor)
    fun setTextSize(textSize: Int) = breadCrumbAdapter.setTextSize(textSize)
    fun getBreadCrumbItem(position: Int) = breadCrumbAdapter.getBreadCrumbItem(position)
    fun removeAllBreadCrumbItems() = breadCrumbAdapter.removeAllBreadCrumbItems()
    fun removeLastBreadCrumbItem() = breadCrumbAdapter.removeLastBreadCrumbItem()

    fun setItemWidth(width: Float) {
        _itemMaxWidth = width
        invalidate()
    }

    fun setIntervalIcon(@DrawableRes id: Int) {
        _intervalIcon = getBitmapFromDrawable(id, context)
        invalidate()
    }

    fun setIntervalIcon(bitmap: Bitmap) {
        _intervalIcon = bitmap
        invalidate()
    }

    fun setIntervalIconSize(width: Float, height: Float) {
        intervalIconWidth = width
        intervalIconHeight = height
    }

    private inner class InternalIconDecoration(private val space: Float) : RecyclerView.ItemDecoration() {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        private val iconWithScaled: Bitmap? = intervalIcon?.let {
            BmpUtils.scaleBitmap(it, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())
        }

        private val iconSrcRect = Rect(0, 0, intervalIconWidth.roundToInt(), intervalIconHeight.roundToInt())

        private val iconDstRectF = RectF()

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            if (null == iconWithScaled) return
            val manager = parent.layoutManager ?: return
            val count = manager.itemCount
            for (index in 0 until count) {
                val view = manager.getChildAt(index) ?: continue
                val right = manager.getRightDecorationWidth(view)
                if (right == 0) continue
                val cx = (view.right + right).toFloat()
                val cy = view.top + view.measuredHeight / 2f
                iconDstRectF.set(
                    cx - intervalIconWidth / 2f,
                    cy - intervalIconHeight / 2f,
                    cx + intervalIconWidth / 2f,
                    cy + intervalIconHeight / 2f
                )
                c.drawBitmap(iconWithScaled, iconSrcRect, iconDstRectF, paint)
            }
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val totalCount = parent.adapter?.itemCount ?: return
            when (position) {
                0 -> {
                    outRect.left = 0
                    outRect.right = (space / 2f).roundToInt()
                }

                totalCount - 1 -> {
                    outRect.left = (space / 2f).roundToInt()
                    outRect.right = 0
                }

                else -> {
                    outRect.left = (space / 2f).roundToInt()
                    outRect.right = (space / 2f).roundToInt()
                }
            }
        }
    }

    private inner class BreadCrumbAdapter(var breadCrumbItemClickListener: BreadCrumbItemClickListener) : RecyclerView.Adapter<BreadCrumbAdapter.ViewHolder>() {

        private var breadCrumbItemsData: MutableList<BreadCrumb> = mutableListOf()
        private var arrowDrawable: Int = R.drawable.ic_baseline_keyboard_arrow_right
        private var textColor: Int = 10
        private var textSize: Int = 10

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val root = LayoutInflater.from(parent.context).inflate(R.layout.item_bread_crumb, parent, false)
            (root as ConstraintLayout).maxWidth = _itemMaxWidth.roundToInt()
            return ViewHolder(root)
        }

        override fun getItemCount(): Int = breadCrumbItemsData.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = breadCrumbItemsData[position]
            holder.breadCrumbTitle.text = item.title
        }

        fun getBreadCrumbItem(position: Int) = breadCrumbItemsData[position]

        fun getBreadCrumbItemsSize(): Int = breadCrumbItemsData.size

        fun removeLastBreadCrumbItem() {
            breadCrumbItemsData.removeLast()
            notifyDataSetChanged()
        }

        fun removeAllBreadCrumbItems() {
            breadCrumbItemsData.removeAll { true }
            notifyDataSetChanged()
        }

        fun addBreadCrumbItem(item: BreadCrumb) {
            breadCrumbItemsData.add(item)
            notifyDataSetChanged()
        }

        fun setBreadCrumbItems(items: MutableList<BreadCrumb>) {
            breadCrumbItemsData = items
            notifyDataSetChanged()
        }

        fun setArrowDrawable(arrowDrawable: Int) {
            this.arrowDrawable = arrowDrawable
            notifyDataSetChanged()
        }

        fun setTextColor(textColor: Int) {
            this.textColor = textColor
            notifyDataSetChanged()
        }

        fun setTextSize(textSize: Int) {
            this.textSize = textSize
            notifyDataSetChanged()
        }

        inner class ViewHolder(breadCrumbItem: View) : RecyclerView.ViewHolder(breadCrumbItem) {
            var breadCrumbTitle: TextView = itemView.findViewById(R.id.bread_crumb_title)

            init {
                breadCrumbTitle.setOnClickListener { view ->
                    breadCrumbItemClickListener.onItemClick(view, adapterPosition)
                }
                breadCrumbTitle.setTextColor(textColor)
                breadCrumbTitle.textSize = textSize.toFloat()
            }
        }
    }

    interface BreadCrumbItemClickListener {
        fun onItemClick(breadCrumbItem: View, position: Int)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.BreadCrumbView, defStyleAttr, defStyleRes) {
            _itemMaxWidth = getDimension(R.styleable.BreadCrumbView_breadcrumb_item_max_width, DEFAULT_ITEM_MAX_WIDTH)
            val textColor = getColor(R.styleable.BreadCrumbView_breadcrumb_text_color, DEFAULT_TEXT_COLOR)
            val textSize = getDimension(R.styleable.BreadCrumbView_breadcrumb_text_size, DEFAULT_TEXT_SIZE)
            val intervalWidth = getDimension(R.styleable.BreadCrumbView_breadcrumb_interval_width, 0f)
            _intervalIcon = getBitmapFromDrawable(getResourceId(R.styleable.BreadCrumbView_breadcrumb_interval_icon, -1), context)
            intervalIconWidth = getDimension(R.styleable.BreadCrumbView_breadcrumb_interval_icon_width, DEFAULT_ICON_SIZE)
            intervalIconHeight = getDimension(R.styleable.BreadCrumbView_breadcrumb_interval_icon_height, DEFAULT_ICON_SIZE)
            createAndAddRecyclerView(context)
            breadCrumbAdapter.setTextColor(textColor)
            breadCrumbAdapter.setTextSize(textSize.roundToInt())
            recyclerView.addItemDecoration(InternalIconDecoration(intervalWidth))
        }

        this.setPadding(40, 0, 40, 0)
    }
}