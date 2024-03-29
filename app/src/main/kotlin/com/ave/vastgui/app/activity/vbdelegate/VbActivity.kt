package com.ave.vastgui.app.activity.vbdelegate

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityVbBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#activity

class VbActivity1 : ComponentActivity() {

    private val mBinding by viewBinding(ActivityVbBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

class VbActivity2 : Activity() {

    private val viewBindingProperty by lazy {
        viewBinding(ActivityVbBinding::inflate)
    }
    private val mBinding by viewBindingProperty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter2(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBindingProperty.clear()
    }

}

class VbActivity3 : ComponentActivity(R.layout.activity_vb) {

    private val mBinding by viewBinding(ActivityVbBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

class VbActivity4 : ComponentActivity(R.layout.activity_vb) {

    private val mBinding by viewBinding(ActivityVbBinding::bind, R.id.activity_my_root)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.personRv.adapter = VbAdapter(persons)
        mBinding.personRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}