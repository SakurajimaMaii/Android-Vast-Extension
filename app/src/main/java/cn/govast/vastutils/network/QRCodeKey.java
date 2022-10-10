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

package cn.govast.vastutils.network;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

import cn.govast.vasttools.network.base.BaseApiRsp;

public class QRCodeKey implements BaseApiRsp {

    private DataDTO data;
    private Integer code;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public static class DataDTO {
        private Integer code;
        private String unikey;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getUnikey() {
            return unikey;
        }

        public void setUnikey(String unikey) {
            this.unikey = unikey;
        }
    }

}