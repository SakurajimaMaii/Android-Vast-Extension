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

package com.ave.vastgui.app.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.view.extension.hideKeyBroad
import com.ave.vastgui.tools.view.extension.isShouldHideKeyBroad

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    private val mLogger = mLogFactory.getLog(FileActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().button.setOnClickListener {
            getBinding().editView.hideKeyBroad()
            mLogger.d(
                "中华人民共和国是一个位于东亚的社会主义国家[1]，首都为北京市[17]，领土东至黑龙江省抚远市的黑瞎子岛中部，西达新疆克孜勒苏境内的帕米尔高原，南抵海南省三沙市的南海海域，北及黑龙江省大兴安岭地区的黑龙江航道，国土面积约为960万平方千米。全国共划分为23个省[注 15]、5个自治区、4个直辖市和2个特别行政区，是世界上总面积第三或第四大的国家（仅陆地面积为世界第二）[注 16][18]。" +
                        "第二次世界大战结束后，毛泽东领导下的中国共产党和中国人民解放军在第二次国共内战中逐步取得优势，于1949年10月1日在北京宣布成立中华人民共和国中央人民政府，并实际控制中国大陆地区，与迁至台湾地区的中华民国政府形成至今的分治格局。[19]" +
                        "截至2023年末，中华人民共和国有约14.1亿人[注 17][20]，约占世界人口的17.6%[注 18]，其也是一个多民族国家，官方承认的民族共有56个，其中汉族占总人口的91.51%[22]。在悠久的历史发展中，中华人民共和国逐渐形成了多元一体的中华文化格局[23]。国家通用语言和文字是普通话和规范汉字，在民族自治地方少数民族可以使用民族语言和文字。自1986年起中华人民共和国实行九年义务教育制度，截至2020年接受高等教育人口达2.4亿[24]。" +
                        "中华人民共和国目前为世界第二大经济体和世界上经济最发达的发展中国家，2023年国内生产总值（GDP）总量达126万亿人民币，依国际汇率折合17.52万亿美元，位居世界第二，仅次于美国；按购买力平价则位列世界第一[25][26]。中华人民共和国是世界上最大的商品出口国及第二大的进口国[26]。1978年改革开放后，中华人民共和国很快成为经济增长最快的主要经济体之一[27][28]。贫困问题随着经济增长也逐渐得到好转，832个国家级贫困县在2020年底全部完成脱贫摘帽[29]，但区域间发展不均衡以及国民贫富差距较大这两大问题仍需解决[30][31]。"
            )
            Log.INFO
//            thread(name = "测试线程1") {
//                repeat(5) {
//                    val bitmap = ResourcesCompat
//                        .getDrawable(resources, R.drawable.ic_github, theme)!!.toBitmap()
//
//                }
//            }
//            thread(name = "测试线程2") {
//                repeat(5) {
//                    val bitmap = ResourcesCompat
//                        .getDrawable(resources, R.drawable.ic_github, theme)!!.toBitmap()
//                    mLogger.d(BmpUtils.getBase64FromBitmap(bitmap).toString())
//                }
//            }
        }
        // 保存文件
//        val save = saveFile(File(appInternalFilesDir().path, "save.txt"))
//        logger.d("文件保存结果 $save")
        // 移动文件
//        val move = moveFile(File(appInternalFilesDir().path, "save.txt"), appInternalCacheDir().path)
//        logger.d("文件移动结果 $move")
        // 创建文件夹
//        val saveDir = makeDir(File(appInternalFilesDir().path, "newDir"))
//        logger.d("文件夹创建结果 $saveDir")
        // 复制文件夹
//        val copyDir = copyDir(File(appInternalFilesDir().path), File(appInternalFilesDir().path,"newDir2"))
//        logger.d("文件夹复制结果 $copyDir")
        // 删除文件
//        val delete = FileMgr.deleteFile(File(appInternalFilesDir().path, "save.txt"))
//        logger.d(getDefaultTag(), "文件删除结果 $delete")
        // 重命名文件
//        val rename = FileMgr.rename(File(appInternalFilesDir().path, "save.txt"), "newname.txt")
//        logger.d( "文件重命名结果 $rename")
        // 删除文件夹
//        val deleteDir = deleteDir(File(appInternalFilesDir().path, "newDir"))
//        logger.d("文件夹删除结果 $deleteDir")
        // 复制文件
//        val copyFile = copyFile(
//            File(appInternalFilesDir().path, "save.txt"),
//            File(appInternalFilesDir().path, "save_copy.txt")
//        )
//        logger.d("文件复制结果 $copyFile")
        // 移动文件夹
//        val moveDir = moveDir(
//            File(appInternalFilesDir().path),
//            "${appInternalFilesDir().path}${File.separator}moveDir"
//        )
//        logger.d("文件夹移动结果 $moveDir")
//        saveBitmap()
    }

//    private fun saveBitmap() {
//        val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_github, this)
//        getBinding().image.setImageBitmap(bitmap)
//        BmpUtils.saveBitmapAsFile(bitmap, File(appInternalFilesDir(), "bitmap.jpg"))?.apply {
//            logger.d("图像${name}保存成功")
//        }
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (MotionEvent.ACTION_DOWN == event?.action) {
            val view = currentFocus
            if (null != view && view is EditText) {
                if (view.isShouldHideKeyBroad(event)) {
                    view.hideKeyBroad()
                }
            }
        }
        return super.onTouchEvent(event)
    }

}