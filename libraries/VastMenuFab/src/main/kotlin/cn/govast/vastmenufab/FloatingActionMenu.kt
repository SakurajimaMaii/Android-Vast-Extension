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

package cn.govast.vastmenufab

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.*
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import cn.govast.vastmenufab.Util.dp2px
import kotlin.math.abs
import kotlin.math.roundToInt

class FloatingActionMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val tag = this::class.java.simpleName

    /**
     * The main [FloatingActionButton] of the menu.
     *
     * @see initMenuFab
     * @since 0.0.1
     */
    private lateinit var menuMainFab: FloatingActionButton

    /**
     * The [menuMainFab] size.Accepted
     * values:[FloatingActionButton.SIZE_NORMAL],
     * [FloatingActionButton.SIZE_MINI].
     *
     * @since 0.0.1
     */
    var menuMainFabSize
        get() = menuMainFab.fabSize
        set(size) {
            menuMainFab.setFabSize(size)
        }

    /**
     * The label text of [menuMainFab].
     *
     * @since 0.0.1
     */
    var menuMainFabLabelText
        get() = menuMainFab.labelText
        set(text) {
            menuMainFab.labelText = text
        }

    /**
     * The icon id of [menuMainFab].
     *
     * @since 0.0.1
     */
    private lateinit var menuMainFabIcon: AppCompatImageView

    /**
     * The icon [AnimatorSet] of [menuMainFab]. Default value is null.
     *
     * @since 0.0.1
     */
    private var menuMainFabIconAnimatorSet: AnimatorSet? = null

    private var menuMainFabIconShowAnimation: Animation? = null

    private var menuMainFabIconHideAnimation: Animation? = null

    /**
     * [menuMainFabIcon] open animator set if
     * [menuMainFabIconAnimatorSet] is null.
     *
     * @see initMenuFabIconAnimation
     * @since 0.0.1
     */
    private val menuMainFabIconDefaultOpenAnimatorSet = AnimatorSet()

    /**
     * [menuMainFabIcon] close animator set if
     * [menuMainFabIconAnimatorSet] is null.
     *
     * @see initMenuFabIconAnimation
     * @since 0.0.1
     */
    private val menuMainFabIconDefaultCloseAnimatorSet = AnimatorSet()

    /**
     * [menuMainFab] open animator interpolator.
     *
     * @see initMenuFabIconAnimation
     * @since 0.0.1
     */
    private var menuMainFabOpenInterpolator: Interpolator? = null

    /**
     * [menuMainFab] close animator interpolator.
     *
     * @see initMenuFabIconAnimation
     * @since 0.0.1
     */
    private var menuMainFabCloseInterpolator: Interpolator? = null

    /**
     * Using label of fab in menu.
     *
     * @since 0.0.1
     */
    private var menuFabLabelEnabled = false

    /**
     * True if show the customize shadow of fab in menu, false otherwise.
     *
     * @since 0.0.1
     */
    var menuFabShowShadow: Boolean = true

    /**
     * The shadow radius of the of fab in menu.
     *
     * @since 0.0.1
     */
    var menuFabShadowRadius: Float = 0f

    /**
     * The shadow x offset of the of fab in menu.
     *
     * @since 0.0.1
     */
    var menuFabShadowXOffset: Float = 0f

    /**
     * The shadow y offset of the of fab in menu.
     *
     * @since 0.0.1
     */
    var menuFabShadowYOffset: Float = 0f

    /**
     * The color of fab in normal state.
     *
     * @see setMenuFabNormalColorResId
     * @since 0.0.1
     */
    var menuFabNormalColor: Int =
        resources.getColor(R.color.design_default_color_primary, context.theme)

    /**
     * The color of fab in pressed state.
     *
     * @see setMenuFabPressedColorResId
     * @since 0.0.1
     */
    var menuFabPressedColor: Int =
        resources.getColor(R.color.design_default_color_primary_variant, context.theme)

    /**
     * The ripple color of fab.
     *
     * @see setMenuFabRippleColorResId
     * @since 0.0.1
     */
    var menuFabRippleColor: Int =
        resources.getColor(R.color.design_default_color_on_primary, context.theme)

    /**
     * The shadow color of fab in menu.
     *
     * @since 0.0.1
     */
    var menuFabShadowColor: Int = resources.getColor(android.R.color.darker_gray, context.theme)

    /**
     * Count of fab in menu. It is initialized in [onFinishInflate].
     *
     * @since 0.0.1
     */
    var menuFabCount = 0
        private set

    /**
     * Max width of fab in menu.
     *
     * @since 0.0.1
     */
    private var menuFabMaxWidth = 0

    /**
     * Spacing of the fab in menu.
     *
     * @since 0.0.1
     */
    var menuFabSpacing = dp2px(0f).roundToInt()
        private set

    /**
     * Menu background color, default value is [Color.TRANSPARENT]
     *
     * @see initBackgroundDimAnimation
     * @since 0.0.1
     */
    var menuBackgroundColor = Color.TRANSPARENT

    /**
     * True if [menuBackgroundColor] is not [Color.TRANSPARENT].
     *
     * @since 0.0.1
     */
    private val menuBackgroundEnabled: Boolean
        get() = menuBackgroundColor != Color.TRANSPARENT

    /**
     * Value animator of show [menuBackgroundColor].
     *
     * @see initBackgroundDimAnimation
     * @since 0.0.1
     */
    private lateinit var menuBackgroundShowAnimator: ValueAnimator

    /**
     * Value animator of hide [menuBackgroundColor].
     *
     * @see initBackgroundDimAnimation
     * @since 0.0.1
     */
    private lateinit var menuBackgroundHideAnimator: ValueAnimator

    /**
     * True if menu is hidden, false otherwise.
     *
     * @since 0.0.1
     */
    val menuHidden: Boolean
        get() = visibility == INVISIBLE

    /**
     * The [LabelView] layout id for all fab in the menu.
     *
     * @see initMenuFabLabel
     * @see setMenuFabLabel
     * @since 0.0.1
     */
    private var menuLabelsViewLayoutId = R.layout.simple_menu_fab_label

    var menuLabelsPaddingTop = dp2px(4f).roundToInt()

    var menuLabelsPaddingBottom = dp2px(4f).roundToInt()

    var menuLabelsPaddingRight = dp2px(8f).roundToInt()

    var menuLabelsPaddingLeft = dp2px(8f).roundToInt()

    /**
     * Margins of labels inside menu.
     *
     * @since 0.0.1
     */
    var menuLabelsMargin = dp2px(0f).roundToInt()

    /**
     * Menu label show animation resource id.
     *
     * @see initMenuFabLabelsSetting
     * @since 0.0.1
     */
    private var menuLabelsShowAnimation = 0

    /**
     * Menu label hide animation resource id.
     *
     * @see initMenuFabLabelsSetting
     * @since 0.0.1
     */
    private var menuLabelsHideAnimation = 0

    /**
     * Position of label to show.
     *
     * @since 0.0.1
     */
    @get:LabelsPosition
    @setparam:LabelsPosition
    private var menuLabelsPosition = 0

    /**
     * True when menu is open, false otherwise.
     *
     * @since 0.0.1
     */
    var menuIsOpened = false
        private set

    private val mLabelsVerticalOffset = dp2px(0f)

    private var mIsMenuOpening = false
    private val mUiHandler = Handler(Looper.getMainLooper())
    private var mLabelsTextColor: ColorStateList? = null
    private var mLabelsTextSize = 0f
    private var mLabelsCornerRadius = dp2px(3f).roundToInt()
    private var mLabelsShowShadow = false
    private var mLabelsColorNormal = 0
    private var mLabelsColorPressed = 0
    private var mLabelsColorRipple = 0
    var animationDelayPerItem = 0
    private var mIsAnimated = true
    private var mLabelsSingleLine = false
    private var mLabelsEllipsize = 0
    private var mLabelsMaxLines = 0
    private var mLabelsStyle = 0
    private var mCustomTypefaceFromFont: Typeface? = null
    var isIconAnimated = true
    private var mMenuButtonShowAnimation: Animation? = null
    private var mMenuButtonHideAnimation: Animation? = null
    private var mIsMenuButtonAnimationRunning = false
    private var mIsSetClosedOnTouchOutside = false
    private var mOpenDirection = 0
    private var mLabelsContext: Context? = null

    /**
     * Sets whether open and close actions should be animated
     *
     * @param animated if **false** - menu items will appear/disappear
     *     instantly without any animation
     */
    var isAnimated: Boolean
        get() = mIsAnimated
        set(animated) {
            mIsAnimated = animated
            menuMainFabIconDefaultOpenAnimatorSet.duration = (if (animated) ANIMATION_DURATION else 0.toLong())
            menuMainFabIconDefaultCloseAnimatorSet.duration =
                (if (animated) ANIMATION_DURATION else 0.toLong())
        }

    /**
     * Initialize setting for all fab in menu.
     *
     * @since 0.0.1
     */
    private fun initMenuFabSetting(attr: TypedArray) {
        menuFabShowShadow =
            attr.getBoolean(R.styleable.FloatingActionMenu_menu_fab_show_shadow, menuFabShowShadow)
        menuFabShadowRadius = attr.getDimension(
            R.styleable.FloatingActionMenu_menu_fab_shadow_radius,
            menuFabShadowRadius
        )
        menuFabShadowXOffset = attr.getDimension(
            R.styleable.FloatingActionMenu_menu_fab_shadow_x_offset,
            menuFabShadowXOffset
        )
        menuFabShadowYOffset = attr.getDimension(
            R.styleable.FloatingActionMenu_menu_fab_shadow_y_offset,
            menuFabShadowYOffset
        )
        menuFabNormalColor =
            attr.getColor(R.styleable.FloatingActionMenu_menu_fab_color_normal, menuFabNormalColor)
        menuFabPressedColor = attr.getColor(
            R.styleable.FloatingActionMenu_menu_fab_color_pressed,
            menuFabPressedColor
        )
        menuFabRippleColor =
            attr.getColor(R.styleable.FloatingActionMenu_menu_fab_color_ripple, menuFabRippleColor)
        menuFabShadowColor =
            attr.getColor(R.styleable.FloatingActionMenu_menu_fab_shadow_color, menuFabShadowColor)
        menuFabSpacing = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_fab_spacing,
            menuFabSpacing
        )

        if (attr.hasValue(R.styleable.FloatingActionMenu_menu_main_fab_label_text)) {
            menuFabLabelEnabled = true
        }

        menuMainFab = FloatingActionButton(context).apply {
            setMenuMain(true)
        }

        menuMainFabSize = attr.getInt(
            R.styleable.FloatingActionMenu_menu_main_fab_size,
            FloatingActionButton.SIZE_NORMAL
        )
        menuMainFabLabelText =
            attr.getString(R.styleable.FloatingActionMenu_menu_main_fab_label_text) ?: ""

        addView(menuMainFab, super.generateDefaultLayoutParams())
    }

    /**
     * Initialize [fab] in menu.
     *
     * @since 0.0.1
     */
    private fun initMenuFab(fab: FloatingActionButton) {
        fab.apply {
            setShowShadow(menuFabShowShadow)
            if (showShadow) {
                setShadowRadius(menuFabShadowRadius)
                setShadowXOffset(menuFabShadowXOffset)
                setShadowYOffset(menuFabShadowYOffset)
            }
            setColors(menuFabNormalColor, menuFabPressedColor, 0, menuFabRippleColor)
            shadowColor = menuFabShadowColor
            updateBackground()
        }
    }

    private fun initMenuFabIcon(attr: TypedArray) {
        menuMainFabIcon = AppCompatImageView(context).apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    attr.getResourceId(
                        R.styleable.FloatingActionMenu_menu_main_fab_icon,
                        R.drawable.fab_add
                    )
                )
            )
        }
        addView(menuMainFabIcon, super.generateDefaultLayoutParams())
        initMenuFabIconAnimation()
    }

    private fun initMenuFabIconAnimation() {
        val collapseAngle: Float
        val expandAngle: Float
        if (mOpenDirection == OPEN_UP) {
            collapseAngle =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_LEFT else OPENED_PLUS_ROTATION_RIGHT
            expandAngle =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_LEFT else OPENED_PLUS_ROTATION_RIGHT
        } else {
            collapseAngle =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_RIGHT else OPENED_PLUS_ROTATION_LEFT
            expandAngle =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) OPENED_PLUS_ROTATION_RIGHT else OPENED_PLUS_ROTATION_LEFT
        }
        val collapseAnimator = ObjectAnimator.ofFloat(
            menuMainFabIcon,
            "rotation",
            collapseAngle,
            CLOSED_PLUS_ROTATION
        )
        val expandAnimator = ObjectAnimator.ofFloat(
            menuMainFabIcon,
            "rotation",
            CLOSED_PLUS_ROTATION,
            expandAngle
        )
        menuMainFabIconDefaultOpenAnimatorSet.apply {
            play(expandAnimator)
            interpolator = menuMainFabOpenInterpolator
            duration = ANIMATION_DURATION
        }
        menuMainFabIconDefaultCloseAnimatorSet.apply {
            play(collapseAnimator)
            interpolator = menuMainFabCloseInterpolator
            duration = ANIMATION_DURATION
        }
    }

    /**
     * Initialize [menuLabelsViewLayoutId].
     *
     * @since 0.0.1
     */
    private fun initMenuFabLabel(attr: TypedArray) {
        menuLabelsViewLayoutId = attr.getResourceId(
            R.styleable.FloatingActionMenu_menu_labels_view,
            R.layout.simple_menu_fab_label
        )
    }

    private fun initBackgroundDimAnimation(attr: TypedArray) {
        menuBackgroundColor =
            attr.getColor(R.styleable.FloatingActionMenu_menu_background_color, Color.TRANSPARENT)
        val maxAlpha = Color.alpha(menuBackgroundColor)
        val red = Color.red(menuBackgroundColor)
        val green = Color.green(menuBackgroundColor)
        val blue = Color.blue(menuBackgroundColor)
        menuBackgroundShowAnimator = ValueAnimator.ofInt(0, maxAlpha).apply {
            duration = ANIMATION_DURATION
            addUpdateListener { animation ->
                val alpha = animation.animatedValue as Int
                setBackgroundColor(Color.argb(alpha, red, green, blue))
            }
        }
        menuBackgroundHideAnimator = ValueAnimator.ofInt(maxAlpha, 0).apply {
            duration = ANIMATION_DURATION
            addUpdateListener { animation ->
                val alpha = animation.animatedValue as Int
                setBackgroundColor(Color.argb(alpha, red, green, blue))
            }
        }
    }

    private fun initMenuButtonAnimations(attr: TypedArray) {
        val showResId = attr.getResourceId(
            R.styleable.FloatingActionMenu_menu_fab_show_animation,
            R.anim.fab_scale_up
        )
        setMenuButtonShowAnimation(AnimationUtils.loadAnimation(context, showResId))
        menuMainFabIconShowAnimation = AnimationUtils.loadAnimation(context, showResId)
        val hideResId = attr.getResourceId(
            R.styleable.FloatingActionMenu_menu_fab_hide_animation,
            R.anim.fab_scale_down
        )
        setMenuButtonHideAnimation(AnimationUtils.loadAnimation(context, hideResId))
        menuMainFabIconHideAnimation = AnimationUtils.loadAnimation(context, hideResId)
    }

    private fun initMenuFabLabelsSetting(attr: TypedArray) {
        val padding =
            attr.getDimensionPixelSize(R.styleable.FloatingActionMenu_menu_labels_padding, 0)
        menuLabelsPaddingTop = padding
        menuLabelsPaddingRight = padding
        menuLabelsPaddingBottom = padding
        menuLabelsPaddingLeft = padding
        menuLabelsShowAnimation = attr.getResourceId(
            R.styleable.FloatingActionMenu_menu_labels_show_animation,
            if (menuLabelsPosition == LABELS_POSITION_LEFT) R.anim.fab_slide_in_from_right else R.anim.fab_slide_in_from_left
        )
        menuLabelsHideAnimation = attr.getResourceId(
            R.styleable.FloatingActionMenu_menu_labels_hide_animation,
            if (menuLabelsPosition == LABELS_POSITION_LEFT) R.anim.fab_slide_out_to_right else R.anim.fab_slide_out_to_left
        )
    }

    /**
     * Set fab normal state color by [colorResId].
     *
     * @param colorResId normal state color resource id.
     * @since 0.0.1
     */
    fun setMenuFabNormalColorResId(colorResId: Int) {
        menuFabNormalColor = resources.getColor(colorResId, context.theme)
    }

    /**
     * Set fab pressed state color by [colorResId].
     *
     * @param colorResId pressed state color resource id.
     * @since 0.0.1
     */
    fun setMenuFabPressedColorResId(colorResId: Int) {
        menuFabPressedColor = resources.getColor(colorResId, context.theme)
    }

    /**
     * Set fab ripple color by [colorResId].
     *
     * @param colorResId ripple color resource id.
     * @since 0.0.1
     */
    fun setMenuFabRippleColorResId(colorResId: Int) {
        menuFabRippleColor = resources.getColor(colorResId, context.theme)
    }

    /**
     * Set the menu fab LabelView by [fabLabelLayoutId].
     *
     * @param fabLabelLayoutId the [LabelView] layout id of the menu fab.
     * @since 0.0.1
     */
    fun setMenuFabLabel(@LayoutRes fabLabelLayoutId: Int) {
        menuLabelsViewLayoutId = fabLabelLayoutId
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        bringChildToFront(menuMainFab)
        bringChildToFront(menuMainFabIcon)
        menuFabCount = childCount
        createLabels()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width: Int
        var height = 0
        menuFabMaxWidth = 0
        var maxLabelWidth = 0

        measureChildWithMargins(menuMainFabIcon, widthMeasureSpec, 0, heightMeasureSpec, 0)

        for (i in 0 until menuFabCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE || child == menuMainFabIcon)
                continue
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            menuFabMaxWidth = menuFabMaxWidth.coerceAtLeast(child.measuredWidth)
        }
        for (i in 0 until menuFabCount) {
            var usedWidth = 0
            val child = getChildAt(i)
            if (child.visibility == GONE || child == menuMainFabIcon)
                continue
            usedWidth += child.measuredWidth
            height += child.measuredHeight
            val label = (child as FloatingActionButton).labelView
            if (null != label) {
                val labelOffset =
                    (menuFabMaxWidth - child.measuredWidth) / if (menuFabLabelEnabled) 1 else 2
                val labelUsedWidth =
                    child.measuredWidth + label.calculateShadowWidth()
                        .roundToInt() + menuLabelsMargin + labelOffset
                measureChildWithMargins(
                    label,
                    widthMeasureSpec,
                    labelUsedWidth,
                    heightMeasureSpec,
                    0
                )
                usedWidth += label.measuredWidth
                maxLabelWidth = maxLabelWidth.coerceAtLeast(usedWidth + labelOffset)
            }
        }
        width =
            menuFabMaxWidth.coerceAtLeast(maxLabelWidth + menuLabelsMargin) + paddingLeft + paddingRight
        height += menuFabSpacing * (menuFabCount - 1) + paddingTop + paddingBottom
        if (layoutParams.width == LayoutParams.MATCH_PARENT) {
            width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        }
        if (layoutParams.height == LayoutParams.MATCH_PARENT) {
            height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val buttonsHorizontalCenter =
            if (menuLabelsPosition == LABELS_POSITION_LEFT) r - l - menuFabMaxWidth / 2 - paddingRight else menuFabMaxWidth / 2 + paddingLeft
        val openUp = mOpenDirection == OPEN_UP
        val menuMainFabTop =
            if (openUp) b - t - menuMainFab.measuredHeight - paddingBottom else paddingTop
        val menuMainFabLeft = buttonsHorizontalCenter - menuMainFab.measuredWidth / 2
        menuMainFab.layout(
            menuMainFabLeft, menuMainFabTop, menuMainFabLeft + menuMainFab.measuredWidth,
            menuMainFabTop + menuMainFab.measuredHeight
        )
        val imageLeft: Int = buttonsHorizontalCenter - menuMainFabIcon.measuredWidth / 2
        val imageTop: Int =
            menuMainFabTop + menuMainFab.measuredHeight / 2 - menuMainFabIcon.measuredHeight / 2
        menuMainFabIcon.layout(
            imageLeft, imageTop, imageLeft + menuMainFabIcon.measuredWidth,
            imageTop + menuMainFabIcon.measuredHeight
        )
        var nextY =
            if (openUp) menuMainFabTop + menuMainFab.measuredHeight + menuFabSpacing else menuMainFabTop
        for (i in menuFabCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (child == menuMainFabIcon)
                continue
            val fab = child as FloatingActionButton
            if (fab.visibility == GONE) continue
            val childX = buttonsHorizontalCenter - fab.measuredWidth / 2
            val childY = if (openUp) nextY - fab.measuredHeight - menuFabSpacing else nextY
            if (!fab.isMenuMain) {
                fab.layout(
                    childX, childY, childX + fab.measuredWidth, childY + fab.measuredHeight
                )
                if (!mIsMenuOpening) {
                    fab.hide(false)
                }
            }
            val label = fab.labelView
            val labelsOffset =
                (if (menuFabLabelEnabled) menuFabMaxWidth / 2 else fab.measuredWidth / 2) + menuLabelsMargin
            val labelXNearButton =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) buttonsHorizontalCenter - labelsOffset else buttonsHorizontalCenter + labelsOffset
            val labelXAwayFromButton =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) labelXNearButton - (label?.measuredWidth
                    ?: 0) else labelXNearButton + (label?.measuredWidth ?: 0)
            val labelLeft =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) labelXAwayFromButton else labelXNearButton
            val labelRight =
                if (menuLabelsPosition == LABELS_POSITION_LEFT) labelXNearButton else labelXAwayFromButton
            val labelTop = childY - mLabelsVerticalOffset + (fab.measuredHeight
                    - (label?.measuredHeight ?: 0)) / 2
            label?.layout(
                labelLeft,
                labelTop.roundToInt(),
                labelRight,
                labelTop.roundToInt() + label.measuredHeight
            )
            if (!mIsMenuOpening) {
                if (label != null) {
                    label.visibility = INVISIBLE
                }
            }
            nextY =
                if (openUp) childY else childY + child.getMeasuredHeight() + menuFabSpacing
        }
    }

    /**
     * Adjust the height appropriately. It should be called in [onMeasure] when
     * needed.
     *
     * @since 0.0.1
     */
    private fun adjustForOvershoot(dimension: Int) =
        (dimension * 0.03 + dimension).toInt()

    private fun createLabels() {
        for (i in 0 until menuFabCount) {
            if (getChildAt(i) == menuMainFabIcon)
                continue
            val fab = getChildAt(i) as FloatingActionButton
            initMenuFab(fab)
            if (fab.labelView != null) continue
            addLabel(fab)
            if (fab == menuMainFab) {
                menuMainFab.setOnClickListener { toggle(mIsAnimated) }
            }
        }
    }

    private fun addLabel(fab: FloatingActionButton) {
        val text = fab.labelText
        Log.d(tag, "$text ${fab.isMenuMain}")
        if (TextUtils.isEmpty(text))
            return
        val mfl = LayoutInflater.from(context).inflate(menuLabelsViewLayoutId, this, false)
            .findViewById<LabelView>(R.id.menu_fab_label) ?: return
        mfl.apply {
            isClickable = true
            setFab(fab)
            setShowAnimation(AnimationUtils.loadAnimation(context, menuLabelsShowAnimation))
            setHideAnimation(AnimationUtils.loadAnimation(context, menuLabelsHideAnimation))
            if (mLabelsStyle > 0) {
                setTextAppearance(mLabelsStyle)
                setShowShadow(false)
                setUsingStyle(true)
            } else {
                setColors(mLabelsColorNormal, mLabelsColorPressed, mLabelsColorRipple)
                setShowShadow(mLabelsShowShadow)
                setCornerRadius(mLabelsCornerRadius)
                if (mLabelsEllipsize > 0) {
                    setLabelEllipsize(this)
                }
                maxLines = mLabelsMaxLines
                updateBackground()
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelsTextSize)
                setTextColor(mLabelsTextColor)
                var left = menuLabelsPaddingLeft
                var top = menuLabelsPaddingTop
                if (mLabelsShowShadow) {
                    left += fab.shadowRadius.roundToInt() + abs(fab.shadowXOffset).roundToInt()
                    top += fab.shadowRadius.roundToInt() + abs(fab.shadowYOffset).roundToInt()
                }
                setPadding(left, top, menuLabelsPaddingLeft, menuLabelsPaddingTop)
                if (mLabelsMaxLines < 0 || mLabelsSingleLine) {
                    isSingleLine = mLabelsSingleLine
                }
            }
            if (mCustomTypefaceFromFont != null) {
                typeface = mCustomTypefaceFromFont
            }
            this.text = text
            setOnClickListener(fab.clickListener)
        }
        fab.setLabelView(mfl)
        addView(mfl)
    }

    private fun setLabelEllipsize(label: LabelView) {
        when (mLabelsEllipsize) {
            1 -> label.ellipsize = TextUtils.TruncateAt.START
            2 -> label.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> label.ellipsize = TextUtils.TruncateAt.END
            4 -> label.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): MarginLayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): MarginLayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): MarginLayoutParams {
        return MarginLayoutParams(
            MarginLayoutParams.WRAP_CONTENT,
            MarginLayoutParams.WRAP_CONTENT
        )
    }

    override fun checkLayoutParams(p: LayoutParams): Boolean {
        return p is MarginLayoutParams
    }

    /**
     * @see hideMenuMainFab
     * @since 0.0.1
     */
    private fun hideMenuMainFabWithImage(animate: Boolean) {
        if (!isMenuMainFabHidden()) {
            menuMainFab.hide(animate)
            if (animate) {
                menuMainFabIcon.startAnimation(menuMainFabIconHideAnimation)
            }
            menuMainFabIcon.visibility = INVISIBLE
            mIsMenuButtonAnimationRunning = false
        }
    }

    /**
     * @see showMenuMainFab
     * @since 0.0.1
     */
    private fun showMenuMainFabWithImage(animate: Boolean) {
        if (isMenuMainFabHidden()) {
            menuMainFab.show(animate)
            if (animate) {
                menuMainFabIcon.startAnimation(menuMainFabIconShowAnimation)
            }
            menuMainFabIcon.visibility = VISIBLE
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mIsSetClosedOnTouchOutside) {
            var handled = false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> handled = menuIsOpened
                MotionEvent.ACTION_UP -> {
                    close(mIsAnimated)
                    handled = true
                }
            }
            return handled
        }
        return super.onTouchEvent(event)
    }

    fun toggle(animate: Boolean) {
        if (menuIsOpened) {
            close(animate)
        } else {
            open(animate)
        }
    }

    fun open(animate: Boolean) {
        if (!menuIsOpened) {
            if (menuBackgroundEnabled) {
                menuBackgroundShowAnimator.start()
            }
            if (isIconAnimated) {
                if (menuMainFabIconAnimatorSet != null) {
                    menuMainFabIconAnimatorSet!!.start()
                } else {
                    menuMainFabIconDefaultCloseAnimatorSet.cancel()
                    menuMainFabIconDefaultOpenAnimatorSet.start()
                }
            }
            var delay = 0
            var counter = 0
            mIsMenuOpening = true
            for (i in childCount - 1 downTo 0) {
                val child = getChildAt(i)
                if (child is FloatingActionButton && child.getVisibility() != GONE) {
                    counter++
                    mUiHandler.postDelayed(Runnable {
                        if (menuIsOpened) return@Runnable
                        if (child != menuMainFab) {
                            child.show(animate)
                        }
                        val label = child.labelView
                        if (label?.isHandleVisibilityChanges == true) {
                            label.show(animate)
                        }
                    }, delay.toLong())
                    delay += animationDelayPerItem
                }
            }
            mUiHandler.postDelayed({
                menuIsOpened = true
            }, (++counter * animationDelayPerItem).toLong())
        }
    }

    fun close(animate: Boolean) {
        if (menuIsOpened) {
            if (menuBackgroundEnabled) {
                menuBackgroundHideAnimator.start()
            }
            if (isIconAnimated) {
                if (menuMainFabIconAnimatorSet != null) {
                    menuMainFabIconAnimatorSet!!.start()
                } else {
                    menuMainFabIconDefaultCloseAnimatorSet.start()
                    menuMainFabIconDefaultOpenAnimatorSet.cancel()
                }
            }
            var delay = 0
            var counter = 0
            mIsMenuOpening = false
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is FloatingActionButton && child.getVisibility() != GONE) {
                    counter++
                    mUiHandler.postDelayed(Runnable {
                        if (!menuIsOpened) return@Runnable
                        if (child != menuMainFab) {
                            child.hide(animate)
                        }
                        val label = child.labelView
                        if (label?.isHandleVisibilityChanges == true) {
                            label.hide(animate)
                        }
                    }, delay.toLong())
                    delay += animationDelayPerItem
                }
            }
            mUiHandler.postDelayed({
                menuIsOpened = false
            }, (++counter * animationDelayPerItem).toLong())
        }
    }

    /**
     * Sets the [Interpolator] for **FloatingActionButton's** icon animation.
     *
     * @param interpolator the Interpolator to be used in animation
     */
    fun setIconAnimationInterpolator(interpolator: Interpolator?) {
        menuMainFabIconDefaultOpenAnimatorSet.interpolator = interpolator
        menuMainFabIconDefaultCloseAnimatorSet.interpolator = interpolator
    }

    fun setIconAnimationOpenInterpolator(openInterpolator: Interpolator?) {
        menuMainFabIconDefaultOpenAnimatorSet.interpolator = openInterpolator
    }

    fun setIconAnimationCloseInterpolator(closeInterpolator: Interpolator?) {
        menuMainFabIconDefaultCloseAnimatorSet.interpolator = closeInterpolator
    }

    fun setMenuButtonShowAnimation(showAnimation: Animation?) {
        mMenuButtonShowAnimation = showAnimation
        menuMainFab.showAnimation = showAnimation
    }

    fun setMenuButtonHideAnimation(hideAnimation: Animation?) {
        mMenuButtonHideAnimation = hideAnimation
        menuMainFab.hideAnimation = hideAnimation
    }

    /**
     * Makes the whole [.FloatingActionMenu] to appear and sets its visibility
     * to [.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun showMenu(animate: Boolean) {
        if (menuHidden) {
            if (animate) {
                startAnimation(mMenuButtonShowAnimation)
            }
            visibility = VISIBLE
        }
    }

    /**
     * Makes the [.FloatingActionMenu] to disappear and sets its visibility to
     * [.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hideMenu(animate: Boolean) {
        if (!menuHidden && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true
            if (menuIsOpened) {
                close(animate)
                mUiHandler.postDelayed({
                    if (animate) {
                        startAnimation(mMenuButtonHideAnimation)
                    }
                    visibility = INVISIBLE
                    mIsMenuButtonAnimationRunning = false
                }, (animationDelayPerItem * menuFabCount).toLong())
            } else {
                if (animate) {
                    startAnimation(mMenuButtonHideAnimation)
                }
                visibility = INVISIBLE
                mIsMenuButtonAnimationRunning = false
            }
        }
    }

    fun toggleMenu(animate: Boolean) {
        if (menuHidden) {
            showMenu(animate)
        } else {
            hideMenu(animate)
        }
    }

    /**
     * Makes the [menuMainFab] to appear inside the
     * [FloatingActionMenu] and sets its visibility to [View.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun showMenuMainFab(animate: Boolean) {
        if (isMenuMainFabHidden()) {
            showMenuMainFabWithImage(animate)
        }
    }

    /**
     * Makes the [menuMainFab] to disappear inside the
     * [FloatingActionMenu] and sets its visibility to [View.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hideMenuMainFab(animate: Boolean) {
        if (!isMenuMainFabHidden() && !mIsMenuButtonAnimationRunning) {
            mIsMenuButtonAnimationRunning = true
            if (menuIsOpened) {
                close(animate)
                mUiHandler.postDelayed(
                    { hideMenuMainFabWithImage(animate) },
                    animationDelayPerItem.toLong() * menuFabCount
                )
            } else {
                hideMenuMainFabWithImage(animate)
            }
        }
    }

    fun toggleMenuButton(animate: Boolean) {
        if (isMenuMainFabHidden()) {
            showMenuMainFab(animate)
        } else {
            hideMenuMainFab(animate)
        }
    }

    fun setClosedOnTouchOutside(close: Boolean) {
        mIsSetClosedOnTouchOutside = close
    }

    fun addMenuFab(fab: FloatingActionButton) {
        addView(fab, menuFabCount - 2)
        menuFabCount++
        addLabel(fab)
    }

    /**
     * @return true if [menuMainFab] is hidden, false otherwise.
     * @since 0.0.1
     */
    fun isMenuMainFabHidden() = menuMainFab.isHidden()

    /**
     * Remove all fab in menu except [menuMainFab].
     *
     * @since 0.0.1
     */
    fun removeAllMenuFab() {
        close(true)
        val viewsToRemove: MutableList<FloatingActionButton> = ArrayList()
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v is FloatingActionButton && !v.isMenuMain) {
                viewsToRemove.add(v)
            }
        }
        for (view in viewsToRemove) {
            removeMenuFab(view)
        }
    }

    /**
     * Remove fab in menu.
     *
     * @since 0.0.1
     */
    fun removeMenuFab(fab: FloatingActionButton) {
        removeView(fab.labelView)
        removeView(fab)
        menuFabCount--
    }

    /**
     * Add [fab] into the menu with [index].
     *
     * @since 0.0.1
     */
    fun addMenuFab(fab: FloatingActionButton, index: Int) {
        var originIndex = index
        val size = menuFabCount - 2
        if (originIndex < 0) {
            originIndex = 0
        } else if (originIndex > size) {
            originIndex = size
        }
        addView(fab, originIndex)
        menuFabCount++
        addLabel(fab)
    }

    companion object {
        private const val ANIMATION_DURATION = 300L
        private const val CLOSED_PLUS_ROTATION = 0f
        private const val OPENED_PLUS_ROTATION_LEFT = -135f
        private const val OPENED_PLUS_ROTATION_RIGHT = 135f

        /**
         * Menu is open up.
         *
         * @since 0.0.1
         */
        private const val OPEN_UP = 0

        /**
         * Menu is open down.
         *
         * @since 0.0.1
         */
        private const val OPEN_DOWN = 1

        /**
         * Label will show in left position.
         *
         * @since 0.0.1
         */
        private const val LABELS_POSITION_LEFT = 0

        /**
         * Label will show in right position.
         *
         * @since 0.0.1
         */
        private const val LABELS_POSITION_RIGHT = 1
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LABELS_POSITION_LEFT, LABELS_POSITION_RIGHT)
    annotation class LabelsPosition

    init {
        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, defStyleAttr, 0)
        menuLabelsMargin = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_margin,
            menuLabelsMargin
        )
        menuLabelsPosition =
            attr.getInt(R.styleable.FloatingActionMenu_menu_labels_position, LABELS_POSITION_LEFT)
        menuLabelsPaddingTop = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_paddingTop,
            menuLabelsPaddingTop
        )
        menuLabelsPaddingRight = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_paddingRight,
            menuLabelsPaddingRight
        )
        menuLabelsPaddingBottom = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_paddingBottom,
            menuLabelsPaddingBottom
        )
        menuLabelsPaddingLeft = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_paddingLeft,
            menuLabelsPaddingLeft
        )
        mLabelsTextColor =
            attr.getColorStateList(R.styleable.FloatingActionMenu_menu_labels_textColor)
        // set default value if null same as for textview
        if (mLabelsTextColor == null) {
            mLabelsTextColor = ColorStateList.valueOf(Color.WHITE)
        }
        mLabelsTextSize = attr.getDimension(
            R.styleable.FloatingActionMenu_menu_labels_textSize,
            resources.getDimension(R.dimen.labels_text_size)
        )
        mLabelsCornerRadius = attr.getDimensionPixelSize(
            R.styleable.FloatingActionMenu_menu_labels_cornerRadius,
            mLabelsCornerRadius
        )
        mLabelsShowShadow =
            attr.getBoolean(R.styleable.FloatingActionMenu_menu_labels_showShadow, true)
        mLabelsColorNormal =
            attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorNormal, -0xcccccd)
        mLabelsColorPressed =
            attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorPressed, -0xbbbbbc)
        mLabelsColorRipple =
            attr.getColor(R.styleable.FloatingActionMenu_menu_labels_colorRipple, 0x66FFFFFF)
        animationDelayPerItem =
            attr.getInt(R.styleable.FloatingActionMenu_menu_animationDelayPerItem, 50)
        mLabelsSingleLine =
            attr.getBoolean(R.styleable.FloatingActionMenu_menu_labels_singleLine, false)
        mLabelsEllipsize = attr.getInt(R.styleable.FloatingActionMenu_menu_labels_ellipsize, 0)
        mLabelsMaxLines = attr.getInt(R.styleable.FloatingActionMenu_menu_labels_maxLines, -1)
        mLabelsStyle = attr.getResourceId(R.styleable.FloatingActionMenu_menu_labels_style, 0)
        val customFont = attr.getString(R.styleable.FloatingActionMenu_menu_labels_customFont)
        try {
            if (!TextUtils.isEmpty(customFont)) {
                mCustomTypefaceFromFont = Typeface.createFromAsset(getContext().assets, customFont)
            }
        } catch (ex: RuntimeException) {
            throw IllegalArgumentException("Unable to load specified custom font: $customFont", ex)
        }
        mOpenDirection = attr.getInt(R.styleable.FloatingActionMenu_menu_openDirection, OPEN_UP)
        menuMainFabOpenInterpolator = OvershootInterpolator()
        menuMainFabCloseInterpolator = AnticipateInterpolator()
        mLabelsContext = ContextThemeWrapper(getContext(), mLabelsStyle)
        initMenuFabLabelsSetting(attr)
        initBackgroundDimAnimation(attr)
        initMenuFabIcon(attr)
        initMenuFabSetting(attr)
        initMenuFabLabel(attr)
        initMenuButtonAnimations(attr)
        attr.recycle()
    }
}