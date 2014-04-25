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

package org.dailywork.sound;

import javax.inject.Inject;

import org.dailywork.sound.timer.Ding;
import org.dailywork.sound.timer.TicTac;
import org.dailywork.sound.timer.Wind;

public class Sounds {

    @Inject
    private Wind wind;
    @Inject
    private TicTac ticTac;
    @Inject
    private Ding ding;

    public Sound wind() {
        return wind;
    }

    public Sound tictac() {
        return ticTac;
    }

    public Sound ding() {
        return ding;
    }

}
