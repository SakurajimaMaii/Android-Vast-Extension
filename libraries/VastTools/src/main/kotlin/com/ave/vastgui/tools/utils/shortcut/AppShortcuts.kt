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

package com.ave.vastgui.tools.utils.shortcut

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.utils.AppUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/19 15:37
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/AppShortcuts/

/**
 * Creating a pinned shortcut for your app.
 *
 * @param shortcutId Only id for shortcut.
 * @param shortcutName The default value of the [AppUtils.getAppName].
 * @param shortcutIcon The default value of the [AppUtils.getAppBitmap].
 * @param shortcutResultIntent
 * @throws IllegalArgumentException
 * @since 0.5.1
 */
@RequiresPermission(Manifest.permission.INSTALL_SHORTCUT)
@RequiresApi(Build.VERSION_CODES.O)
@Throws(IllegalArgumentException::class)
fun createPinnedShortcut(
    shortcutId: String,
    shortcutName: String? = null,
    shortcutIcon: Bitmap? = null,
    shortcutResultIntent: ((Intent) -> PendingIntent)? = null,
    shortcutContext: Context? = null,
    shortcutIntent: () -> Intent
) {
    val context = shortcutContext ?: ContextHelper.getAppContext()
    if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
        val intent = shortcutIntent()
        if (intent.action == null) {
            throw IllegalArgumentException("You must set a action for the Intent.")
        }
        val shortcutInfo = ShortcutInfoCompat.Builder(context, shortcutId)
            .setIcon(IconCompat.createWithBitmap(AppUtils.getAppBitmap(shortcutIcon)))
            .setShortLabel(AppUtils.getAppName(shortcutName))
            .setIntent(shortcutIntent())
            .build()
        val pinnedShortcutCallbackIntent =
            ShortcutManagerCompat.createShortcutResultIntent(context, shortcutInfo)
        ShortcutManagerCompat.requestPinShortcut(
            context, shortcutInfo,
            shortcutResultIntent?.invoke(pinnedShortcutCallbackIntent)?.intentSender
        )
    }
}

/**
 * Publish the list of shortcuts. All existing dynamic shortcuts
 * from the caller app will be replaced. If there are already pinned
 * shortcuts with the same IDs, the mutable pinned shortcuts are updated.
 *
 * @see ShortcutManagerCompat.setDynamicShortcuts
 * @since 0.5.1
 */
fun setDynamicShortcuts(shortcutInfoCompat: List<ShortcutInfoCompat>, context: Context? = null) {
    val mContext = getContext(context)
    ShortcutManagerCompat.setDynamicShortcuts(mContext, shortcutInfoCompat)
}

/**
 * Publish the list of dynamic shortcuts. If there are already dynamic or
 * pinned shortcuts with the same IDs, each mutable shortcut is updated.
 *
 * @see ShortcutManagerCompat.addDynamicShortcuts
 * @since 0.5.1
 */
fun addDynamicShortcuts(shortcutInfoCompat: List<ShortcutInfoCompat>, context: Context? = null) {
    val mContext = getContext(context)
    ShortcutManagerCompat.addDynamicShortcuts(mContext, shortcutInfoCompat)
}

/**
 * Delete all dynamic shortcuts from the caller app.
 *
 * @since 0.5.1
 */
fun removeAllDynamicShortcuts(context: Context? = null) {
    val mContext = getContext(context)
    ShortcutManagerCompat.removeAllDynamicShortcuts(mContext)
}

/**
 * Delete dynamic shortcuts by ID.
 *
 * @since 0.5.1
 */
fun removeDynamicShortcuts(shortcutIds: List<String>, context: Context? = null) {
    val mContext = getContext(context)
    ShortcutManagerCompat.removeDynamicShortcuts(mContext, shortcutIds)
}

/** @since 0.5.1 */
private fun getContext(context: Context?): Context {
    return context ?: ContextHelper.getAppContext()
}