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

package com.ave.vastgui.tools.os

import android.os.Handler
import android.os.Handler.Callback
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.os.Process
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/30

/**
 * Default implementation of [Handler.Callback].
 *
 * @since 1.5.2
 */
internal object DefaultCallback : Callback {
    override fun handleMessage(msg: Message): Boolean = false
}

/**
 * A [Handler] will call [removeCallbacksAndMessages] when the [onDestroy]
 * of [LifecycleOwner] is called.
 *
 * @param looper The looper.
 * @param callback The callback interface in which to handle messages.
 * @since 1.5.2
 */
open class LifecycleHandler @JvmOverloads constructor(
    looper: Looper = Looper.getMainLooper(),
    callback: Callback = DefaultCallback
) : Handler(looper, callback),
    ReadOnlyProperty<LifecycleOwner, Handler>,
    DefaultLifecycleObserver {

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Handler {
        thisRef.lifecycle.addObserver(this)
        return this
    }

    override fun onDestroy(owner: LifecycleOwner) {
        removeCallbacksAndMessages(null)
        owner.lifecycle.addObserver(this)
    }
}

/**
 * A [Handler] with the [Looper] provided by [LifecycleHandlerThread], will
 * call [quitSafely] when the [onDestroy] of [LifecycleOwner] is called.
 *
 * @param name The name of the new thread.
 * @param priority The priority to run the thread at. The value supplied
 * must be from [Process] and not from [Thread].
 * @param callback The callback interface in which to handle messages.
 * @since 1.5.2
 */
open class LifecycleHandlerThread @JvmOverloads constructor(
    name: String,
    priority: Int = Process.THREAD_PRIORITY_DEFAULT,
    private val callback: Callback = DefaultCallback
) : HandlerThread(name, priority),
    ReadOnlyProperty<LifecycleOwner, Handler>,
    DefaultLifecycleObserver {

    private lateinit var handlerImpl: Handler

    /**
     * The override of [Handler.handleMessage] for [handlerImpl].
     *
     * @since 1.5.2
     */
    open val handleMessage: ((Message) -> Unit)? = null

    /**
     * The override of [Handler.dispatchMessage] for [handlerImpl].
     *
     * @since 1.5.2
     */
    open val dispatchMessage: ((Message) -> String)? = null

    /**
     * The override of [Handler.sendMessageAtTime] for [handlerImpl].
     *
     * @since 1.5.2
     */
    open val sendMessageAtTime: ((Message, Long) -> Boolean)? = null

    /**
     * The override of [Handler.getMessageName] for [handlerImpl].
     *
     * @since 1.5.2
     */
    open val getMessageName: ((Message) -> String)? = null

    /**
     * The override of [Handler.toString] for [handlerImpl].
     *
     * @since 1.5.2
     */
    open val handlerToString: (() -> String)? = null

    override fun getValue(thisRef: LifecycleOwner, property: KProperty<*>): Handler {
        if (!::handlerImpl.isInitialized) {
            thisRef.lifecycle.addObserver(this)
            this.start()
            handlerImpl = HandlerImpl(this.looper, callback)
        }
        return handlerImpl
    }

    override fun onDestroy(owner: LifecycleOwner) {
        this.quitSafely()
        owner.lifecycle.addObserver(this)
    }

    /**
     * The implementation of [Handler].
     *
     * @since 1.5.2
     */
    private inner class HandlerImpl(looper: Looper, callback: Callback) : Handler(looper, callback) {
        override fun handleMessage(message: Message) {
            if (handleMessage != null) {
                handleMessage!!.invoke(message)
            } else {
                super.handleMessage(message)
            }
        }

        override fun dispatchMessage(message: Message) {
            if (dispatchMessage != null) {
                dispatchMessage!!.invoke(message)
            } else {
                super.dispatchMessage(message)
            }
        }

        override fun sendMessageAtTime(message: Message, uptimeMillis: Long): Boolean {
            return sendMessageAtTime?.invoke(message, uptimeMillis)
                ?: super.sendMessageAtTime(message, uptimeMillis)
        }

        override fun getMessageName(message: Message): String {
            return getMessageName?.invoke(message) ?: super.getMessageName(message)
        }

        override fun toString(): String {
            return handlerToString?.invoke() ?: super.toString()
        }
    }

}