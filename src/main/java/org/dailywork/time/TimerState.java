package org.dailywork.time;

class TimerState {

    private Time time;
    private boolean ended;

    public TimerState(Time time) {
        this.time = time;
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

    public void setTime(Time time) {
        this.time = time;
    }

    public void decreaseOneSecond() {
        time = time.minusOneSecond();
    }

}
