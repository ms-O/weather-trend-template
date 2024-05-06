package edu.studio.weather;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ForecastAnalyzerTest {
    
    @Test
    public void testAnalyze() {
        OpenMeteoRestClient openrest= new OpenMeteoRestClient();
        OpenMeteoParser parser = new OpenMeteoParser();
        List<Forecast> forecasts = parser.parseData(openrest.fetchWeatherData());
        ForecastAnalyzer analyzer = new ForecastAnalyzer(forecasts);
        List<Result> results = analyzer.analyze();
        // Expected number of results based on provided data
        int expectedResultsSize = 1;
        assertEquals(expectedResultsSize, results.size());
    }

   @Test
    public void testAnalyze2() {
        List<Forecast> forecasts = new ArrayList<>();
        forecasts.add(new Forecast("00:00", 1000.0)); 
        forecasts.add(new Forecast("01:00", 1001.0));
        forecasts.add(new Forecast("02:00", 1002.0));
        forecasts.add(new Forecast("03:00", 1003.0));
        forecasts.add(new Forecast("04:00", 1004.0));
        forecasts.add(new Forecast("05:00", 1009.0)); 
        forecasts.add(new Forecast("06:00", 1015.0)); 
        forecasts.add(new Forecast("07:00", 1016.0));
        forecasts.add(new Forecast("08:00", 1017.0));
        forecasts.add(new Forecast("09:00", 1022.0)); 
        forecasts.add(new Forecast("10:00", 1026.0)); 
        forecasts.add(new Forecast("11:00", 1030.0));
        forecasts.add(new Forecast("12:00", 1025.0));
        forecasts.add(new Forecast("13:00", 1015.0)); 
        forecasts.add(new Forecast("14:00", 1016.0));
        forecasts.add(new Forecast("15:00", 1017.0));
        forecasts.add(new Forecast("16:00", 1022.0)); 
        forecasts.add(new Forecast("17:00", 1026.0)); 
        forecasts.add(new Forecast("18:00", 1027.0)); 
        ForecastAnalyzer analyzer = new ForecastAnalyzer(forecasts);
        List<Result> results = analyzer.analyze();
        assertEquals(2, results.size()); 
        
       
    } 
}
