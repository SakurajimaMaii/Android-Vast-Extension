package com.ave.vastgui.tools.view.extension

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/3/30
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/extension/view/

// ---------- visibility ----------
fun View.gone() {
    isGone = true
}

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    isInvisible = true
}