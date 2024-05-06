package edu.studio.weather;

import java.util.ArrayList;
import java.util.List;

public class ForecastAnalyzer {

    private  List<Forecast> forecasts;

    public ForecastAnalyzer(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public List<Result> analyze() {
        List<Result> pressureDropResults = new ArrayList<>(); 
        for (int i = 0; i < forecasts.size() - 6; i += 6) {
            double pressureChange = Math.abs(forecasts.get(i).getPressure() - forecasts.get(i + 6).getPressure());
            if (pressureChange >= 5.0) {
                Result result = new Result(forecasts.get(i).getTime(), forecasts.get(i+6).getTime(), pressureChange);
                pressureDropResults.add(result);
            }
        }
        return pressureDropResults;
}
}