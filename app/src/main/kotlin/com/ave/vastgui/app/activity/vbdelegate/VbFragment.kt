package com.ave.vastgui.app.activity.vbdelegate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.FragmentSampleBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#fragment

class VbFragment1 : Fragment(R.layout.fragment_sample) {

    private val mBinding by viewBinding(FragmentSampleBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.text.setOnClickListener {
            VbFragment2().show(childFragmentManager, null)
        }
    }

}

class VbFragment2 : DialogFragment(R.layout.fragment_sample) {

    val mBinding by viewBinding(FragmentSampleBinding::bind, R.id.fragment_sample_root)

}