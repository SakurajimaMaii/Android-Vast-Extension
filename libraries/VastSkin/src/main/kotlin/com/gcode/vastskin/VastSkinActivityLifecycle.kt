/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastskin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.gcode.vastskin.utils.VastSkinUtils
import java.util.*

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/27 19:45

internal class VastSkinActivityLifecycle constructor(private val mObservable: Observable) :
    Application.ActivityLifecycleCallbacks {
    private val mLayoutInflaterFactories: ArrayMap<Activity, VastSkinLayoutInflaterFactory> =
        ArrayMap()

    @SuppressLint("DiscouragedPrivateApi")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        VastSkinUtils.updateStatusBarColor(activity)
        val layoutInflater = activity.layoutInflater

        val skinLayoutInflaterFactory: VastSkinLayoutInflaterFactory
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            try {
                // Set the mFactorySet with false
                val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
                field.isAccessible = true
                field.setBoolean(layoutInflater, false)
                skinLayoutInflaterFactory = VastSkinLayoutInflaterFactory(activity)
                LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
                mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory
                mObservable.addObserver(skinLayoutInflaterFactory)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            try {
                skinLayoutInflaterFactory = forceSetFactory2(layoutInflater,activity)
                mLayoutInflaterFactories[activity] = skinLayoutInflaterFactory
                mObservable.addObserver(skinLayoutInflaterFactory)
            }catch (e:IllegalAccessException){
                e.printStackTrace()
            }catch (e:NoSuchFieldException){
                e.printStackTrace()
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        val observer: VastSkinLayoutInflaterFactory? = mLayoutInflaterFactories.remove(activity)
        VastSkinManager.deleteObserver(observer)
    }

    // See https://blog.csdn.net/qq_25412055/article/details/100033637
    @SuppressLint("DiscouragedPrivateApi")
    @Throws(IllegalAccessException::class,NoSuchFieldException::class)
    private fun forceSetFactory2(
        inflater: LayoutInflater,
        activity: Activity
    ): VastSkinLayoutInflaterFactory {
        val compatClass = LayoutInflaterCompat::class.java
        val inflaterClass = LayoutInflater::class.java
        val sCheckedField = compatClass.getDeclaredField("sCheckedField")
        sCheckedField.isAccessible = true
        sCheckedField.setBoolean(inflater, false)
        val mFactory = inflaterClass.getDeclaredField("mFactory")
        mFactory.isAccessible = true
        val mFactory2 = inflaterClass.getDeclaredField("mFactory2")
        mFactory2.isAccessible = true
        val factory = VastSkinLayoutInflaterFactory(activity)
        mFactory2[inflater] = factory
        mFactory[inflater] = factory
        return factory
    }

}