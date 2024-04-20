package edu.studio.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class WeatherIntegrationTest {

    @Test
    public void testFromMain_Valid() {
        String[] args = {};
        String userInput = String.format("12\n09\n", System.lineSeparator(), System.lineSeparator(),
                System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        ByteArrayInputStream bais = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(bais);

        String expected = "Attention!!! Migrane Warning!!!";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        WeatherTrendDriver.main(args); // call the main method
        String[] lines = baos.toString().split(System.lineSeparator());
        String actual = lines[lines.length - 1];

        assertEquals(true, actual.startsWith(expected));    
    }
}
