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

package org.dialywork.ui.state.breaks;

import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.dialywork.bus.messages.ui.ChangeUiState;
import org.dialywork.config.Options;
import org.dialywork.time.Time;
import org.dialywork.ui.UiState;
import org.dialywork.ui.state.TimerSupport;
import org.dialywork.ui.theme.Colors;
import org.dialywork.ui.theme.colors.Green;

public class Break extends TimerSupport {

    @Inject
    private Options options;

    @Override
    public Colors colors() {
        return Green.instance();
    }

    @Override
    protected String title() {
        return messages.get("Break");
    }

    @Override
    protected Time initialTime() {
        int minutes = options.time().breakTime();
        return new Time(minutes);
    }

    @Override
    protected Action[] primaryActions() {
        return new Action[] {
                new Interrupt()
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return new Action[] { };
    }

    @Override
    protected Class<? extends UiState> finishedState() {
        return BreakFinished.class;
    }

    @Override
    protected Class<? extends UiState> interruptedState() {
        return BreakInterrupted.class;
    }

    @SuppressWarnings("serial")
    private class Interrupt extends AbstractAction {
        public Interrupt() {
            super(messages.get("Interrupt"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.interrupt();
            bus.publish(new ChangeUiState(interruptedState()));
        }
    }

}
