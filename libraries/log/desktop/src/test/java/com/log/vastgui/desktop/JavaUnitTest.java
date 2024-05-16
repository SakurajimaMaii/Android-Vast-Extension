/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.log.vastgui.desktop;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/15 11:44

import static com.log.vastgui.desktop.LoggerKt.mLogFactory;

import com.log.vastgui.core.LogUtil;
import com.sun.tools.javac.Main;

import org.junit.Test;

public class JavaUnitTest {

    private static final LogUtil mLogger = mLogFactory.getLog(Main.class);

    @Test
    public void logTest() {
        mLogger.v("这是一条测试日志");
        mLogger.d("这是一条测试日志");
        mLogger.i("这是一条测试日志");
        mLogger.w("这是一条测试日志");
        mLogger.e("这是一条测试日志");
    }

}