package org.dialywork.bus.timer;

import org.dialywork.time.Time;

public class TimerStarted extends TimerEvent {

    public TimerStarted(Time time) {
        super(time);
    }

}
