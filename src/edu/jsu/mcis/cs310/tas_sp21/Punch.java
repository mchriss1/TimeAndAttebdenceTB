  
package edu.jsu.mcis.cs310.tas_sp21;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;


public class Punch {	
    

    private int id;
    private int terminalid;
    private String badgeid;
    private long originaltimestamp;
    private int punchtypeid;
    private String adjustmenttype;
    private String adjustedtimestamp;
    
    
    public Punch (int id, int terminalid, String badgeid,
            Timestamp originaltimestamp, int punchtypeid) {
    
        if(id >= 0){this.id = id;}
        this.terminalid = terminalid;
        this.badgeid = badgeid;
        this.originaltimestamp = originaltimestamp.getTime();
        this.punchtypeid = punchtypeid;
        
    }


    public Punch(Badge badge, int terminalid, int punchtypeid) {
        this.badgeid = badge.getId();
        this.terminalid = terminalid;
        this.punchtypeid = punchtypeid;
        this.id = 0;
        this.originaltimestamp = System.currentTimeMillis();
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
        return originaltimestamp;
    }

    public void setOriginaltimestamp(long originaltimestamp) {
        this.originaltimestamp = originaltimestamp;
    }

    public int getPunchtypeid() {
        return punchtypeid;
    }

    public void setPunchtypeid(int punchtypeid) {
        this.punchtypeid = punchtypeid;
    }

  
     
