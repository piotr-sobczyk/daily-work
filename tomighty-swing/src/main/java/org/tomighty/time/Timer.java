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

package org.tomighty.time;

import java.util.TimerTask;

import javax.inject.Inject;

import org.tomighty.Phase;
import org.tomighty.bus.Bus;
import org.tomighty.bus.messages.timer.TimerFinished;
import org.tomighty.bus.messages.timer.TimerInterrupted;
import org.tomighty.bus.messages.timer.TimerStarted;
import org.tomighty.bus.messages.timer.TimerTick;

public class Timer {

    private static final int ONE_SECOND = 1000;

    private TimerState state;
    private java.util.Timer timer;
    private boolean isScheduled;

    private final Bus bus;

    @Inject
    public Timer(Bus bus) {
        this.bus = bus;
    }

    public Time getTime() {
        return state.getTime();
    }

    public void setTime(Time time) {
        state = new TimerState(time, Phase.BURST);
    }

    public boolean isInProgress() {
        return state != null && !state.isEnded();
    }

    public void start(Time initialTime, Phase phase) {
        state = new TimerState(initialTime, phase);
        scheduleTimer();

        bus.publish(new TimerStarted(initialTime, phase));
    }

    private void finish() {
        cancelTimer();
        state.markEnded();

        bus.publish(new TimerFinished(state.getPhase()));
    }

    public void interrupt() {
        cancelTimer();
        state.markEnded();

        bus.publish(new TimerInterrupted(state.getTime(), state.getPhase()));
    }

    public void pause() {
        cancelTimer();
    }

    public void resume() {
        scheduleTimer();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isScheduled = false;
    }

    private void scheduleTimer() {
        if (isScheduled) {
            throw new IllegalStateException("Trying to start a timer that is already active");
        }

        timer = new java.util.Timer(getClass().getSimpleName());
        timer.scheduleAtFixedRate(new Tick(), ONE_SECOND, ONE_SECOND);
        isScheduled = true;
    }

    private void tick() {
        state.decreaseOneSecond();

        bus.publish(new TimerTick(state.getTime(), state.getPhase()));

        if(state.getTime().isZero())
            finish();
    }

    private class Tick extends TimerTask {
        @Override
        public void run() {
            tick();
        }
    }

}

