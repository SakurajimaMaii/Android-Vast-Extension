/*
 * Copyright 2021-2024 VastGui
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
 *
 */

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/9/21
// Description: 
// Documentation: 
// Reference:

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.ave.vastgui.tools.manager.filemgr.FileMgr;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class FileMgrTest {

    @Test
    public void saveFile() {
        final Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final File txt = new File(appContext.getFilesDir(), "test.txt");
        assertFalse(txt.isDirectory());
        final File dir = FileMgr
                .getOrMakeDir(new File(appContext.getFilesDir(), "test"))
                .getOrNull();
        assertTrue(dir.isDirectory());
    }

}
