package com.ave.vastgui.tools.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.ave.vastgui.tools.content.ContextHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * ```kotlin
 * internal object KVTest : SharedPreferencesOwner("test") {
 *     private var i1 by spInt()
 *     private var i2 by spInt(default = -1)
 *     private var l1 by spLong()
 *     private var l2 by spLong(default = -1L)
 *     private var b1 by spBoolean()
 *     private var b2 by spBoolean(default = true)
 *     private var f1 by spFloat()
 *     private var f2 by spFloat(default = -1f)
 *     private var s1 by spString()
 *     private var s2 by spString(default = "")
 *     private var set1 by spStringSet(default = setOf(""))
 *     private var set2 by spStringSet()
 * }
 * ```
 */
interface ISharedPreferencesOwner {
    val name: String
    val kv: SharedPreferences
    fun clearAllKV() = kv.clearAll()
}

open class SharedPreferencesOwner(override val name: String) : ISharedPreferencesOwner {
    override val kv: SharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        ContextHelper.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}

fun ISharedPreferencesOwner.spString(default: String = ""): SharedPreferencesProperty<String> = SharedPreferencesProperty(
    { kv.getString(it, default) ?: default },
    { kv.edit { putString(first, second) } }
)

fun ISharedPreferencesOwner.spString(): SharedPreferencesProperty<String?> = SharedPreferencesProperty(
    { kv.getString(it, null) },
    { kv.edit { putString(first, second) } }
)

fun ISharedPreferencesOwner.spStringSet(default: Set<*> = setOf<String>()): SharedPreferencesProperty<Set<*>> = SharedPreferencesProperty(
    { kv.getStringSet(it, mutableSetOf()) ?: default },
    { kv.edit { putStringSet(first, second.map { it.toString() }.toMutableSet()) } })

fun ISharedPreferencesOwner.spStringSet(): SharedPreferencesProperty<Set<*>?> = SharedPreferencesProperty(
    { kv.getStringSet(it, null) },
    { kv.edit { putStringSet(first, second?.map { it.toString() }?.toMutableSet()) } })

fun ISharedPreferencesOwner.spInt(default: Int = 0) = SharedPreferencesProperty(
    { kv.getInt(it, default) },
    { kv.edit { putInt(first, second) } })

fun ISharedPreferencesOwner.spLong(default: Long = 0) = SharedPreferencesProperty(
    { kv.getLong(it, default) },
    { kv.edit { putLong(first, second) } })

fun ISharedPreferencesOwner.spFloat(default: Float = 0f) = SharedPreferencesProperty(
    { kv.getFloat(it, default) },
    { kv.edit { putFloat(first, second) } })

fun ISharedPreferencesOwner.spBoolean(default: Boolean = false) = SharedPreferencesProperty(
    { kv.getBoolean(it, default) },
    { kv.edit { putBoolean(first, second) } })

class SharedPreferencesProperty<V>(
    private val get: (String) -> V,
    private val put: Pair<String, V>.() -> Unit,
) : ReadWriteProperty<ISharedPreferencesOwner, V> {
    override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): V = get(property.name)
    override fun setValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>, value: V) {
        put(property.name to value)
    }
}

fun <V> SharedPreferencesProperty<V>.asLiveData() = object : ReadOnlyProperty<ISharedPreferencesOwner, MutableLiveData<V>> {
    private var cache: MutableLiveData<V>? = null

    override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): MutableLiveData<V> =
        cache ?: object : MutableLiveData<V>() {
            override fun getValue() = this@asLiveData.getValue(thisRef, property)

            override fun setValue(value: V) {
                if (super.getValue() == value) return
                this@asLiveData.setValue(thisRef, property, value)
                super.setValue(value)
            }

            override fun onActive() = super.setValue(value)
        }.also { cache = it }
}

fun <V> SharedPreferencesProperty<V>.asStateFlow() = object : ReadOnlyProperty<ISharedPreferencesOwner, MutableStateFlow<V>> {
    private lateinit var cache: MutableStateFlow<V>
    override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): MutableStateFlow<V> =
        if (::cache.isInitialized) cache else object : MutableStateFlow<V> {
            override val replayCache: List<V>
                get() = listOf(value)
            override val subscriptionCount: StateFlow<Int>
                get() = cache.subscriptionCount

            override var value: V
                get() = this@asStateFlow.getValue(thisRef, property)
                set(value) {
                    this@asStateFlow.setValue(thisRef, property, value)
                    cache.updateAndGet { value }
                }

            override suspend fun collect(collector: FlowCollector<V>): Nothing {
                cache.collect(collector)
            }

            override fun compareAndSet(expect: V, update: V): Boolean {
                return true
            }

            @ExperimentalCoroutinesApi
            override fun resetReplayCache() {
                throw UnsupportedOperationException("MutableStateFlow.resetReplayCache is not supported")
            }

            override fun tryEmit(value: V): Boolean {
                this.value = value
                return true
            }

            override suspend fun emit(value: V) {
                this.value = value
            }
        }.also { cache = it }
}
