package edu.studio.weather;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;


public class WeatherTrendDriver {
    public static void main(String[] args) {

       OpenMeteoRestClient openrest= new OpenMeteoRestClient();
       //System.out.println(openrest.fetchWeatherData()); 
        //System.out.println("Forecasts:");
        OpenMeteoParser openParser= new OpenMeteoParser();
        List<Forecast> forecastes = openParser.parseData(openrest.fetchWeatherData());
        //for (Forecast forecast : forecastes) {
          //  System.out.println(forecast);
        //} 
        ForecastAnalyzer analyzer = new ForecastAnalyzer(forecastes);
        List<Result> pressureDropResults = analyzer.analyze();      
        //System.out.println();
        Notifier.printResults(pressureDropResults);
        
       
        
        
        
        
    }

}