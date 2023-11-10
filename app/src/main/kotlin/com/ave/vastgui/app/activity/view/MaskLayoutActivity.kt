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

package com.ave.vastgui.app.activity.view

import android.app.UiModeManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.adapter.widget.AdapterLongClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.ImageAdapter
import com.ave.vastgui.app.adapter.entity.ImageWrapper
import com.ave.vastgui.app.adapter.entity.StudentWrapper
import com.ave.vastgui.app.databinding.ActivityMaskLayoutBinding
import com.ave.vastgui.app.sharedpreferences.SpEncryptedExample
import com.ave.vastgui.app.theme.AppTheme
import com.ave.vastgui.app.viewmodel.ThemeVM
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.annotation.ExperimentalView
import com.ave.vastgui.tools.theme.darkColorScheme
import com.ave.vastgui.tools.theme.lightColorScheme
import com.ave.vastgui.tools.view.masklayout.MaskAnimation
import com.ave.vastgui.tools.view.masklayout.MaskLayout
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25

/**
 * Mask Layout Activity.
 *
 * The following settings are required:
 * ```xml
 * <activity
 *     ...
 *     android:configChanges="uiMode">
 * ...
 * ```
 * This is to ensure that the Activity will not be restarted when [UiModeManager.setApplicationNightMode]
 * or [AppCompatDelegate.setDefaultNightMode] takes effect.
 */
class MaskLayoutActivity : AppCompatActivity(R.layout.activity_mask_layout) {

    private val uiManager: UiModeManager by lazy {
        getSystemService(UI_MODE_SERVICE) as UiModeManager
    }
    private val mViewModel: ThemeVM by viewModels<ThemeVM>()
    private val mBinding: ActivityMaskLayoutBinding by viewBinding(ActivityMaskLayoutBinding::bind)
    private var mAdapter by NotNUllVar<ImageAdapter>()
    private val mData: MutableList<AdapterItemWrapper<*>> = ArrayList<AdapterItemWrapper<*>>().apply {
        repeat(10) {
            add(ImageWrapper(R.drawable.img_astronaut))
            add(StudentWrapper("学生$it", 10 + it))
        }
    }

    private val mClick = object : AdapterClickListener {
        override fun onItemClick(view: View, pos: Int) {
        }
    }

    private val mLongClick = object : AdapterLongClickListener {
        override fun onItemLongClick(view: View, pos: Int): Boolean {
            return true
        }
    }

    @androidx.annotation.OptIn(ExperimentalView::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.lifecycleOwner = this

        SpEncryptedExample.isDark = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            currentNightMode == Configuration.UI_MODE_NIGHT_YES
        } else {
            getDefaultNightMode() == MODE_NIGHT_YES
        }
        if (SpEncryptedExample.isDark){
            AppTheme.update(darkColorScheme)
        } else {
            AppTheme.update(lightColorScheme)
        }

        mBinding.maskLayout.updateCoordinate(mBinding.changeDark)

        mAdapter = ImageAdapter(mData, this).apply {
            registerClickEvent(mClick)
            registerLongClickEvent(mLongClick)
        }
        mBinding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MaskLayoutActivity)
        }

        mBinding.changeDark.setOnClickListener {
            mBinding.maskLayout.activeMask(MaskAnimation.EXPANDED,
                object : MaskLayout.MaskAnimationListener {
                    override fun onMaskComplete() {
                        mViewModel.changeTheme()
                    }

                    override fun onMaskFinished() {
                        if (SpEncryptedExample.isDark) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                            } else {
                                setDefaultNightMode(MODE_NIGHT_YES)
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                uiManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                            } else {
                                setDefaultNightMode(MODE_NIGHT_NO)
                            }
                        }
                    }
                }
            )
        }
    }

}