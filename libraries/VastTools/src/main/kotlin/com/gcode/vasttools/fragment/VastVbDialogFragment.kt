package com.gcode.vasttools.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.gcode.vasttools.extension.reflexViewBinding


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/7 12:06
// Description: 
// Documentation:

abstract class VastVbDialogFragment<VB : ViewBinding> : VastDialogFragment() {

    /**
     * The viewBinding of the fragment, it will
     * be initialized in [Fragment.onCreateView].
     *
     * @since 0.0.6
     */
    protected lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = reflexViewBinding(javaClass,layoutInflater)
        return mBinding.root
    }

    final override fun setVmBySelf(): Boolean = false

    final override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

}