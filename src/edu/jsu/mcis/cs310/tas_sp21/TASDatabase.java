 package edu.jsu.mcis.cs310.tas_sp21;

import java.sql.*;
import java.sql.Connection;
import java.util.GregorianCalendar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class TASDatabase {
    
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        
public TASDatabase(){
    
          try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost:/tas?autoReconnect=true&useSSL=false");
            String username = "teamB";
            String password = "apple";
            System.out.println("Connecting to " + server + "...");
            
              /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);
            
            //  conn.close(); 
        } 
          
          catch (Exception e) {
            System.err.println(e.toString());
        }
       
       finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }   
    }
   

/**
 * Method for retrieving information about a single punch from the database.This method should then query the database, 
 retrieve the punch data from the database, populate a new Punch object,
 and then return this object to the caller.
     * @param punchid
     * @return 
 */

    public Punch getPunch(int punchid){
        
        Timestamp originaltimestamp = null;
        Punch p = null; 

        
        try{
            
        // prepare statement
        pstSelect = conn.prepareStatement("SELECT * FROM punch WHERE id = ?");
        
        //set params
        pstSelect.setInt(1, punchid);
        
        //execute
        pstSelect.execute();
        resultset = pstSelect.getResultSet();
        
        //get results
        resultset.first();
        int punchId = resultset.getInt(1);
        int punchTerminalID = resultset.getInt(2);
        String punchBadge =resultset.getString(3);
        int punchTypeID = resultset.getInt(5);
        originaltimestamp = resultset.getTimestamp(4);

        
        p = new Punch(punchId,punchTerminalID,punchBadge, originaltimestamp, punchTypeID);
        
         
        }
        catch(Exception e){
            System.err.println("** getPunch: " + e.toString());
        }

        return p;
    }
 
 /**
  * Method creating objects of the Badge class.
  * @param badgeid
  * @return
  * @throws Exception 
  */
 
    public Badge getBadge(String badgeid){
        try{
            
            // prepare statement
            pstSelect = conn.prepareStatement("SELECT * FROM badge WHERE id = ?");

            //set params
            pstSelect.setString(1, badgeid);

            //execute
            pstSelect.execute();
            resultset = pstSelect.getResultSet();
            resultset.first();

            //get results            resultset.first();

            String idNum = resultset.getString(1);
            String name = resultset.getString(2);

            Badge b = new Badge(idNum, name);

            return b;
        }
        
        catch(Exception e){
            System.err.println("** getBadge: " + e.toString());
        }

        return null;
        
    }

  //GET SHIFT BY ID
    
    public Shift getShift (int shiftid){
        
        try{
            
             
            pstSelect = conn.prepareStatement("SELECT * FROM shift WHERE id = ?");

             
            pstSelect.setInt(1, shiftid);

           
            pstSelect.execute();
            resultset = pstSelect.getResultSet();

             
            resultset.first();

            String description = resultset.getString("description");

            String start = resultset.getString("start");
            String[] arrayStart = start.split(":");
            int startHour = Integer.parseInt(arrayStart[0]);
            int startMin = Integer.parseInt(arrayStart[1]);

            String stop = resultset.getString("stop");
            String[] arrayStop = stop.split(":");
            int stopHour = Integer.parseInt(arrayStop[0]);
            int stopMin = Integer.parseInt(arrayStop[1]);

            int interval = resultset.getInt("interval");
            int gracePeriod = resultset.getInt("graceperiod");
            int dock = resultset.getInt("dock");

            String lunchStart = resultset.getString("lunchstart");
            String[] arrayLunchStart = lunchStart.split(":");
            int lunchStartHour = Integer.parseInt(arrayLunchStart[0]);
            int lunchStartMin = Integer.parseInt(arrayLunchStart[1]);

            String lunchStop = resultset.getString("lunchstop");
            String[] arrayLunchStop = lunchStop.split(":");
            int lunchStopHour = Integer.parseInt(arrayLunchStop[0]);
            int lunchStopMin = Integer.parseInt(arrayLunchStop[1]);

            int lunchDeduct = resultset.getInt("lunchdeduct");

            Shift s = new Shift(description, startHour, startMin, stopHour, stopMin, gracePeriod, interval,
                    dock, lunchStartHour, lunchStartMin, lunchStopHour, lunchStopMin, lunchDeduct);

            return s;
            
        }
                
        catch(Exception e){
            System.err.println("** getShift: " + e.toString());
        }

        return null;
    }
    
 
    
  //GET SHIFT BY BADGE 
  public Shift getShift(Badge badge){
        
        try{
            
            
            pstSelect = conn.prepareStatement("SELECT * FROM shift INNER JOIN employee"
                    + " ON employee.shiftid = shift.id INNER JOIN badge ON badge.id"
                    + " = employee.badgeid WHERE badge.id = ?");

           
            pstSelect.setString(1, badge.getId() );

             
            pstSelect.execute();
            resultset = pstSelect.getResultSet();

             
            resultset.first();

            String description = resultset.getString("description");

            String start = resultset.getString("start");
            String[] arrayStart = start.split(":");
            int startHour = Integer.parseInt(arrayStart[0]);
            int startMin = Integer.parseInt(arrayStart[1]);

            String stop = resultset.getString("stop");
            String[] arrayStop = stop.split(":");
            int stopHour = Integer.parseInt(arrayStop[0]);
            int stopMin = Integer.parseInt(arrayStop[1]);

            int interval = resultset.getInt("interval");
            int gracePeriod = resultset.getInt("graceperiod");
            int dock = resultset.getInt("dock");

            String lunchStart = resultset.getString("lunchstart");
            String[] arrayLunchStart = lunchStart.split(":");
            int lunchStartHour = Integer.parseInt(arrayLunchStart[0]);
            int lunchStartMin = Integer.parseInt(arrayLunchStart[1]);

            String lunchStop = resultset.getString("lunchstop");
            String[] arrayLunchStop = lunchStop.split(":");
            int lunchStopHour = Integer.parseInt(arrayLunchStop[0]);
            int lunchStopMin = Integer.parseInt(arrayLunchStop[1]);

            int lunchDeduct = resultset.getInt("lunchdeduct");

            Shift s = new Shift(description, startHour, startMin, stopHour, stopMin, gracePeriod, interval,
                    dock, lunchStartHour, lunchStartMin, lunchStopHour, lunchStopMin, lunchDeduct);

            return s;
        }
        
        catch(Exception e){
            System.err.println("** getShift: " + e.toString());
        }

        return null;
    }   

 public int insertPunch(Punch p) {   
        
        String badgeID = p.getBadgeid();
        int terminalID = p.getTerminalid();
        int punchTypeID = p.getPunchtypeid();
        int punchid = 1;
        
        GregorianCalendar originalts = new GregorianCalendar();
        originalts.setTimeInMillis(p.getOriginaltimestamp());
        String originaltimestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(originalts.getTime());

        PreparedStatement pst;
        String query;
        ResultSet results;
        
        try {
     
            query = "INSERT INTO punch (terminalid, badgeid, originaltimestamp, punchtypeid) VALUES (?, ?, ?, ?)";
  
            pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setInt(1, terminalID);
            pst.setString(2, badgeID);
            pst.setString(3, originaltimestamp);
            pst.setInt(4, punchTypeID);
            
                        
            pst.execute();
            results = pst.getGeneratedKeys();
            results.first();
            return results.getInt(1);
   
        }
        catch (Exception e) {
            System.err.println("** insertPunch: " + e.toString());
        }
        
        return punchid;
    }
     

   public ArrayList<Punch> getDailyPunchList(Badge badge, long ts){
        
        //Santoshi 
         
        
    } 
     
  
   public void close() {
        
        try{
            
            conn.close();
        }
        catch(Exception e){
            System.err.println("** close: " + e.toString());
        }
    }
}
    
