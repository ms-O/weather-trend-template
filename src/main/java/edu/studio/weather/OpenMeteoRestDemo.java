package edu.studio.weather;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/*
 * Open-Meteo API interactive reference page: https://open-meteo.com/en/docs/gfs-api
 */
public class OpenMeteoRestDemo {
    
    protected static final String API_URL = "https://api.open-meteo.com/v1/gfs";

    public static void main(String[] args) {
        
        HttpResponse<JsonNode> response = Unirest.get(API_URL)
                .queryString("latitude", 40.03705685765183)
                .queryString("longitude", -75.34258923444702)
                .queryString("hourly", "pressure_msl")
                .queryString("timezone","America/New_York")
                .asJson();

        System.out.println("HTTP Status " + response.getStatus());
        System.out.println(response.getBody().toPrettyString());
    }

}
