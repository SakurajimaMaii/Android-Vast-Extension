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

package com.ave.vastgui.tools.text

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Hashtable
import java.util.Locale

// Author: lt
// Email: lt.dygzs@qq.com
// Date: 2022/3/10 18:32
// Description: Validates the Chinese ID card number.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/id-regex/

object IDRegex {
    /**
     * Verify the correctness of the Chinese resident ID card number.
     *
     * @param str Chinese resident ID card number string.
     * @return Validation results,"" indicates that the verification is
     *     successful.
     */
    @JvmStatic
    fun validateIDCardNumber(str: String): String {
        var errorInfo: String
        val valCodeArr = arrayOf(
            "1", "0", "x", "9", "8", "7", "6", "5", "4",
            "3", "2"
        )
        val wi = arrayOf(
            "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
            "9", "10", "5", "8", "4", "2"
        )

        if (str.length != 15 && str.length != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。"
            return errorInfo
        }

        var ai: String = if (str.length == 18) {
            str.substring(0, 17)
        } else {
            str.substring(0, 6) + "19" + str.substring(6, 15)
        }

        if (!ai.isNumeric()) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。"
            return errorInfo
        }

        val strYear = ai.substring(6, 10) // year
        val strMonth = ai.substring(10, 12) // month
        val strDay = ai.substring(12, 14) // month
        if (!"$strYear-$strMonth-$strDay".isDate()) {
            errorInfo = "身份证生日无效。"
            return errorInfo
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            if (gc[Calendar.YEAR] - strYear.toInt() > 150
                || gc.time.time - s.parse("$strYear-$strMonth-$strDay")!!.time < 0
            ) {
                errorInfo = "身份证生日不在有效范围。"
                return errorInfo
            }
        } catch (e: NumberFormatException) {
            Log.e("IDCardUtils", e.message.toString())
        } catch (e: ParseException) {
            Log.e("IDCardUtils", e.message.toString())
        }
        if (strMonth.toInt() > 12 || strMonth.toInt() == 0) {
            errorInfo = "身份证月份无效。"
            return errorInfo
        }
        if (strDay.toInt() > 31 || strDay.toInt() == 0) {
            errorInfo = "身份证日期无效。"
            return errorInfo
        }

        val h: Hashtable<String, String> = getAreaCode()
        if (h[ai.substring(0, 2)] == null) {
            errorInfo = "身份证地区编码错误。"
            return errorInfo
        }

        var totalmulAiWi = 0
        for (i in 0..16) {
            totalmulAiWi = (totalmulAiWi
                    + ai[i].toString().toInt() * wi[i].toInt())
        }
        val modValue = totalmulAiWi % 11
        val strVerifyCode = valCodeArr[modValue]
        ai += strVerifyCode
        if (str.length == 18) {
            if (ai != str) {
                errorInfo = "身份证无效，不是合法的身份证号码"
                return errorInfo
            }
        } else {
            return ""
        }

        return ""
    }

    /** Get area code */
    private fun getAreaCode(): Hashtable<String, String> {
        val hashtable = Hashtable<String, String>()
        hashtable["11"] = "北京"
        hashtable["12"] = "天津"
        hashtable["13"] = "河北"
        hashtable["14"] = "山西"
        hashtable["15"] = "内蒙古"
        hashtable["21"] = "辽宁"
        hashtable["22"] = "吉林"
        hashtable["23"] = "黑龙江"
        hashtable["31"] = "上海"
        hashtable["32"] = "江苏"
        hashtable["33"] = "浙江"
        hashtable["34"] = "安徽"
        hashtable["35"] = "福建"
        hashtable["36"] = "江西"
        hashtable["37"] = "山东"
        hashtable["41"] = "河南"
        hashtable["42"] = "湖北"
        hashtable["43"] = "湖南"
        hashtable["44"] = "广东"
        hashtable["45"] = "广西"
        hashtable["46"] = "海南"
        hashtable["50"] = "重庆"
        hashtable["51"] = "四川"
        hashtable["52"] = "贵州"
        hashtable["53"] = "云南"
        hashtable["54"] = "西藏"
        hashtable["61"] = "陕西"
        hashtable["62"] = "甘肃"
        hashtable["63"] = "青海"
        hashtable["64"] = "宁夏"
        hashtable["65"] = "新疆"
        hashtable["71"] = "台湾"
        hashtable["81"] = "香港"
        hashtable["82"] = "澳门"
        hashtable["91"] = "国外"
        return hashtable
    }

    /** @return true if the string is a date,false otherwise. */
    private fun String.isDate(): Boolean {
        return Regex("^((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9])))))?\$").matches(
            this
        )
    }

}