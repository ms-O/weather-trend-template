package edu.studio.weather;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeatherTrendDriverTest {

    @Test
    public void testMain() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        WeatherTrendDriver.main(null);
        // Verified if the printed output contains expected strings
        String output = outputStreamCaptor.toString().trim().replaceAll("\\s+", " ");
        String expectedOutput = "Alert!, Pressure changes that may trigger a headache are predicted for the following time ranges: 2024-04-03T12:00 to 2024-04-03T18:00 change 6.0 hPa";
        // Splitting both actual and expected outputs into lines
        String[] actualLines = output.split("\\r?\\n");
        String[] expectedLines = expectedOutput.split("\\r?\\n");
        
        assertTrue(Arrays.asList(actualLines).containsAll(Arrays.asList(expectedLines)));
    }
}

