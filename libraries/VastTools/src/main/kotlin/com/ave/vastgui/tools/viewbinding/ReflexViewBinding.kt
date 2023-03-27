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

package com.ave.vastgui.tools.viewbinding

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.utils.LogUtils
import java.lang.reflect.ParameterizedType
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/4
// Description: 
// Documentation:
// Reference:

// ----------------------------------------------- Without base class ------------------------------------------------

// -------------------------------------------------------------------------------------
// ViewBindingProperty for Activity
// -------------------------------------------------------------------------------------

@JvmName("viewBindingActivity")
inline fun <V : ViewBinding> ComponentActivity.reflexViewBinding(
    crossinline viewBinder: LayoutInflater.() -> V
): ViewBindingProperty<ComponentActivity, V> =
    ActivityViewBindingProperty { activity: ComponentActivity ->
        viewBinder(activity.layoutInflater).apply {
            setContentView(root)
        }
    }

@JvmName("viewBindingActivity")
inline fun <V : ViewBinding> ComponentActivity.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (ComponentActivity) -> View = ::findRootView
): ViewBindingProperty<ComponentActivity, V> =
    ActivityViewBindingProperty { activity: ComponentActivity ->
        viewBinder(viewProvider(activity)).apply {
            setContentView(root)
        }
    }

@JvmName("viewBindingActivity")
inline fun <V : ViewBinding> ComponentActivity.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, V> =
    ActivityViewBindingProperty { activity: ComponentActivity ->
        viewBinder(activity.requireViewByIdCompat(viewBindingRootId)).apply {
            setContentView(root)
        }
    }

// -------------------------------------------------------------------------------------
// ViewBindingProperty for Fragment / DialogFragment
// -------------------------------------------------------------------------------------

@JvmName("viewBindingFragment")
inline fun <F : Fragment, V : ViewBinding> Fragment.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (F) -> View = Fragment::requireView
): ViewBindingProperty<F, V> = when (this) {
    is DialogFragment -> cast(DialogFragmentViewBindingProperty { fragment: F ->
        viewBinder(viewProvider(fragment))
    })

    else -> FragmentViewBindingProperty { fragment: F ->
        viewBinder(viewProvider(fragment))
    }
}

@JvmName("viewBindingFragment")
inline fun <F : Fragment, V : ViewBinding> Fragment.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<F, V> = when (this) {
    is DialogFragment -> cast(reflexViewBinding(viewBinder) { fragment: DialogFragment ->
        fragment.getRootView(viewBindingRootId)
    })

    else -> reflexViewBinding(viewBinder) { fragment: F ->
        fragment.requireView().requireViewByIdCompat(viewBindingRootId)
    }
}

// -------------------------------------------------------------------------------------
// ViewBindingProperty for ViewGroup
// -------------------------------------------------------------------------------------

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> ViewGroup.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (ViewGroup) -> View = { this }
): ViewBindingProperty<ViewGroup, V> = LazyViewBindingProperty { viewGroup: ViewGroup ->
    viewBinder(viewProvider(viewGroup))
}

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> ViewGroup.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ViewGroup, V> = LazyViewBindingProperty { viewGroup: ViewGroup ->
    viewBinder(viewGroup.requireViewByIdCompat(viewBindingRootId))
}

// -------------------------------------------------------------------------------------
// ViewBindingProperty for RecyclerView#ViewHolder
// -------------------------------------------------------------------------------------

@JvmName("viewBindingViewHolder")
inline fun <V : ViewBinding> RecyclerView.ViewHolder.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (RecyclerView.ViewHolder) -> View = RecyclerView.ViewHolder::itemView
): ViewBindingProperty<RecyclerView.ViewHolder, V> =
    LazyViewBindingProperty { holder: RecyclerView.ViewHolder ->
        viewBinder(viewProvider(holder))
    }

@JvmName("viewBindingViewHolder")
inline fun <V : ViewBinding> RecyclerView.ViewHolder.reflexViewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<RecyclerView.ViewHolder, V> =
    LazyViewBindingProperty { holder: RecyclerView.ViewHolder ->
        viewBinder(holder.itemView.requireViewByIdCompat(viewBindingRootId))
    }

// -------------------------------------------------------------------------------------
// ViewBindingProperty
// -------------------------------------------------------------------------------------

interface ViewBindingProperty<in R : Any, out V : ViewBinding> : ReadOnlyProperty<R, V> {
    @MainThread
    fun clear()
}

class LazyViewBindingProperty<in R : Any, out V : ViewBinding>(
    private val viewBinder: (R) -> V
) : ViewBindingProperty<R, V> {

    private var viewBinding: V? = null

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        // Already bound
        viewBinding?.let { return it }

        return viewBinder(thisRef).also {
            this.viewBinding = it
        }
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }
}

