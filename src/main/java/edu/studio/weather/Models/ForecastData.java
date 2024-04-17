package edu.studio.weather.Models;

import java.util.List;

public class ForecastData {

    private List<String> time;
    private List<Double> pressure_msl;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Double> getPressureMsl() {
        return pressure_msl;
    }

    public void setPressureMsl(List<Double> pressure_msl) {
        this.pressure_msl = pressure_msl;
    }

}
