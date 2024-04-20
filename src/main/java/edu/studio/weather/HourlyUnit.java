package edu.studio.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyUnit {

    private String time_iso;
    private String pressure_msl;

    
    public HourlyUnit() {
    
    }
    
    public HourlyUnit(String time_iso, String pressure_msl) {
        super();
        this.time_iso = time_iso;
        this.pressure_msl = pressure_msl;
    }

    public String getTime_iso() {
        return time_iso;
    }

    public void setTime_iso(String time) {
        this.time_iso = time;
    }

    public String getPressure_msl() {
        return pressure_msl;
    }

    public void setPressure_msl(String pressure_msl) {
        this.pressure_msl = pressure_msl;
    }

}
