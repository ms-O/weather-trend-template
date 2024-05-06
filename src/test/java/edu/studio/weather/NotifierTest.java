package edu.studio.weather;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class NotifierTest {
    
    @Test
    public void testPrintResults() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        OpenMeteoRestClient openrest= new OpenMeteoRestClient();
        OpenMeteoParser parser = new OpenMeteoParser();
        List<Forecast> forecasts = parser.parseData(openrest.fetchWeatherData());
        ForecastAnalyzer analyzer = new ForecastAnalyzer(forecasts);
        List<Result> results = analyzer.analyze();      
        Notifier.printResults(results);
        String expectedOutput = "Alert!, Pressure changes that may trigger a headache are predicted for the following time ranges:\n" +
                "2024-04-03T12:00 to 2024-04-03T18:00 change 6.0 hPa\n";
        assertLinesMatch(expectedOutput.lines(), outputStreamCaptor.toString().lines());
    }

   @Test
    public void testPrintResults2() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        List<Result> results = new ArrayList<>();
        results.add(new Result("2024-04-03T12:00", "2024-04-03T18:00", 6.0));
        Notifier.printResults(results);
        String expectedOutput = "Alert!, Pressure changes that may trigger a headache are predicted for the following time ranges:\n" +
                "2024-04-03T12:00 to 2024-04-03T18:00 change 6.0 hPa\n";
        assertLinesMatch(expectedOutput.lines(), outputStreamCaptor.toString().lines());
    }
}



