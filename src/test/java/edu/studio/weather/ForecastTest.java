package edu.studio.weather;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;


@RunWith(MockitoJUnitRunner.class)
public class ForecastTest {

    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    @Mock
    private HttpResponse<JsonNode> mockResponse;

    @Test
    public void testGetWeatherData_Success() throws Exception {

        HttpResponse<JsonNode> response = WeatherTrendDriver.getWeatherData(40.03705685765183, -75.34258923444702);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getObject().has("latitude"));
    }


    @Test
    public void testGetWeatherData_Failure() throws Exception {

        HttpResponse<JsonNode> response = WeatherTrendDriver.getWeatherData(100.0, -200.0);

        assertEquals(400, response.getStatus());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getObject().has("error"));
    }
    
    
    @Test
    public void testCalculateMigranePeriods() {

        HourlyData mockHourlyData = mock(HourlyData.class);
        
        List<String> mockTimes = Arrays.asList("2024-04-20T12:00:00", "2024-04-20T18:00:00", "2024-04-21T00:00:00");
        List<Double> mockPressures = Arrays.asList(1010.0, 1005.0, 1000.0);

        when(mockHourlyData.getTime()).thenReturn(mockTimes);
        when(mockHourlyData.getPressure_msl()).thenReturn(mockPressures);
        
        ForecastAnalyzer forecastAnalyzer = new ForecastAnalyzer();
        

        List<List<String>> result = forecastAnalyzer.calculateMigranePeriods(mockHourlyData);

        assertEquals(0, result.size()); // We expect 0 trigger period
    }
    
    
    @Test
    public void testCalculateMigranePeriodsPrecise() {
        HourlyData mockHourlyData = mock(HourlyData.class);

        List<String> mockTimes = Arrays.asList(
                "2024-04-23T05:00",
                "2024-04-23T06:00",
                "2024-04-23T07:00",
                "2024-04-23T08:00",
                "2024-04-23T09:00",
                "2024-04-23T10:00",
                "2024-04-23T11:00",
                "2024-04-23T12:00",
                "2024-04-23T13:00",
                "2024-04-23T14:00");
        List<Double> mockPressures = Arrays.asList(      
                1013.6,
                1013.5,
                1013.5,
                1008.0,
                1011.1,
                1013.0,
                1013.6,
                1014.8,
                1019.7);
        
        when(mockHourlyData.getTime()).thenReturn(mockTimes);
        when(mockHourlyData.getPressure_msl()).thenReturn(mockPressures);
        
        ForecastAnalyzer forecastAnalyzer = new ForecastAnalyzer();
        
        List<List<String>> result = forecastAnalyzer.calculateMigranePeriodsPrecise(mockHourlyData);
        
        assertEquals(3, result.size());
        
        List<String> triggerEvent1 = result.get(0);
        assertEquals("2024-04-23T05:00", triggerEvent1.get(0));
        assertEquals("2024-04-23T08:00", triggerEvent1.get(1));
        assertEquals("5.60", triggerEvent1.get(2));
   
        List<String> triggerEvent3 = result.get(2);
        assertEquals("2024-04-23T07:00", triggerEvent3.get(0));
        assertEquals("2024-04-23T08:00", triggerEvent3.get(1));
        assertEquals("5.50", triggerEvent3.get(2));
        
    }
    
    
    @Test
    public void testConvertDateToFormat() {
        // Input date-time string in ISO_LOCAL_DATE_TIME format
        String inputDateTime = "2024-04-20T15:30:00";
        Notifier notifier = new Notifier();
        
        // Expected formatted date-time string
        String expectedFormattedDateTime = "20 April 2024 at 3:30PM";
        
        String formattedDateTime = notifier.convertDateToFormat(inputDateTime);
        
        assertEquals(expectedFormattedDateTime, formattedDateTime);
    }
    
    
    
    @Test
    public void testNotifyUsersWithTriggerTimes() {

        // Prepare triggerTimes data for testing
        List<List<String>> triggerTimes = new ArrayList<>();
        triggerTimes.add(Arrays.asList("2024-04-20T12:00:00", "2024-04-20T18:00:00", "10.0"));
        
        //This is to print and compare with a print statement
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        
        Notifier notifier = new Notifier();
        notifier.notifyUsers(triggerTimes);
        String actualOutput = outputStream.toString().trim(); // Trim to remove extra whitespace
        
        String expectedOutput = "Attention!!! Migrane Warning!!! The pressure will drop by 10.0 from 20 April 2024"
                + " at 12:00PM to 20 April 2024 at 6:00PM. Exercise caution!!!";
        assertEquals(expectedOutput, actualOutput);
    }
    

    @Test
    public void testNotifyUsersWithEmptyTriggerTimes() {

        List<List<String>> emptyTriggerTimes = new ArrayList<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Notifier notifier = new Notifier();
        notifier.notifyUsers(emptyTriggerTimes);
        String actualOutput = outputStream.toString().trim();

        String expectedOutput = "Based on our analysis, there are zero migrane triggerring presure drops in the next 7 days.";
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testPromptUserInput_validCoordinates() {
        ByteArrayOutputStream mockOutput = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream("12\n09\n".getBytes()));
        System.setOut(new PrintStream(mockOutput));
        
        List<Double> validCoordinates = WeatherTrendDriver.promptUserInput();
        List<Double> expectedCoordinates = List.of(12.0, 9.0);
        assertEquals(expectedCoordinates, validCoordinates);

        String expectedOutput = "Enter latitude (between -90 and 90): Enter longitude (between -180 and 180): ";
        assertEquals(expectedOutput, mockOutput.toString());
    }
    
}

