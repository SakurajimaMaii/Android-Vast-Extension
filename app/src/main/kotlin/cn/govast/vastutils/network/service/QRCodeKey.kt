package cn.govast.vastutils.network.service

import cn.govast.vasttools.network.base.BaseApiRsp

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/24
// Description: 
// Documentation:

data class QRCodeKey(
    val code: Int,
    val data: Data?
): BaseApiRsp {

    data class Data(
        val code: Int,
        val unikey: String
    )

    override fun isSuccess(): Boolean {
        return code == 200
    }

    override fun isEmpty(): Boolean {
        return data == null
    }

}