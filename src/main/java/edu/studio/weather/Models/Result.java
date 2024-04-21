package edu.studio.weather.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Result {
    private LocalDateTime migraineStartTime;
    private LocalDateTime migraineEndTime;
    private double pressureDrop;

    public Result(LocalDateTime migraineStartTime, LocalDateTime migraineEndTime, double pressureDrop) {
        this.migraineStartTime = migraineStartTime;
        this.migraineEndTime = migraineEndTime;
        this.pressureDrop = pressureDrop;
    }

    public LocalDateTime getMigraineStartTime() {
        return migraineStartTime;
    }

    public void setMigraineStartTime(LocalDateTime migraineStartTime) {
        this.migraineStartTime = migraineStartTime;
    }

    public LocalDateTime getMigraineEndTime() {
        return migraineEndTime;
    }

    public void setMigraineEndTime(LocalDateTime migraineEndTime) {
        this.migraineEndTime = migraineEndTime;
    }

    public double getPressureDrop() {
        return pressureDrop;
    }

    public void setPressureDrop(double pressureDrop) {
        this.pressureDrop = pressureDrop;
    }

    @Override
    public String toString() {
        return "Potential migraine episode from " + migraineStartTime + " to " + migraineEndTime + " | change : "
                + pressureDrop + " hPa";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Result result = (Result) o;
        return Double.compare(result.pressureDrop, pressureDrop) == 0 &&
                migraineStartTime.equals(result.migraineStartTime) &&
                migraineEndTime.equals(result.migraineEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(migraineStartTime, migraineEndTime, pressureDrop);
    }
}
