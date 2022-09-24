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
@file:Suppress("DEPRECATION")

package cn.govast.vastskin

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.view.View
import cn.govast.vastskin.VastSkinManager.loadSkin
import cn.govast.vastskin.utils.VastSkinResources
import java.io.File
import java.util.*

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/27 18:35
// Description: VastSkin is a non-intrusive skinning framework based on the replacement LayoutInflater.Factory2
// Documentation: [VastSkin](https://sakurajimamaii.github.io/VastDocs/document/en/VastSkin.html)


/**
 * VastSkinManager
 *
 * Using [loadSkin] to load the skin
 * ```kotlin
 * VastSkinManager.loadSkin("data/data/com.gcode.vastutils/files/darkskin-debug.apk")
 * ```
 * If you want to load the default skin ,just do this
 * ```kotlin
 * VastSkinManager.loadSkin(null)
 * ```
 *
 * @since 0.0.1
 */
object VastSkinManager : Observable() {

    /**
     * The application of your app.[originalApplication]
     * will be initialized in [initVastThemeManager].
     *
     * @since 0.0.1
     */
    private lateinit var originalApplication:Application

    /**
     * The log tag of the [VastSkinManager].
     *
     * @since 0.0.1
     */
    private const val tag:String = "VastSkinManager"

    private lateinit var skinActivityLifecycle: VastSkinActivityLifecycle

    /**
     * The font change listener.
     *
     * @since 0.0.1
     */
    var fontChangeListener:FontChangeListener? = null
        private set

    /**
     * Initialization the [VastSkinManager].
     *
     * If [originalApplication] and [skinActivityLifecycle] is initialized,
     * when you call [initVastThemeManager],it will do nothing.
     */
    @JvmStatic
    fun initVastThemeManager(application: Application){
        if(!this::originalApplication.isInitialized and !this::skinActivityLifecycle.isInitialized){
            originalApplication = application
            VastSkinResources.initSkinResources(originalApplication)
            VastSkinSharedPreferences.initSkinSharedPreferences(originalApplication)
            // Register the original application as Observer.
            skinActivityLifecycle = VastSkinActivityLifecycle(this)
            originalApplication.registerActivityLifecycleCallbacks(skinActivityLifecycle)
            // Load the skin setting in the last time.
            loadSkin(VastSkinSharedPreferences.skin)
        }
    }

    /**
     * Loading the skin.
     *
     * @param skinPath skin path,if empty will use default skin.
     * @param skinFileListener register a listener of the skin file.
     * @since 0.0.1
     */
    @JvmStatic
    @JvmOverloads
    fun loadSkin(skinPath: String,skinFileListener: SkinFileListener? = null) {
        if(!File(skinPath).exists()){
            skinFileListener?.fileNoExists()
            return
        }
        try {
            // The resources of application.
            val appResource = originalApplication.resources
            // Create AssetManager and Resource by reflection.
            val assetManager = AssetManager::class.java.newInstance()
            // Resource path setting directory or zip.
            val addAssetPath = assetManager.javaClass.getMethod(
                "addAssetPath",
                String::class.java
            )
            addAssetPath.invoke(assetManager, skinPath)
            // Create Resources based on the current device display information
            // and configuration (horizontal and vertical screen, language, etc.)
            val themeResource =
                Resources(assetManager, appResource.displayMetrics, appResource.configuration)
            //Get theme package
            val mPm = originalApplication.packageManager
            val info = mPm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            val packageName = info!!.packageName
            VastSkinResources.update(themeResource, packageName)
            // Save the skin file path.
            VastSkinSharedPreferences.skin = skinPath
        } catch (e: Exception) {
            skinFileListener?.fileLoadError(e)
            e.printStackTrace()
        }
        setChanged()
        notifyObservers(this)
    }

    /**
     * Loading the default skin.
     *
     * @since 0.0.1
     */
    @JvmStatic
    fun resetSkin(){
        VastSkinSharedPreferences.reset()
        VastSkinResources.reset()
        setChanged()
        notifyObservers(this)
    }

    /**
     * Register a font change listener.
     *
     * @since 0.0.1
     */
    fun registerFontChangeListener(fontChangeListener: FontChangeListener?){
        this.fontChangeListener = fontChangeListener
    }

    /**
     * A listener of skin file.
     *
     * @since 0.0.1
     */
    interface SkinFileListener{

        fun fileNoExists()

        fun fileLoadError(e:Exception)

    }

    /**
     * A listener of font
     *
     * @since 0.0.1
     */
    interface FontChangeListener{

        /**
         * Define the font change.
         *
         * @param mContext context.
         * @param view the view need to change.
         * @since 0.0.1
         */
        fun changeFont(mContext: Context, view: View)

    }

}