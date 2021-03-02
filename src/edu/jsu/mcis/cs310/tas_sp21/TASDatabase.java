
package edu.jsu.mcis.cs310.tas_sp21;

import java.sql.*;
import java.util.*;


public class TASDatabase {
    
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query; 
        boolean theResults;
        
        int resultCount;
        int colCount = 0;   //column count
        
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
       
          
    }
   

/**
 * Method for retrieving information about a single punch from the database.This method should then query the database, 
 retrieve the punch data from the database, populate a new Punch object,
 and then return this object to the caller.
     * @param punchID
     * @return 
 */

    public Punch getPunch(int punchID) throws SQLException {
       int id;
       int terminalid;
       String badgeid;
       long originaltimestamp;
       int punchtypeid;

       try {
               query = "SELECT * FROM punch WHERE id = ?";
               pstSelect = conn.prepareStatement(query);
               int punch = 0;
               pstSelect.setInt(1, punch);

                System.out.println("Submitting Query ...");

               theResults = pstSelect.execute();                
               resultset = pstSelect.getResultSet();
               metadata = resultset.getMetaData();
               colCount = metadata.getColumnCount();

               System.out.println("Getting Results ...");

               while ( theResults || pstSelect.getUpdateCount() != -1 ) 
               {
                   if ( theResults ) 
                   {  
                       resultset = pstSelect.getResultSet();

               while (resultset.next()){
                           id = resultset.getInt("id");
                           terminalid = resultset.getInt("terminalid");
                           badgeid = resultset.getString("badgeid");
                           originaltimestamp = resultset.getTimestamp("originaltimestamp").getTime();
                           punchtypeid = resultset.getInt("punchtypeid");
               }

           } 
                    else 
                   {
                       resultCount = pstSelect.getUpdateCount();  
                       if ( resultCount == -1 ) 
                       {
                           break;
                       }    
                   }
                   theResults = pstSelect.getMoreResults();
               }
           }
           catch (Exception e) {
               System.err.println(e.toString());   
           }
           finally {

               if (resultset != null) { try { resultset.close(); resultset = null; 
               } catch (Exception e) {} }

               if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; 
               } catch (Exception e) {} }

               if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; 
               } catch (Exception e) {} }

           }

           return null;        
    }
    
 
 /**
  * Method creating objects of the Badge class.
  * @param badgeId
  * @return
  * @throws Exception 
  */
 
    public Badge getBadge(String badgeId) throws Exception {
        String id;
        String description;
        try
           {        
                query = "SELECT * FROM badge WHERE id = ?";
                pstSelect = conn.prepareStatement(query);
                String badge = null;
                pstSelect.setString(1, badge);

                System.out.println("Submitting Query ...");
                
                theResults = pstSelect.execute();                
                resultset = pstSelect.getResultSet();
                metadata = resultset.getMetaData();
                colCount = metadata.getColumnCount(); 
                
                 System.out.println("Getting Results ...");
                
            while ( theResults || pstSelect.getUpdateCount() != -1 ) 
            {
                if ( theResults ) 
                {       
                    resultset = pstSelect.getResultSet();
                    
                    while(resultset.next()) 
                        {
                            id = resultset.getString(1);
                            description = resultset.getString(2);
                        }
                }
                
                else 
                    {
                        resultCount = pstSelect.getUpdateCount();  
                        if ( resultCount == -1 ) 
                        {
                            break;
                        }    
                    }

                  theResults = pstSelect.getMoreResults();
            }
        }
        catch (Exception e) {
            System.err.println(e.toString());   
        }
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; 
            } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; 
            } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; 
            } catch (Exception e) {} }
            
        }
            return null;
         }  
        
 /**
  * 
  */
    /* public Shift getShift(int shift)
    {
        
        
        
        
    }*/
    
    
  
    }



