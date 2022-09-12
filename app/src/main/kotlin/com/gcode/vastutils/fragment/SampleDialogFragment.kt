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

package com.gcode.vastutils.fragment

import android.os.Bundle
import android.view.View
import com.gcode.vasttools.fragment.VastVbVmDialogFragment
import com.gcode.vasttools.utils.UnitStrUtils
import com.gcode.vastutils.databinding.FragmentSampleDialogBinding
import com.gcode.vastutils.viewModel.BasicViewModel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/6 21:02
// Description: 
// Documentation:

class SampleDialogFragment : VastVbVmDialogFragment<FragmentSampleDialogBinding, BasicViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}