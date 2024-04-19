package edu.studio.weather;

import java.util.*;


public class HourlyData {

    private List<String> time;
    private List<Double> pressure_msl;
    
    
    
    public HourlyData(List<String> time, List<Double> pressure_msl) {
        super();
        this.time = time;
        this.pressure_msl = pressure_msl;
    }

    public List<String> getTime() {
        return time;
    }
    
    public void setTime(List<String> time) {
        this.time = time;
    }
    
    public List<Double> getPressure_msl() {
        return pressure_msl;
    }
    
    public void setPressure_msl(List<Double> pressure_msl) {
        this.pressure_msl = pressure_msl;
    }
    
    
}
