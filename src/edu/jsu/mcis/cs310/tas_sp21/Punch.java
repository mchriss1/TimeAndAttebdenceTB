  
package edu.jsu.mcis.cs310.tas_sp21;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;


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
        this.adjustedtimestamp = 0; 
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
        /*if(punchtypeid==1){
            s.append("#").append(badgeid).append(" ");
            s.append("CLOCKED IN: ");
        }
        else if(punchtypeid==0)
        {
            s.append("#").append(badgeid).append(" ");
            s.append("CLOCKED OUT: ");
        }
        else if {
            s.append("#").append(badgeid).append(" ");
            s.append("TIMED OUT: ");
        }
*/

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

}