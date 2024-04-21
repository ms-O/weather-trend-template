package edu.studio.weather.Models;

public class Forecast {

    private String time;
    private double pressure;

    public Forecast(String time, double pressure) {
        this.time = time;
        this.pressure = pressure;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "Forecast [time=" + time + ", pressure=" + pressure + "]";
    }

}
