package org.dailywork.bus.timer;

import org.dailywork.time.Time;

public class TimerStarted extends TimerEvent {

    public TimerStarted(Time time) {
        super(time);
    }

}
