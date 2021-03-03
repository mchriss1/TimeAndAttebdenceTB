  
package edu.jsu.mcis.cs310.tas_sp21;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;


public class Punch {	
    

    private int id;
    private int terminalid;
    private String badgeid;
    private long originaltimestamp;
    private int punchtypeid;
    private String adjustmenttype;
    private long adjustedtimestamp;

    public Punch(Badge badge, int terminalid, int punchtypeid) {
        this.badgeid = badge.getId();
        this.terminalid = terminalid;
        this.punchtypeid = punchtypeid;
        this.id = 0;
        this.originaltimestamp = 0;
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

    public String getAdjustmenttype() {
        return adjustmenttype;
    }

    public void setAdjustmenttype(String adjustmenttype) {
        this.adjustmenttype = adjustmenttype;
    }

    public long getAdjustedtimestamp() {
        return adjustedtimestamp;
    }

    public void setAdjustedtimestamp(long adjustedtimestamp) {
        this.adjustedtimestamp = adjustedtimestamp;
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
}