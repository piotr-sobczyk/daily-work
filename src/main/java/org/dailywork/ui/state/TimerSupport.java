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

package org.dailywork.ui.state;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JLabel;

import com.google.common.eventbus.Subscribe;
import org.dailywork.bus.messages.ui.ChangeUiState;
import org.dailywork.bus.timer.TimerFinished;
import org.dailywork.bus.timer.TimerTick;
import org.dailywork.sound.SoundPlayer;
import org.dailywork.sound.Sounds;
import org.dailywork.time.Time;
import org.dailywork.time.Timer;
import org.dailywork.ui.UiState;

public abstract class TimerSupport extends UiStateSupport {

    @Inject
    protected Timer timer;
    @Inject
    private Sounds sounds;
    @Inject
    private SoundPlayer soundPlayer;
    private JLabel remainingTime;

    protected abstract Time initialTime();

    protected abstract Class<? extends UiState> finishedState();

    @PostConstruct
    public void initialize() {
        eventBus.register(this);
    }

    @Override
    protected Component createContent() {
        remainingTime = labelFactory.big();
        return remainingTime;
    }

    @Override
    public void afterRendering() {
        if (timer.isInProgress()) {
            timer.resume();
            remainingTime.setText(timer.getTime().toString());
        } else {
            Time time = initialTime();
            timer.start(time);
            remainingTime.setText(time.toString());
        }

        soundPlayer.play(sounds.wind()).playRepeatedly(sounds.tictac());
    }

    @Override
    public void beforeDetaching() {
        soundPlayer.stop(sounds.tictac());
        eventBus.unregister(this);
    }

    @Subscribe
    public void updateTime(TimerTick tick) {
        final Time time = tick.getTime();
        invokeLater(new Runnable() {
            @Override
            public void run() {
                remainingTime.setText(time.toString());
            }
        });
    }

    @Subscribe
    public void endTimer(TimerFinished end) {
        soundPlayer.play(sounds.ding());
        eventBus.post(new ChangeUiState(finishedState()));
    }

}
