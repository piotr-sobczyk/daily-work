package org.tomighty.time;

import org.tomighty.Phase;

class TimerState {

    private Time time;
    private final Phase phase;
    private boolean ended;

    public TimerState(Time time, Phase phase) {
        this.time = time;
        this.phase = phase;
    }

    public void markEnded() {
        ended = true;
    }

    public boolean isEnded() {
        return ended;
    }

    public Time getTime() {
        return time;
    }

    public Phase getPhase() {
        return phase;
    }

    public void decreaseOneSecond() {
        time = time.minusOneSecond();
    }

}
