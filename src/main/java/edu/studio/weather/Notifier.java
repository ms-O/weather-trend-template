package edu.studio.weather;
import java.util.List;

public class Notifier {
    public static void printResults(List<Result> results) {
        System.out.println("Alert!, Pressure changes that may trigger a headache are predicted for the following time ranges:");
        for (Result result : results) {
            System.out.println(result.getStartTime() + " to " + result.getEndTime() +
                               " change " + result.getPressureChange() + " hPa");
        }
    }
}

