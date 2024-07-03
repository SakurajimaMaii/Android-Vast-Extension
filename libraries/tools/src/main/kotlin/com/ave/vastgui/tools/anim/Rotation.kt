/*
 * Copyright 2021-2024 VastGui
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

@file:JvmName("RotationKt")

package com.ave.vastgui.tools.anim

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/17
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/animation/flip-animation/

/**
 * Y-axis flip animation.
 *
 * @param initFront Front-facing component.
 * @param initBack Component shown on the back.
 * @param frontAnima Configure the animation of the view in [View.VISIBLE].
 * @param backAnim Configure the animation of the view in [View.GONE].
 * @since 0.5.3
 */
@JvmOverloads
inline fun flipYAnimation(
    initFront: View,
    initBack: View,
    frontAnima: (ObjectAnimator) -> Unit = { animator ->
        animator.setDuration(500)
        animator.interpolator = AccelerateInterpolator()
    },
    backAnim: (ObjectAnimator) -> Unit = { animator ->
        animator.setDuration(500)
        animator.interpolator = DecelerateInterpolator()
    }
) {
    if (initFront.left != initBack.left || initFront.right != initBack.right || initFront.top != initBack.top || initFront.bottom != initBack.bottom)
        return
    initBack.rotationY = -90f
    val visView = if (initFront.visibility == View.GONE) initBack else initFront
    val unVisView = if (initFront.visibility == View.GONE) initFront else initBack
    val bAnimation = ObjectAnimator.ofFloat(unVisView, "rotationY", -90f, 0f)
    val fAnimation = ObjectAnimator.ofFloat(visView, "rotationY", 0f, 90f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visView.visibility = View.GONE
                bAnimation.start()
                unVisView.visibility = View.VISIBLE
            }
        })
    }
    frontAnima(fAnimation)
    backAnim(bAnimation)
    fAnimation.start()
}

/**
 * X-axis flip animation.
 *
 * @param initFront Front-facing component.
 * @param initBack Component shown on the back.
 * @param frontAnima Configure the animation of the view in [View.VISIBLE].
 * @param backAnim Configure the animation of the view in [View.GONE].
 * @since 0.5.3
 */
@JvmOverloads
inline fun flipXAnimation(
    initFront: View,
    initBack: View,
    frontAnima: (ObjectAnimator) -> Unit = { animator ->
        animator.setDuration(500)
        animator.interpolator = AccelerateInterpolator()
    },
    backAnim: (ObjectAnimator) -> Unit = { animator ->
        animator.setDuration(500)
        animator.interpolator = DecelerateInterpolator()
    }
) {
    if (initFront.left != initBack.left || initFront.right != initBack.right || initFront.top != initBack.top || initFront.bottom != initBack.bottom)
        return
    initBack.rotationX = -90f
    val visView = if (initFront.visibility == View.GONE) initBack else initFront
    val unVisView = if (initFront.visibility == View.GONE) initFront else initBack
    val bAnimation = ObjectAnimator.ofFloat(unVisView, "rotationX", -90f, 0f)
    val fAnimation = ObjectAnimator.ofFloat(visView, "rotationX", 0f, 90f).apply {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visView.visibility = View.GONE
                bAnimation.start()
                unVisView.visibility = View.VISIBLE
            }
        })
    }
    frontAnima(fAnimation)
    backAnim(bAnimation)
    fAnimation.start()
}