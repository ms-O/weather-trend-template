package edu.studio.weather;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class OpenMeteoRestClient {
    
    public static void main(String[] args) {
        WeatherTrendDriver weatherTrendDriver = new WeatherTrendDriver();
        weatherTrendDriver.main(args);
    }
}
