@file:JvmName("TuplesKt")

package com.ave.vastgui.core.utils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/28
// Description: 
// Documentation:
// Reference:

/**
 * Represents a quadruple of values.
 *
 * @since 0.0.4
 */
data class Quadruple<out A, out B, out C, out D>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D
) {
    override fun toString() = "($param1,$param2,$param3,$param4)"
}

/**
 * Represents a quintuple of values.
 *
 * @since 0.0.4
 */
data class Quintuple<out A, out B, out C, out D, out E>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D,
    val param5: E
) {
    override fun toString() = "($param1,$param2,$param3,$param4$param4)"
}