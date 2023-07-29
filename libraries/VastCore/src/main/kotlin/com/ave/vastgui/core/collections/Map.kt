@file:JvmName("MapKt")

package com.ave.vastgui.core.collections

import kotlin.reflect.full.memberProperties

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/28
// Description: 
// Documentation:
// Reference:

/**
 * Return object as a map, where key is the parameter name and value is the
 * parameter value.
 *
 * @since 0.0.4
 */
fun Any.toMap() = this::class
    .memberProperties
    .associate {
        it.name to it.call(this)
    }