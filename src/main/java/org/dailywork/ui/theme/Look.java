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

package org.dailywork.ui.theme;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.ui.LookChanged;
import org.dailywork.bus.messages.ui.UiStateChanged;
import org.dailywork.config.Options;
import org.dailywork.ui.UiState;
import org.dailywork.ui.theme.colors.Gray;

public class Look {

    @Inject
    private Options options;

    private EventBus bus;

    private Colors currentColors = defaultColors();

    @Inject
    public Look(EventBus bus) {
        this.bus = bus;

        bus.register(this);
    }

    public Theme theme() {
        return options.ui().theme();
    }

    public Colors colors() {
        return currentColors;
    }

    @Subscribe
    public void changeUiState(UiStateChanged message) {
        UiState uiState = message.uiState();
        Colors colors = uiState.colors();
        if (colors == null) {
            colors = defaultColors();
        }
        changeColors(colors);
    }

    private void changeColors(Colors colors) {
        if (currentColors.equals(colors)) {
            return;
        }
        currentColors = colors;
        bus.post(new LookChanged());
    }

    private static Colors defaultColors() {
        return Gray.instance();
    }

}
