package org.dailywork.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class TimeImmutabilityTest {

    @Test
    public void subtractingOneSecond() {
        Time originalTime = Time.seconds(9);
        Time resultingTime = originalTime.minusOneSecond();

        assertNotSame("Returns new instance of Time", resultingTime, originalTime);
        assertEquals("Original time did not change", 9, originalTime.seconds());
    }


}
