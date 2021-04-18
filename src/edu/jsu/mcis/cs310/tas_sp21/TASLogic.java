package edu.jsu.mcis.cs310.tas_sp21;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONValue;

 
public class TASLogic {
    
    public static final int CLOCKIN = 1;
    public static final int CLOCKOUT = 0;
    
 
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, 
            Shift shift) {

        int totalMin = 0;
        long totalMillis = 0;
        long inTime = 0;
        long outTime = 0;
        int punchCounter = 0;
        int lunchTime = 30;
        
        for(int i = 0; i < dailypunchlist.size(); i++) {
            
           if (dailypunchlist.get(i).getPunchtypeid() == CLOCKIN) {
               
               inTime = dailypunchlist.get(i).getAdjustedTimeStamp().getTime();
               punchCounter++;
               continue;
               
           }

           if (dailypunchlist.get(i).getPunchtypeid() == CLOCKOUT) {
               
               outTime = dailypunchlist.get(i).getAdjustedTimeStamp().getTime();
               punchCounter++;
               
           }
           
           if (inTime != 0 && outTime != 0) {
               
               totalMillis += outTime - inTime;
               
           }
           
           
           inTime = 0;
           outTime = 0;
           
        }
        
        if (totalMillis != 0) {
            
            totalMin = (int) (totalMillis/60000);
            
        }
        
        if (totalMin > shift.getShiftLunchDeduct() && punchCounter <= 3) {
            
            totalMin -= lunchTime;
            
        }
        
        return totalMin;
        
    }    
   
        public static String getPunchListAsJSON(ArrayList<Punch> dailyPunchList){
            
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for(Punch p : dailyPunchList){
            
            HashMap<String, String> punchdata = new HashMap<>();
            punchdata.put("id", String.valueOf(p.getId()));
            punchdata.put("badgeid", p.getBadgeid());
            punchdata.put("terminalid", String.valueOf(p.getTerminalid()));
            punchdata.put("punchtypeid", String.valueOf(p.getPunchtypeid()));
            punchdata.put("punchdata", p.getAdjustmenttype());
            punchdata.put("originaltimestamp", Long.toString(p.getOriginaltimestamp()));
            punchdata.put("adjustedtimestamp", Long.toString(p.getAdjustedTimeStamp().getTime()));
            
            jsonData.add(punchdata);
            
        }
        
        return JSONValue.toJSONString(jsonData);
    }
     

      }

