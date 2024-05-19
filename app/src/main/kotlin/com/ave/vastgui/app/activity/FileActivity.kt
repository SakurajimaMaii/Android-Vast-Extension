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
import android.view.MotionEvent
import android.widget.EditText
import com.ave.vastgui.app.databinding.ActivityFileBinding
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.bean.UserBean
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.view.extension.hideKeyBroad
import com.ave.vastgui.tools.view.extension.isShouldHideKeyBroad
import com.log.vastgui.core.base.LogLevel

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

class FileActivity : VastVbActivity<ActivityFileBinding>() {

    private val mLogger = mLogFactory.getLog(FileActivity::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val length = FileMgr.appInternalFilesDir().length()
        getBinding().button.setOnClickListener {
            mLogger.i("苏州园林里都有假山和池沼。假山的堆叠，可以说是一项艺术而不仅是技术。或者是重峦叠嶂，或者是几座小山配合着竹子花木，全在乎设计者和匠师们生平多阅历，胸中有邱壑，才能使游览者攀登的时候忘却苏州城市，只觉得身在山间。至于池沼，大多引用活水。有些园林池沼宽敞，就把池沼作为全园的中心，其他景物配合着布置。水面假如成河道模样，往往安排桥梁。假如安排两座以上的桥梁，那就一座一个样，决不雷同。池沼或河道的边沿很少砌齐整的石岸，总是高低屈曲任其自然。还在那儿布置几块玲珑的石头，或者种些花草：这也是为了取得从各个角度看都成一幅画的效果。池沼里养着金鱼或各色鲤鱼，夏秋季节荷花或睡莲开放，游览者看“鱼戏莲叶间”，又是入画的一景。 ")
            mLogger.d("这是一条日志")
            mLogger.json(LogLevel.WARN, UserBean("小明", "123456789"))
//            getBinding().editView.hideKeyBroad()
//            ContextCompat.getDrawable(getContext(), R.drawable.ic_github)?.toBitmap()?.apply {
//                val bmpString = BmpUtils.getBase64FromBitmap(this)
//                mLogger.d(bmpString ?: "")
//            }
//            mLogger.json(LogLevel.INFO, UserBean("小明", "123456"))
//            thread(name = "测试线程1") {
//                repeat(100) {
//                    val bitmap = ResourcesCompat
//                        .getDrawable(resources, R.drawable.ic_github, theme)!!.toBitmap()
//                    val bmpString = BmpUtils.getBase64FromBitmap(bitmap)
//                    mLogger.d(bmpString ?: "")
//                }
//            }
//            thread(name = "测试线程2") {
//                repeat(100) {
//                    mLogger.json(LogLevel.INFO, UserBean("小明", "123456"))
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