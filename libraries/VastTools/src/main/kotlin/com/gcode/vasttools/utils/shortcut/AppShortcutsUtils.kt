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

package com.gcode.vasttools.utils.shortcut

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.gcode.vasttools.bean.Component2
import com.gcode.vasttools.helper.ContextHelper
import com.gcode.vasttools.utils.AppUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/19 15:37
// Description: 
// Documentation:

/**
 * Help you to create short cut launcher for app.
 *
 * @since 0.0.9
 */
object AppShortcutsUtils : ShortCutInterface {

    private var shortCutListener: (() -> Intent?)? = null

    private var shortCutResult: (() -> PendingIntent?)? = null

    /**
     * Creating a pinned shortcut for your app.
     *
     * @param shortcutName shortcut name.
     * @param shortcutIcon icon resource id for the shortcut.
     * @since 0.0.9
     */
    @RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
    @JvmOverloads
    fun createPinnedShortcut(
        shortcutName: String? = AppUtils.getAppName(),
        shortcutIcon: Int? = null,
        context: Context? = null
    ) {
        val mContext = context ?: ContextHelper.getAppContext()
        val intent = Intent().apply {
            action = "com.android.launcher.action.INSTALL_SHORTCUT"
            putExtra(Intent.ACTION_CREATE_SHORTCUT, shortcutName)
            val bitmap = if (null != shortcutIcon) {
                BitmapFactory.decodeResource(mContext.resources, shortcutIcon)
            } else {
                AppUtils.getAppBitmap()
            }
            putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap)
            putExtra(Intent.EXTRA_SHORTCUT_INTENT, onPinShortCutClick())
        }
        mContext.sendBroadcast(intent)
    }

    /**
     * Creating a pinned shortcut for your app.
     *
     * @param shortcutActivity the activity that will be launched by shortcut.
     * @param shortcutId only id for shortcut.
     * @param shortcutIcon icon resource id for the shortcut.
     * @param shortcutName shortcut name.
     * @since 0.0.9
     */
    @RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun createPinnedShortcut(
        shortcutActivity: Class<out ComponentActivity>,
        shortcutId: String,
        shortcutIcon: Int,
        shortcutName: String,
        context: Context? = null
    ) {
        val (mContext, shortcutManager) = getContextAndShortcutManager(context)
        if (shortcutManager.isRequestPinShortcutSupported) {
            val shortcutInfo = createShortcutInfo(
                shortcutActivity,
                shortcutId,
                shortcutIcon,
                shortcutName,
                mContext
            )
            shortcutManager.requestPinShortcut(shortcutInfo, onPinShortCutSuccess()?.intentSender)
        }
    }

    /**
     * Publish the list of shortcuts. All existing dynamic shortcuts
     * from the caller app will be replaced. If there are already pinned
     * shortcuts with the same IDs, the mutable pinned shortcuts are updated.
     *
     * @see ShortcutManager.setDynamicShortcuts
     * @since 0.0.9
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun setDynamicShortcuts(shortcutInfo: List<ShortcutInfo>, context: Context? = null) {
        val (_, shortcutManager) = getContextAndShortcutManager(context)
        shortcutManager.dynamicShortcuts = shortcutInfo
    }

    /**
     * Publish the list of dynamic shortcuts. If there are already dynamic or
     * pinned shortcuts with the same IDs, each mutable shortcut is updated.
     *
     * @see ShortcutManager.addDynamicShortcuts
     * @since 0.0.9
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun addDynamicShortcuts(shortcutInfo: List<ShortcutInfo>, context: Context? = null) {
        val (_, shortcutManager) = getContextAndShortcutManager(context)
        shortcutManager.addDynamicShortcuts(shortcutInfo)
    }

    /**
     * Delete all dynamic shortcuts from the caller app.
     *
     * @since 0.0.9
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun removeAllDynamicShortcuts(context: Context? = null) {
        val (_, shortcutManager) = getContextAndShortcutManager(context)
        shortcutManager.removeAllDynamicShortcuts()
    }

    /**
     * Delete dynamic shortcuts by ID.
     *
     * @since 0.0.9
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun removeDynamicShortcuts(shortcutIds: List<String>, context: Context? = null) {
        val (_, shortcutManager) = getContextAndShortcutManager(context)
        shortcutManager.removeDynamicShortcuts(shortcutIds)
    }

    /**
     * Create a [ShortcutInfo].
     *
     * @param shortcutActivity the activity that will be launched by shortcut.
     * @param shortcutId only id for shortcut.
     * @param shortcutIcon icon resource id for the shortcut.
     * @param shortcutName shortcut name.
     * @since 0.0.9
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmOverloads
    fun createShortcutInfo(
        shortcutActivity: Class<out ComponentActivity>,
        shortcutId: String,
        shortcutIcon: Int,
        shortcutName: String,
        context: Context? = null
    ): ShortcutInfo {
        val shortcutIntent =
            Intent(context, shortcutActivity).apply {
                action = Intent.ACTION_VIEW
            }
        return ShortcutInfo.Builder(context, shortcutId)
            .setIcon(Icon.createWithResource(context, shortcutIcon))
            .setShortLabel(shortcutName)
            .setIntent(shortcutIntent)
            .build()
    }

    fun registerPinnedShortCutListener(l: () -> Intent) = apply {
        shortCutListener = l
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setPinnedShortcutResult(l: () -> PendingIntent) = apply {
        shortCutResult = l
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getContextAndShortcutManager(context: Context?): Component2<Context, ShortcutManager> {
        val mContext = context ?: ContextHelper.getAppContext()
        val shortcutManager =
            mContext.getSystemService(ShortcutManager::class.java)
        return Component2(mContext, shortcutManager)
    }

    override fun onPinShortCutClick(): Intent? {
        return shortCutListener?.invoke()
    }

    override fun onPinShortCutSuccess(): PendingIntent? {
        return shortCutResult?.invoke()
    }
}