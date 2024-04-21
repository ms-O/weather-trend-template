package edu.studio.weather.Models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ResultTest {
    @Test
    public void testConstructorAndGetters() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(startTime, endTime, pressureDrop);

        assertEquals(startTime, result.getMigraineStartTime());
        assertEquals(endTime, result.getMigraineEndTime());
        assertEquals(pressureDrop, result.getPressureDrop());
    }

    @Test
    public void testSetters() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(null, null, 0);

        result.setMigraineStartTime(startTime);
        result.setMigraineEndTime(endTime);
        result.setPressureDrop(pressureDrop);

        assertEquals(startTime, result.getMigraineStartTime());
        assertEquals(endTime, result.getMigraineEndTime());
        assertEquals(pressureDrop, result.getPressureDrop());
    }

    @Test
    public void testEquals() {
        LocalDateTime startTime1 = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime startTime2 = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result1 = new Result(startTime1, endTime, pressureDrop);
        Result result2 = new Result(startTime2, endTime, pressureDrop);

        assertTrue(result1.equals(result2) && result2.equals(result1));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(startTime, endTime, pressureDrop);
        Object obj = new Object(); // Object of different class

        assertFalse(result.equals(obj));
    }

    @Test
    public void testEqualsWithSameObject() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(startTime, endTime, pressureDrop);
        Object obj = result;

        assertTrue(result.equals(obj));
    }

    @Test
    public void testEqualsWithNullObject() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(startTime, endTime, pressureDrop);
        Object obj = null;

        assertFalse(result.equals(obj));
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentInstances() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result1 = new Result(startTime, endTime, pressureDrop);
        Result result2 = new Result(startTime, endTime, pressureDrop);

        assertFalse(result1 == result2);
        assertTrue(result1.equals(result2) && result2.equals(result1));
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testToString() {
        LocalDateTime startTime = LocalDateTime.of(2022, 4, 19, 10, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 4, 19, 11, 30);
        double pressureDrop = 5.6;

        Result result = new Result(startTime, endTime, pressureDrop);
        String expectedString = "Potential migraine episode from 2022-04-19T10:30 to 2022-04-19T11:30 | change : 5.6 hPa";

        assertEquals(expectedString, result.toString());
    }
}
