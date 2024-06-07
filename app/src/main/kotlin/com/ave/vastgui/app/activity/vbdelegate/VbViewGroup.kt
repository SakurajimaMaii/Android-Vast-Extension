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

class ViewGroupGetViewBindingByDelegate @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    // 使用方式 1
    private val mBinding by viewBinding(ViewgroupVbBinding::bind)
    // 使用方式 2
    // root 是 ViewgroupVbBinding 的根布局 id
    // private val mBinding by viewBinding(ViewgroupVbBinding::bind, R.id.root)

    init {
        inflate(context, R.layout.viewgroup_vb, this)
        orientation = VERTICAL
    }

    fun setFirstName(value: String) {
        mBinding.firstName.text = value
    }

    fun setLastName(value: String) {
        mBinding.lastName.text = value
    }

}