package org.dailywork.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class OneMinuteTimeTest {

    private Time time;

    @Before
    public void setUp() throws Exception {
        time = Time.minutes(1);
    }

    @Test
    public void isNotZero() {
        assertFalse(time.isZero());
    }

    @Test
    public void hasOneMinute() {
        assertEquals(1, time.minutes());
    }

    @Test
    public void hasZeroSeconds() {
        assertEquals(0, time.seconds());
    }

    @Test
    public void shortestStringRepresentation() {
        assertEquals("1", time.shortestString());
    }

    @Test
    public void standardStringRepresentation() {
        assertEquals("01:00", time.toString());
    }

}
