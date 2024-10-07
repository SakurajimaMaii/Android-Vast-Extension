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
 */

import com.log.vastgui.slf4j.Slf4jProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class SLF4JTest {

    public static void main(String[] args) {
        Slf4jProvider.Options.setFactory(FactoryKt.getLogFactory());
        Logger logger = LoggerFactory.getLogger(SLF4JTest.class);
        logger.info(MarkerFactory.getMarker("Hello"), "testLogger");
        logger.debug(MarkerFactory.getMarker("Hello"),"testLogger");
    }

}