    public String printOriginalTimestamp(){
        StringBuilder s = new StringBuilder();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(originaltimestamp);
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
    
    
    public String adjustTimestamp(GregorianCalendar gc, String adjustment)
    {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss");
        
        sb.append("#").append(badgeid);

        switch (punchtypeid) {
            case 0:
                sb.append(" CLOCKED OUT: ");
                break;
            case 1:
                sb.append(" CLOCKED IN: ");
                break;
            case 2:
                sb.append(" TIMED OUT: ");
                break;
            default:
                sb.append(" ERROR ");
                break;
        }
        
        String s = sdf.format(gc.getTime()).toUpperCase();
        sb.append(s);
        sb.append(" ").append(adjustment);

        return (sb.toString());
    }
    
    public void setAdjustedTimestamp(String adjustedtimestamp) {
        this.adjustedtimestamp = adjustedtimestamp;
    }
    
    public String printAdjustedTimestamp() {
        return adjustedtimestamp;
    }
    
    
    
    
        public void adjust (Shift s)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(originaltimestamp);
        LocalTime ltime = LocalTime.of(gc.get(Calendar.HOUR_OF_DAY), gc.get(Calendar.MINUTE), gc.get(Calendar.SECOND));
        
        LocalTime shiftStart = s.getShiftStart();
        LocalTime shiftStop = s.getShiftStop();
        long interval = s.getShiftInterval();
        long intervalReturn = gc.get(Calendar.MINUTE) % interval;
        LocalTime lunchStart = s.getShiftLunchStart();
        LocalTime lunchStop = s.getShiftLunchStop();
        
        System.out.println(ltime.toString());       
        
        switch (punchtypeid) {
            // Clock Out //
            case 0:
                // Weekend Check //
                if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                {
                    int adjusted = intervalRound();
                    gc.set(Calendar.SECOND, 0);
                    gc.set (Calendar.MINUTE, adjusted);
                    
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isAfter(shiftStop.minusSeconds(1)) && ltime.isBefore(shiftStop.plusMinutes(interval).plusSeconds(1)))
                {
                    gc.set(Calendar.HOUR_OF_DAY, s.getShiftStopHour());
                    gc.set(Calendar.MINUTE, s.getShiftStopMin());
                    gc.set(Calendar.SECOND, 0);

                    adjustmenttype = ("(Shift Stop)");
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isBefore(lunchStop.plusSeconds(1)) && ltime.isAfter(lunchStart.minusSeconds(1)))
                {
                    gc.set(Calendar.HOUR_OF_DAY, s.getLunchStartHour());
                    gc.set(Calendar.MINUTE, s.getLunchStartMin());
                    gc.set(Calendar.SECOND, 0);
                        
                    adjustmenttype = ("(Lunch Start)");
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isBefore(shiftStop) && ltime.isAfter(shiftStop.minusMinutes(15).minusSeconds(1)))
                {
                    if (ltime.isAfter(shiftStop.minusMinutes(5).minusSeconds(1)))
                    {
                        // Grace //
                        int adjusted = grace(shiftStart, shiftStop);
                        gc.set(Calendar.SECOND, 0);
                        gc.set (Calendar.MINUTE, adjusted);
                        
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                    else
                    {
                        // Dock //
                        gc.set(Calendar.SECOND, 0);
                        gc.set (Calendar.MINUTE, s.getShiftStopMin());
                        gc.add(Calendar.MINUTE, -15);
                        
                        adjustmenttype = ("(Shift Dock)");
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                }
                
                else
                {
                    // None //
                    if (intervalReturn == 0)
                    {
                        gc.set(Calendar.SECOND, 0);
                        adjustmenttype = ("(None)");
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                    
                    // Interval Round //
                    else
                    {
                        int adjusted = intervalRound();
                        gc.set(Calendar.SECOND, 0);
                        gc.set (Calendar.MINUTE, adjusted);

                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                }
                break;
                                
            // Clock In //
            case 1:
                
             // Weekend Check //
                if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                {
                    int adjusted = intervalRound();
                    gc.set(Calendar.SECOND, 0);
                    gc.set (Calendar.MINUTE, adjusted);
                    
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isBefore(shiftStart.plusSeconds(1)) && ltime.isAfter(shiftStart.minusMinutes(interval).minusSeconds(1)))
                {
                    gc.set(Calendar.HOUR_OF_DAY, s.getShiftStartHour());
                    gc.set(Calendar.MINUTE, s.getShiftStartMin());
                    gc.set(Calendar.SECOND, 0);

                    adjustmenttype = ("(Shift Start)");
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isBefore(lunchStop.plusSeconds(1)) && ltime.isAfter(lunchStart.minusSeconds(1)))
                {
                    gc.set(Calendar.HOUR_OF_DAY, s.getLunchStopHour());
                    gc.set(Calendar.MINUTE, s.getLunchStopMin());
                    gc.set(Calendar.SECOND, 0);
                        
                    adjustmenttype = ("(Lunch Stop)");
                    setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                }
                
                else if (ltime.isAfter(shiftStart) && ltime.isBefore(shiftStart.plusMinutes(15).plusSeconds(1)))
                {
                    if (ltime.isBefore(shiftStart.plusMinutes(5).plusSeconds(1)))
                    {
                        // Grace //
                        int adjusted = grace(shiftStart, shiftStop);
                        gc.set(Calendar.SECOND, 0);
                        gc.set(Calendar.MINUTE, adjusted);
                        
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                    else
                    {
                        // Dock //
                        gc.set(Calendar.SECOND, 0);
                        gc.set(Calendar.MINUTE, s.getShiftStartMin());
                        gc.add(Calendar.MINUTE, 15);
                        
                        adjustmenttype = ("(Shift Dock)");
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                }
                
                else
                {
                    if (intervalReturn == 0)
                    {
                        // None //
                        gc.set(Calendar.SECOND, 0);
                        adjustmenttype = ("(None)");
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                    
                    else
                    {
                        // Interval Round //
                        int adjusted = intervalRound();
                        gc.set(Calendar.SECOND, 0);
                        gc.set (Calendar.MINUTE, adjusted);

                        adjustmenttype = ("(Shift Start)");
                        setAdjustedTimestamp(adjustTimestamp(gc, adjustmenttype));
                    }
                }
                break;                      
        }
    }
   
               
         
    
    
       private int grace (LocalTime shiftStart, LocalTime shiftStop)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(originaltimestamp);
        
        int p = punchtypeid;
        int adjusted = 0;
        
        if ( p <= 0){
            adjusted = shiftStop.getMinute();
            adjustmenttype = ("(Shift Stop)");
        }
        if ( p >= 1){
            adjusted = shiftStart.getMinute();
            adjustmenttype = ("(Shift Start)");
        }
        
        return adjusted;
    }
    
    private int intervalRound ()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(originaltimestamp);
        
        int minute = gc.get(Calendar.MINUTE);
        int second = gc.get(Calendar.SECOND);
        int adjusted = 0;
        
        if ( minute % 15 == 7){
            if (second >= 30) {
                adjusted = roundUp(minute, 15);   
            }
            else {
                adjusted = roundDown(minute, 15);
            }    
        }
        else if ( minute % 15 < 7.5){
            adjusted = roundDown(minute, 15);    
        }
        else if ( minute % 15 >= 7.5){
            adjusted = roundUp(minute, 15);
        }
        
        adjustmenttype = ("(Interval Round)");
        
        return adjusted;
    }

    
        private int roundDown(int a, int b) 
    {
        return a>= 0 ? (a/ b) * b : ((a - b + 1) / b) * b;
    }
    
    private int roundUp(int a, int b) 
    {
        return a>= 0 ? ((a + b - 1) / b) * b : (a / b) * b;
       
}
    
}
    
    


