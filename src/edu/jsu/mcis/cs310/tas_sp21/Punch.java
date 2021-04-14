  
package edu.jsu.mcis.cs310.tas_sp21;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Calendar;


public class Punch {	
    

    private int id;
    private int terminalid;
    private String badgeid;
    private Timestamp originaltimestamp;
    private int punchtypeid;
    private String adjustmenttype;
    private Timestamp adjustedtimestamp;
    
    
    public Punch (int id, int terminalid, String badgeid,
            Timestamp originaltimestamp, int punchtypeid) {
    
        if(id >= 0){this.id = id;}
        this.terminalid = terminalid;
        this.badgeid = badgeid;
        this.originaltimestamp = originaltimestamp;
        this.punchtypeid = punchtypeid;
        
    }


    public Punch(Badge badge, int terminalid, int punchtypeid) {
        this.badgeid = badge.getId();
        this.terminalid = terminalid;
        this.punchtypeid = punchtypeid;
        this.id = 0;
        this.originaltimestamp = new Timestamp(new GregorianCalendar().getTimeInMillis());
        this.adjustedtimestamp = null; 
        this.adjustmenttype = null;
    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(int terminalid) {
        this.terminalid = terminalid;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public void setBadgeid(String badgeid) {
        this.badgeid = badgeid;
    }

    public long getOriginaltimestamp() {
        return originaltimestamp.getTime();
    }

    public Timestamp getOriginaltimestamp2() {
        return originaltimestamp;
    }
 
    public int getPunchtypeid() {
        return punchtypeid;
    }

    public void setPunchtypeid(int punchtypeid) {
        this.punchtypeid = punchtypeid;
    }

    public Timestamp getAdjustedTimeStamp() {
        return adjustedtimestamp;
    }

     
    public String printOriginalTimestamp(){
        StringBuilder s = new StringBuilder();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(originaltimestamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");

        switch(punchtypeid){
            case(1):
                 s.append("#").append(badgeid).append(" ");
                 s.append("CLOCKED IN: ");
                 break;
                 
            case (0):
                s.append("#").append(badgeid).append(" ");
                s.append("CLOCKED OUT: ");
                break;
            
            case(2) :
                 s.append("#").append(badgeid).append(" ");
                 s.append("TIMED OUT: ");
                 break;
                 
            default :
                s.append("Error.");
                break;
                
                }

        String otimestamp = sdf.format(gc.getTime());
        s.append(otimestamp);

        return (s.toString().toUpperCase());

    }
    
    
     public String printAdjustedTimestamp() {
        String sb = "";
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(adjustedtimestamp.getTime());
        
        switch(this.punchtypeid){
            case 0:
                sb = "CLOCKED OUT:";
                break;
            case 1:
                sb = "CLOCKED IN:";
                break;
            case 2:
                sb = "TIMED OUT:";
                break;
          
        }   
        
        String pattern = "EEE MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formattedDate = sdf.format(gc.getTime()).toUpperCase();

        String output = "#" + getBadgeid() + " " + sb + " " + formattedDate + " (" + adjustmenttype + ")";
            
        return output;
    }

    
        public void adjust(Shift s){
        
        LocalTime shiftStart = s.getShiftStart();
        LocalTime shiftStop = s.getShiftStop();
        LocalTime lunchStart = s.getShiftLunchStart();
        LocalTime lunchStop = s.getShiftLunchStop();
        
        int grace  = s.getShiftGrace();
        int interval = s.getShiftInterval();
        int dock = s.getShiftDock();
         
        
        LocalDateTime punchTimeStamp = originaltimestamp.toLocalDateTime();
        LocalTime punchTime = punchTimeStamp.toLocalTime();
         
        int totalpunchTimeMinutes = punchTime.getMinute() + (punchTime.getHour()*60);
        int totalshiftStopMinutes = shiftStop.getMinute() + (punchTime.getHour()*60);
        int totalshiftStartMinutes = shiftStart.getMinute() + (shiftStart.getHour()*60);
        boolean weekend =  (punchTimeStamp.getDayOfWeek().toString().equals("SATURDAY") || 
                punchTimeStamp.getDayOfWeek().toString().equals("SUNDAY") );
        
        adjustedtimestamp = Timestamp.valueOf(punchTimeStamp);
        
        switch(this.punchtypeid){
            
            case 0:
                
                //If punch is clock out for lunch break (checks)
           
                if( punchTime.isAfter(shiftStart) && punchTime.isBefore(lunchStop) && (!weekend) ) {
                  
                    if( punchTime.isAfter(lunchStart) && punchTime.isBefore(lunchStop)) {
                        adjustmenttype = "Lunch Start";
                        adjustedtimestamp = Timestamp2(punchTimeStamp, lunchStart);                       
                    }
                    
                }
                
                else if(weekend) {
                    
                    if(punchTime.isBefore(shiftStop) ) {
                        
                        if( (totalshiftStopMinutes - totalpunchTimeMinutes) <= grace  ) {
                            adjustmenttype = "Shift Stop";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStop);
                        }
                        
                        else if ( (totalshiftStopMinutes - totalpunchTimeMinutes) > grace  && 
                                (totalshiftStopMinutes - totalpunchTimeMinutes) <= dock ) {
                            adjustmenttype = "Shift Dock";
                            totalshiftStopMinutes = totalshiftStopMinutes - dock;
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);  
                        }
                        
                        else {
                            adjustmenttype = "Interval Round";
                            int timeInterval = totalshiftStopMinutes - totalpunchTimeMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 8) {
                                totalshiftStopMinutes = totalshiftStopMinutes - (quotient*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                            }
                            else {
                                totalshiftStopMinutes = totalshiftStopMinutes - ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                            }
                        }
                    }
                    
                    else if(punchTime.isAfter(shiftStop) ) { 
                        
                        int timeInterval = totalpunchTimeMinutes - totalshiftStopMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                        
                        if(timeInterval <= interval) {
                            
                            if( (remainder == 0) && punchTime.getSecond() < 60) {
                                adjustmenttype = "None";
                            }
                            else {
                                adjustmenttype = "Shift Stop";
                            }
                            totalpunchTimeMinutes = totalpunchTimeMinutes - (quotient*interval);
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        else {
                            adjustmenttype = "Interval Round";
                            totalpunchTimeMinutes = totalpunchTimeMinutes - ((quotient+1)*interval);
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                        }
                        
                    }

                }
                
                
                //If the punch is clock out for the shift end
                
                else if ( punchTime.isAfter(lunchStop) && (!weekend) ) {
                    
                    //Before the shift stop
                    
                    if(punchTime.isBefore(shiftStop) ) {
                        
                        if( (totalshiftStopMinutes - totalpunchTimeMinutes) <= grace  ) {
                            adjustmenttype = "Shift Stop";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStop);
                        }
                        
                        else if ( (totalshiftStopMinutes - totalpunchTimeMinutes) > grace  && 
                                (totalshiftStopMinutes - totalpunchTimeMinutes) <= dock ) {
                            adjustmenttype = "Shift Dock";
                            totalshiftStopMinutes = totalshiftStopMinutes - dock;
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        
                        else {
                            adjustmenttype = "Interval Round";
                            int timeInterval = totalshiftStopMinutes - totalpunchTimeMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 8) {
                                totalshiftStopMinutes = totalshiftStopMinutes - (quotient*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                            }
                            else {
                                totalshiftStopMinutes = totalshiftStopMinutes - ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                            }
               
                        }
                        
                    }
                    
                    //After the shift stop
                    
                    else if( punchTime.isAfter(shiftStop) && (!weekend) ) {
                        
                        int timeInterval = totalpunchTimeMinutes - totalshiftStopMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                        
                        if(timeInterval <= interval) {
                            
                            if( (remainder == 0) && punchTime.getSecond() < 60) {
                                adjustmenttype = "None";
                            }
                            else {
                                adjustmenttype = "Shift Stop";
                            }
                            totalpunchTimeMinutes = totalpunchTimeMinutes - (quotient*interval);
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStopMinutes);
                        }
                        else {
                            adjustmenttype = "Interval Round";
                            totalpunchTimeMinutes = totalpunchTimeMinutes - ((quotient+1)*interval);
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                        }
                        
                    }
                                       
                }
                
                break;
                
                
            case 1:
                
                // If punch is clock in for lunch stop
                
                if( punchTime.isAfter(lunchStart) && punchTime.isBefore(shiftStop) && (!weekend) ) {
                  
                    if( punchTime.isAfter(lunchStart) && punchTime.isBefore(lunchStop)) {
                        adjustmenttype = "Lunch Stop";
                        adjustedtimestamp = Timestamp2(punchTimeStamp, lunchStop);    
                    }
                    
                }
                
                // If punch is clock in for shift start
                
                else if( punchTime.isBefore(lunchStart) && (!weekend) ) {
                    
                    //Before the shift start
                    
                    if( punchTime.isBefore(shiftStart) ) {
                        
                        int timeInterval = totalshiftStartMinutes - totalpunchTimeMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                                           
                        if(timeInterval <= interval) {
                            adjustmenttype = "Shift Start";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStart);
                        }
                        else {
                            adjustmenttype = "Interval Round";
                            if(remainder <= 7) {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                            else {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                        }    

                    }
                    
                    // After the shift start
                    
                    else if( punchTime.isAfter(shiftStart) ) {
                    
                        if( (totalpunchTimeMinutes - totalshiftStartMinutes) <= grace  ) {
                            adjustmenttype = "Shift Start";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStart);
                        }

                        else if ( (totalpunchTimeMinutes - totalshiftStartMinutes) <= dock ) {
                            adjustmenttype = "Shift Dock";
                            totalshiftStartMinutes = totalshiftStartMinutes + dock;
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                        }

                        else {
                            adjustmenttype = "Interval Round";
                            int timeInterval = totalpunchTimeMinutes - totalshiftStartMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 7) {
                                totalshiftStartMinutes = totalshiftStartMinutes + (quotient*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                            }
                            else {
                                totalshiftStartMinutes = totalshiftStartMinutes + ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                            }                        
                        }
                        
                    }
                    
                }
            
                else if(weekend) {
                    
                    if( punchTime.isBefore(shiftStart) ) {
                        
                        int timeInterval = totalshiftStartMinutes - totalpunchTimeMinutes;
                        int quotient = timeInterval/interval;
                        int remainder = timeInterval%interval;
                                           
                        if(timeInterval <= interval) {
                            adjustmenttype = "Shift Start";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStart);
                        }
                        else {
                            adjustmenttype = "Interval Round";
                            if(remainder <= 7) {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                            else {
                                totalpunchTimeMinutes = totalshiftStartMinutes - ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalpunchTimeMinutes);
                            }
                        }    
                        
                    }
                    
                    else if( punchTime.isAfter(shiftStart) ) {
                        
                        if( (totalpunchTimeMinutes - totalshiftStartMinutes) <= grace ) {
                            adjustmenttype = "Shift Start";
                            adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStart);
                        }

                        else if ( (totalpunchTimeMinutes - totalshiftStartMinutes) <= dock ) {
                            adjustmenttype = "Shift Dock";
                            totalshiftStartMinutes = totalshiftStartMinutes + dock;
                            adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                        }

                        else {
                            adjustmenttype = "Interval Round";
                            int timeInterval = totalpunchTimeMinutes - totalshiftStartMinutes;
                            int quotient = timeInterval/interval;
                            int remainder = timeInterval%interval;
                            if(remainder <= 7) {
                                totalshiftStartMinutes = totalshiftStartMinutes + (quotient*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                            }
                            else {
                                totalshiftStartMinutes = totalshiftStartMinutes + ((quotient+1)*interval);
                                adjustedtimestamp = Timestamp1(punchTimeStamp, totalshiftStartMinutes);
                            }                        
                        }
                        
                    }
                    
                }

                break;
                
                
            case 2:
                adjustmenttype = "Time Out";
                adjustedtimestamp = Timestamp2(punchTimeStamp, shiftStop);
                break;
                
            default:
                System.out.println("ERROR");
                               
        }
        
      }
        
        private Timestamp Timestamp1(LocalDateTime punch, int totalminutes) {
        punch = punch.withHour(totalminutes/60);
        punch = punch.withMinute(totalminutes%60);
        punch = punch.withSecond(0);
        Timestamp ts = Timestamp.valueOf(punch);
        
        return ts;
    }
    
        private Timestamp Timestamp2(LocalDateTime punch, LocalTime time) {
        punch = punch.withHour(time.getHour());
        punch = punch.withMinute(time.getMinute());
        punch = punch.withSecond(0);
        Timestamp ts = Timestamp.valueOf(punch);
        
        return ts;
    }
    
      
}

         