/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastskin.model

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/30 20:00
// Description: VastSkinPair is used to store the name and id of the attr that can be changed.

/**
 * [VastSkinPair] is used to store the name and resource
 * id of the attribute that can be changed.
 *
 * @property attributeName attribute name.
 * @property resourceId resource id of the attribute.
 *
 * @since 0.0.1
 */
class VastSkinPair(internal var attributeName: String,internal var resourceId: Int)