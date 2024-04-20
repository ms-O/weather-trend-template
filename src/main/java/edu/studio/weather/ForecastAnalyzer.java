package edu.studio.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForecastAnalyzer {
    
    //Interval based approach, only the item in i and i+6 position are considered
    public List<List<String>> calculateMigranePeriods(HourlyData hourlyData) {
        double MIGRANEDROP = 5;
        List<List<String>> triggerTimes = new ArrayList<>();
        List<String> hourlyTimes = hourlyData.getTime();
        List<Double> hourlyPressure = hourlyData.getPressure_msl();
        int lengthOfTimePressureData = hourlyTimes.size();
        
        int prevIndex = 0;
        for(int i = 0; i < lengthOfTimePressureData; i = i + 6) {
            double pressureDrop = hourlyPressure.get(prevIndex) - hourlyPressure.get(i);
            if (pressureDrop >= MIGRANEDROP) {
                List<String> triggerEvent = new ArrayList<>();
                triggerEvent.add(String.valueOf(hourlyTimes.get(prevIndex)));
                triggerEvent.add(String.valueOf(hourlyTimes.get(i)));
                triggerEvent.add(String.valueOf(pressureDrop));
                
                triggerTimes.add(triggerEvent);
                triggerEvent.clear();
            }
            prevIndex = i;
            
        }
        return triggerTimes;
    }
    
    
    // More precise approach where all items are considered
    public List<List<String>> calculateMigranePeriodsPrecise(HourlyData hourlyData) {
        double MIGRANEDROP = 5;
        int hourInterval = 6;
        List<List<String>> triggerTimes = new ArrayList<>();
        List<String> hourlyTimes = hourlyData.getTime();
        List<Double> hourlyPressure = hourlyData.getPressure_msl();
//        List<String> hourlyTimes = Arrays.asList("2024-04-23T05:00", "2024-04-23T06:00", "2024-04-23T07:00", "2024-04-23T08:00", "2024-04-23T09:00", "2024-04-23T10:00", "2024-04-23T11:00", "2024-04-23T12:00", "2024-04-23T13:00", "2024-04-23T14:00");
//        List<Double> hourlyPressure = Arrays.asList(1013.6, 1013.5, 1013.5, 1008.0, 1011.1, 1013.0, 1010.6, 1010.8, 1000.7);
//        
        int lengthOfTimePressureData = hourlyTimes.size();
        
        for(int i = 6; i < lengthOfTimePressureData; i = i + 1) {
            int leftIndex = i - hourInterval;
            double currMax = hourlyPressure.get(leftIndex);
            int currMaxIndex = leftIndex;
            int rightIndex = i;
            for (int j = leftIndex; j < rightIndex; j ++) {
                double currPressure = hourlyPressure.get(j);
                if (currPressure > currMax) {
                    currMax = currPressure;
                    currMaxIndex = j;
                }
                double pressureDrop = currMax - hourlyPressure.get(j);
                if (pressureDrop >= MIGRANEDROP) {
                    List<String> triggerEvent = new ArrayList<>();
                    triggerEvent.add(String.valueOf(hourlyTimes.get(currMaxIndex)));
                    triggerEvent.add(String.valueOf(hourlyTimes.get(j)));
                    String stringPressureDrop = String.format("%.2f", pressureDrop);
                    triggerEvent.add(stringPressureDrop);
                    triggerTimes.add(triggerEvent);
                }
                
            }
        }
        return triggerTimes;
    }
    
    public static void main(String[] args)  {
        
    }

}
