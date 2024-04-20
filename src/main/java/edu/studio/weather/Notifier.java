package edu.studio.weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Notifier {

    public void notifyUsers(List<List<String>> triggerTimes) {
        
        if (triggerTimes.size() > 0 ) {
            
            for (List<String> triggerInterval : triggerTimes) {
                String startTime = triggerInterval.get(0);
                String endTime = triggerInterval.get(1);
                String pressureDrop = triggerInterval.get(2);
                String startTimeToShow = convertDateToFormat(startTime);
                String endTimeShow = convertDateToFormat(endTime);
//                String pressureDropShow = convertDateToFormat(pressureDrop);
                
                String messageToDisplay = "Attention!!! Migrane Warning!!! The pressure "
                        + "will drop by " + pressureDrop  + " from " + startTimeToShow + " to " + 
                        endTimeShow + ". Exercise caution!!!";
                System.out.println(messageToDisplay);
                
            }
        }
        else {
            System.out.println("Based on our analysis, there are zero migrane triggerring presure drops in the next 7 days.");
        }
        
    }
    
    
    public static String convertDateToFormat(String inputDateTime) {
        
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, inputFormatter);
        
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy 'at' h:mma");
        
        String formattedDateTime = dateTime.format(outputFormatter);
        
//        System.out.println("Formatted datetime: " + formattedDateTime);
        return formattedDateTime;
        
    }
}
