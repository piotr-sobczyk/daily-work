package org.dialywork.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class LessThanOneMinuteTimeTest {

    private Time time;

    @Before
    public void setUp() throws Exception {
        time = Time.seconds(9);
    }

    @Test
    public void isNotZero() {
        assertFalse(time.isZero());
    }

    @Test
    public void hasZeroMinutes() {
        assertEquals(0, time.minutes());
    }

    @Test
    public void hasNineSeconds() {
        assertEquals(9, time.seconds());
    }

    @Test
    public void shortestStringRepresentation() {
        assertEquals("9", time.shortestString());
    }

    @Test
    public void standardStringRepresentation() {
        assertEquals("00:09", time.toString());
    }

}
