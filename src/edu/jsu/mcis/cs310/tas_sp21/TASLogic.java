 
package edu.jsu.mcis.cs310.tas_sp21;

 
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
            
           if (dailypunchlist.get(i).getPunchtypeid() == CLOCKIN) 
           {
               
               inTime = dailypunchlist.get(i).getAdjustedTimeStamp() ;
               punchCounter++;
               continue;
               
           }
}
