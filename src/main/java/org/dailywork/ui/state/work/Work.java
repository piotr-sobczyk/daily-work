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

package org.dailywork.ui.state.work;

import java.awt.event.ActionEvent;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.config.Options;
import org.dailywork.time.Time;
import org.dailywork.ui.UiState;
import org.dailywork.ui.state.TimerSupport;
import org.dailywork.ui.theme.Colors;
import org.dailywork.ui.theme.colors.Red;

public class Work extends TimerSupport {

    @Inject
    private Options options;

    @Override
    public Colors colors() {
        return Red.instance();
    }

    @Override
    protected String title() {
        return messages.get("Work");
    }

    @Override
    protected Time initialTime() {
        int minutes = options.time().pomodoro();
        return new Time(0, minutes);
    }

    @Override
    protected boolean displaysGauge() {
        return false;
    }

    @Override
    protected Class<? extends UiState> finishedState() {
        return WorkFinished.class;
    }

    @Override
    protected Action[] primaryActions() {
        return new Action[] {
                new Pause()
        };
    }

    @Override
    protected Action[] secondaryActions() {
        return new Action[] {
        };
    }

    @SuppressWarnings("serial")
    private class Pause extends AbstractAction {
        public Pause() {
            super("Pause");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            timer.pause();
            bus.publish(new ChangeUiState(WorkPaused.class));
        }
    }

}
