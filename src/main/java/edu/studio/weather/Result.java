package edu.studio.weather;

public class Result {
    private String startTime;
    private String endTime;
    private double pressureChange;

    public Result(String startTime, String endTime, double pressureChange) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pressureChange = pressureChange;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getPressureChange() {
        return pressureChange;
    }

    public void setPressureChange(double pressureChange) {
        this.pressureChange = pressureChange;
    }
}
