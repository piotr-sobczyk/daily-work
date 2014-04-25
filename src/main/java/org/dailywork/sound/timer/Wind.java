/*
 * Copyright (c) 2010-2012 Célio Cidral Junior.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.dailywork.sound.timer;

import org.dailywork.config.Options.SoundConfig;
import org.dailywork.sound.SoundSupport;

public class Wind extends SoundSupport {

    @Override
    protected SoundConfig configuration() {
        return options().sound().wind();
    }

    @Override
    protected String defaultSoundResource() {
        return "/crank.wav";
    }

}
