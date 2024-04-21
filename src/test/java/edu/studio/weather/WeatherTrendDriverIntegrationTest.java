package edu.studio.weather;

import edu.studio.weather.Mocks.WeatherTrendDriverMock;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherTrendDriverIntegrationTest {

    @Test
    public void testWeatherTrendDriverIntegration() {
        // Redirect System.out to capture program output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        WeatherTrendDriverMock.main(new String[] {});

        // program output
        String programOutput = outputStream.toString().trim();

        System.setOut(System.out);

        // excpected output
        // expected output
        String expectedOutput = "---Migrane Alerts---\r\n" +
                "Potential migraine episode from 2024-04-11T11:00 to 2024-04-11T12:00 | change : 5.5 hPa\r\n" +
                "Potential migraine episode from 2024-04-11T13:00 to 2024-04-11T14:00 | change : 5.1 hPa\r\n" +
                "Potential migraine episode from 2024-04-11T15:00 to 2024-04-11T16:00 | change : 5.1 hPa";

        System.out.println("Program Output: " + programOutput);
        System.out.println("Expected Output: " + expectedOutput);

        // Assert
        assertEquals(expectedOutput, programOutput);
    }
}
