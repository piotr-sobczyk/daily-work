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

package org.dialywork.ui.state;

import static javax.swing.SwingUtilities.invokeLater;

import java.awt.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JLabel;

import org.dialywork.bus.Subscriber;
import org.dialywork.bus.messages.ui.ChangeUiState;
import org.dialywork.bus.timer.TimerFinished;
import org.dialywork.bus.timer.TimerTick;
import org.dialywork.sound.SoundPlayer;
import org.dialywork.sound.Sounds;
import org.dialywork.time.Time;
import org.dialywork.time.Timer;
import org.dialywork.ui.UiState;

public abstract class TimerSupport extends UiStateSupport {

    @Inject
    protected Timer timer;
    @Inject
    private Sounds sounds;
    @Inject
    private SoundPlayer soundPlayer;
    private JLabel remainingTime;
    private UpdateTime updateTime = new UpdateTime();
    private EndTimer endTimer = new EndTimer();

    protected abstract Time initialTime();

    protected abstract Class<? extends UiState> finishedState();

    protected abstract Class<? extends UiState> interruptedState();

    @PostConstruct
    public void initialize() {
        bus.subscribe(updateTime, TimerTick.class);
        bus.subscribe(endTimer, TimerFinished.class);
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
        bus.unsubscribe(updateTime, TimerTick.class);
        bus.unsubscribe(endTimer, TimerFinished.class);
    }

    private class UpdateTime implements Subscriber<TimerTick> {
        @Override
        public void receive(TimerTick tick) {
            final Time time = tick.getTime();
            invokeLater(new Runnable() {
                @Override
                public void run() {
                    remainingTime.setText(time.toString());
                }
            });
        }
    }

    private class EndTimer implements Subscriber<TimerFinished> {
        @Override
        public void receive(TimerFinished end) {
            soundPlayer.play(sounds.ding());
            bus.publish(new ChangeUiState(finishedState()));
        }
    }

}
