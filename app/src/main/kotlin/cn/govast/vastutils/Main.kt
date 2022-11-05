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

package cn.govast.vastutils

import cn.govast.vasttools.extension.AutoEnumValue
import cn.govast.vasttools.extension.AutoField
import cn.govast.vasttools.extension.getKeyAndValue

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/5
// Description: 
// Documentation:
// Reference:

enum class Gender{
    @AutoEnumValue("m") MAN,WOMAN
}

fun main(){

    data class Person(
        @AutoField("fn") val firstName: String,
        @AutoField("ln") val lastName:String,
        @AutoField("g") val gender: Gender)

    val person = Person("张","三",Gender.MAN)
    val map = person.getKeyAndValue()
    map.forEach { (t, u) ->
        println("$t $u")
    }
}