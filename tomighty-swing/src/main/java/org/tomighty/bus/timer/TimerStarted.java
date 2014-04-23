package org.tomighty.bus.timer;

import org.tomighty.time.Time;

public class TimerStarted extends TimerEvent {

    public TimerStarted(Time time) {
        super(time);
    }

}
