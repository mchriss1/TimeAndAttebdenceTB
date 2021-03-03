 package edu.jsu.mcis.cs310.tas_sp21;

import java.sql.*;
 
import java.util.*;


public class TASDatabase {
    
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        
public TASDatabase(){
    
          try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/tas");
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
        Badge punchBadge = new Badge(resultset.getString(3), "Description");
        int punchTypeID = resultset.getInt(5);
        
        Punch p = new Punch(punchBadge, punchTerminalID, punchTypeID);
        
        // prepare statement to get timestamp
        pstSelect = conn.prepareStatement("SELECT *, UNIX_TIMESTAMP(originaltimestamp) * 1000 AS ts FROM punch WHERE id = ?");
        
        //set params
        pstSelect.setInt(1, punchid);
        
        //execute
        pstSelect.execute();
        resultset = pstSelect.getResultSet();
        
        //get results
        resultset.first();
        long punchOriginalTimestamp = resultset.getLong("ts");
        
        p.setOriginaltimestamp(punchOriginalTimestamp);
        
        
        return p;
        }
        
        catch(Exception e){
            System.err.println("** getPunch: " + e.toString());
        }

        return null;
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
}

    
    
     
  
    
  
    
