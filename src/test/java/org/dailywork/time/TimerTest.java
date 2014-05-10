package org.dailywork.time;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.dailywork.bus.timer.TimerFinished;
import org.dailywork.bus.timer.TimerStarted;
import org.dailywork.bus.timer.TimerTick;
import org.dailywork.mock.bus.MockBus;
import org.junit.Before;
import org.junit.Test;

public class TimerTest {

    private MockBus bus;
    private Timer timer;

    @Before
    public void setUp() throws Exception {
        bus = new MockBus();
        timer = new Timer(bus);
    }

    @Test(timeout = 5000)
    public void startTimerAndWaitToFinish() {
        timer.start(Time.seconds(3));

        List<Object> messages = bus.waitUntilNumberOfMessagesReach(5);

        assertEquals("Amount of published messages", 5, messages.size());
        assertEquals("First message", TimerStarted.class, messages.get(0).getClass());
        assertEquals("Second message", TimerTick.class, messages.get(1).getClass());
        assertEquals("Third message", TimerTick.class, messages.get(2).getClass());
        assertEquals("Fourth message", TimerTick.class, messages.get(3).getClass());
        assertEquals("Fifth message", TimerFinished.class, messages.get(4).getClass());

        TimerStarted timerStarted = (TimerStarted) messages.get(0);
        assertEquals("Initial time", Time.seconds(3), timerStarted.getTime());

        TimerTick firstTick = (TimerTick) messages.get(1);
        assertEquals("First tick's time", Time.seconds(2), firstTick.getTime());

        TimerTick secondTick = (TimerTick) messages.get(2);
        assertEquals("Second tick's time", Time.seconds(1), secondTick.getTime());

        TimerTick thirdTick = (TimerTick) messages.get(3);
        assertEquals("Third tick's time", Time.seconds(0), thirdTick.getTime());

        TimerFinished timerFinished = (TimerFinished) messages.get(4);
    }

}
