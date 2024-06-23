/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.core

import com.log.vastgui.core.base.LogInfoFactory
import com.log.vastgui.core.pipeline.Pipeline
import com.log.vastgui.core.pipeline.PipelinePhase

// Author: ywnkm
// Email: https://github.com/ywnkm
// Date: 2024/6/22

/**
 * Log pipeline.
 *
 * @since 1.3.4
 */
class LogPipeline :
    Pipeline<LogInfoFactory, LogCat>(Before, State, Transform, Render, Output) {
    companion object {
        /** @since 1.3.4 */
        val Before: PipelinePhase = PipelinePhase("Before")

        /** @since 1.3.4 */
        val State: PipelinePhase = PipelinePhase("State")

        /** @since 1.3.4 */
        val Transform: PipelinePhase = PipelinePhase("Transform")

        /** @since 1.3.4 */
        val Render: PipelinePhase = PipelinePhase("Render")

        /** @since 1.3.4 */
        val Output: PipelinePhase = PipelinePhase("Output")
    }
}