abstract class LifecycleViewBindingProperty<in R : Any, out V : ViewBinding>(
    private val viewBinder: (R) -> V
) : ViewBindingProperty<R, V> {

    private var viewBinding: V? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        // Already bound
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            LogUtils.w(
                "LifecycleViewBindingProperty",
                "Access to viewBinding after Lifecycle is destroyed or hasn't created yet. " +
                        "The instance of viewBinding will be not cached."
            )
            // We can access to ViewBinding after Fragment.onDestroyView(), but don't save it to prevent memory leak
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: LifecycleViewBindingProperty<*, *>
    ) : DefaultLifecycleObserver {

        private companion object {
            private val mainHandler = Handler(Looper.getMainLooper())
        }

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            mainHandler.post { property.clear() }
        }

    }
}

class FragmentViewBindingProperty<in F : Fragment, out V : ViewBinding>(
    viewBinder: (F) -> V
) : LifecycleViewBindingProperty<F, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        try {
            return thisRef.viewLifecycleOwner
        } catch (ignored: IllegalStateException) {
            error("Fragment doesn't have view associated with it or the view has been destroyed")
        }
    }

}

class DialogFragmentViewBindingProperty<in F : DialogFragment, out V : ViewBinding>(
    viewBinder: (F) -> V
) : LifecycleViewBindingProperty<F, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.showsDialog) {
            thisRef
        } else {
            try {
                thisRef.viewLifecycleOwner
            } catch (ignored: IllegalStateException) {
                error("Fragment doesn't have view associated with it or the view has been destroyed")
            }
        }
    }
}

// -------------------------------------------------------------------------------------
// Utils
// -------------------------------------------------------------------------------------

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ActivityViewBindingProperty<in A : ComponentActivity, out V : ViewBinding>(
    viewBinder: (A) -> V
) : LifecycleViewBindingProperty<A, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }

}

fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}

fun <V : View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this, id)
}

/** Utility to find root view for ViewBinding in Activity */
fun findRootView(activity: Activity): View {
    val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView) { "Activity has no content view" }
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("Content view has no children. Provide root view explicitly")
        else -> error("More than one child view found in Activity content view")
    }
}

fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog) {
        "DialogFragment doesn't have dialog. Use viewBinding delegate after onCreateDialog"
    }
    val window = checkNotNull(dialog.window) { "Fragment's Dialog has no window" }
    return with(window.decorView) {
        if (viewBindingRootId != 0) requireViewByIdCompat(
            viewBindingRootId
        ) else this
    }
}

// ------------------------------------------------- With base class ------------------------------------------------

/**
 * Creates an instance of the binding class for the activity to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(activity: ComponentActivity): VB =
    reflexViewBinding(activity.javaClass, activity.layoutInflater)

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(fragment: Fragment, container: ViewGroup?): VB =
    reflexViewBinding(fragment.javaClass, fragment.layoutInflater, container)

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(fragment: Fragment): VB =
    reflexViewBinding(fragment.javaClass, fragment.requireView())

/**
 * Creates an instance of the binding class for the activity to use.
 *
 * @param aClass current class.
 * @param from get from [ComponentActivity.getLayoutInflater].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the activity to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(
    aClass: Class<*>, from: LayoutInflater
): VB {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val tClass: Class<*> = try {
                    actualTypeArgument as Class<*>
                } catch (e: Exception) {
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflate = tClass.getMethod("inflate", LayoutInflater::class.java)
                    return cast(inflate.invoke(null, from))
                }
            }
        }
        reflexViewBinding(aClass.superclass, from)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param aClass current class.
 * @param from get from [Fragment.getLayoutInflater].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the fragment to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater,
    container: ViewGroup?
): VB {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val tClass: Class<*> = try {
                    actualTypeArgument as Class<*>
                } catch (e: Exception) {
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflate = tClass.getMethod(
                        "inflate",
                        LayoutInflater::class.java,
                        ViewGroup::class.java,
                        Boolean::class.java
                    )
                    return cast(inflate.invoke(null, from, container, false))
                }
            }
        }
        reflexViewBinding(aClass.superclass, from, container)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param aClass current class.
 * @param view get from [Fragment.requireView].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the fragment to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(aClass: Class<*>, view: View): VB {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val tClass: Class<*> = try {
                    actualTypeArgument as Class<*>
                } catch (e: Exception) {
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflate = tClass.getMethod("bind", View::class.java)
                    return cast(inflate.invoke(null, view))
                }
            }
        }
        reflexViewBinding(aClass.superclass, view)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}