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

package cn.govast.vasttools.manager.filemgr

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/25
// Description: 
// Documentation:
// Reference:

/**
 * File operations result.
 *
 * @property FLAG_SUCCESS means running successful.
 * @property FLAG_PARENT_NOT_EXISTS means the parent of the file is not
 * exist.
 * @property FLAG_EXISTS means the file or directory is exist.
 * @property FLAG_NOT_EXISTS means the file or directory is not exist.
 * @property FLAG_FAILED means running failed.
 */
enum class FileResult {
    FLAG_SUCCESS,
    FLAG_PARENT_NOT_EXISTS,
    FLAG_EXISTS,
    FLAG_NOT_EXISTS,
    FLAG_FAILED
}