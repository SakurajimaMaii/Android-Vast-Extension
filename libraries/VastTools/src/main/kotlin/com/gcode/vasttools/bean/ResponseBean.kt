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

package com.gcode.vasttools.bean

//Author: Vast Gui
//Email: guihy2019@gmail.com
//Date: 2022/3/17 12:21
//Documentation: [Bean](https://sakurajimamaii.github.io/VastDocs/document/en/Bean.html)

/**
 * Base results for network requests.
 *
 * @param T Type of the result data.
 * @property code Result code.
 * @property msg Result message.
 * @property data Result data.
 *
 * @since 0.0.5
 */
open class ResponseBean<T:Any> (val code:Int, val msg:String, val data:T)