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

package org.dialywork.ui.theme;

import javax.inject.Inject;

import org.dialywork.bus.Bus;
import org.dialywork.bus.Subscriber;
import org.dialywork.bus.messages.ui.LookChanged;
import org.dialywork.bus.messages.ui.UiStateChanged;
import org.dialywork.config.Options;
import org.dialywork.ui.UiState;
import org.dialywork.ui.theme.colors.Gray;

public class Look implements Subscriber<UiStateChanged> {

    @Inject
    private Bus bus;
    @Inject
    private Options options;
    private Colors currentColors = defaultColors();

    @Inject
    public Look(Bus bus) {
        bus.subscribe(this, UiStateChanged.class);
    }

    public Theme theme() {
        return options.ui().theme();
    }

    public Colors colors() {
        return currentColors;
    }

    @Override
    public void receive(UiStateChanged message) {
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
        bus.publish(new LookChanged());
    }

    private static Colors defaultColors() {
        return Gray.instance();
    }

}
