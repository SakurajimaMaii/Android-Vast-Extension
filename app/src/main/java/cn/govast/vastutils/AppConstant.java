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

package cn.govast.vastutils;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/15
// Description: 
// Documentation:

import java.io.File;

import cn.govast.vasttools.utils.FileUtils;

public class AppConstant {

    // 应用内部文件夹File文件夹路径
    public static final String APP_ROOT_PATH = FileUtils.appInternalFilesDir().getPath();

    // 皮肤包名和路径
    public static final String THEME_NAME = "darkskin-debug.apk";
    public static final String THEME_PATH = APP_ROOT_PATH + File.separator + THEME_NAME;

}
