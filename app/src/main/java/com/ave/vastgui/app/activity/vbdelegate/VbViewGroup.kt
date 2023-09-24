package com.ave.vastgui.app.activity.vbdelegate

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ViewgroupVbBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#viewgroup

class VbViewGroup1 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding by viewBinding(ViewgroupVbBinding::bind)

    init {
        inflate(context, R.layout.viewgroup_vb, this)
        orientation = VERTICAL
    }

    fun setFirstName(value: String) {
        binding.firstName.text = value
    }

    fun setLastName(value: String) {
        binding.lastName.text = value
    }

}

class VbViewGroup2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding by viewBinding(ViewgroupVbBinding::bind, R.id.item_textview_root)

    init {
        inflate(context, R.layout.viewgroup_vb, this)
        orientation = VERTICAL
    }

    fun setFirstName(value: String) {
        binding.firstName.text = value
    }

    fun setLastName(value: String) {
        binding.lastName.text = value
    }

}