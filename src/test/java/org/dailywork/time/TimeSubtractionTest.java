package org.dailywork.time;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeSubtractionTest {

    @Test
    public void subtractingOneSecond() {
        Time threeSeconds = Time.seconds(3);
        Time twoSeconds = Time.seconds(2);
        assertEquals(twoSeconds, threeSeconds.minusOneSecond());
    }


}
