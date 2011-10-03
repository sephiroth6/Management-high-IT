/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Claudio
 */
public class Usage {
    
    private int rep_id;
    private String serial;
    private int used;
    
    // useful when inserting new usage info
    public Usage (Repair r, Warehouse w, int n) {
        
        this.rep_id = r.getID();
        this.serial = w.getSerial();
        this.used = n;
        
    }
    
    // useful when retrieving info from the database
    public Usage (ResultSet r) throws SQLException {
        
        if(r != null) {
            
            this.rep_id = r.getInt(1);
            this.serial = r.getString(2);
            this.used = r.getInt(3);
            
        }
        
    }
    
    // return the serial number of the spare part used in a repair
    public String getSerial () {
        
        return this.serial;
        
    }
    
    // return the number of a piece used in a repair
    public int getUsed () {
        
        return this.used;
        
    }
    
    // insert into database details about the usage of a spare part
    public int dbInsert (Connection c) throws SQLException {
        
        PreparedStatement s = null;
        StringBuilder q = new StringBuilder(Constants.INS);
        q.append("usage");
        q.append(Constants.VAL);
        q.append("?, ?, ?);");
        
        s = c.prepareStatement(new String(q));
        s.setInt(1, this.rep_id);
        s.setString(2, this.serial);
        s.setInt(3, this.used);
        
        try {
            // TODO check query result?
            s.execute();
            return 0;
            
        } catch (SQLException e) {
            
            // if a record already exists, returns 0
            if(e.getMessage().contains(Constants.EXC_US))
                return -1;
            else    // another exception occourred
                return 1;
            
        }
        
    }
    
    // search the spare parts usage for a specific repair
    public ResultSet search (Connection c) throws SQLException {
        
        PreparedStatement s = null;
        ResultSet ret = null;
        StringBuilder q = new StringBuilder(Constants.SEL);
        q.append("usage");
        q.append(Constants.W);
        q.append("rep_id = ?;");
        
        s = c.prepareStatement(new String(q));
        s.setInt(1, this.rep_id);
        
        try {
            
            ret = s.executeQuery();
        
            if(ret.next())      // if there is result, ok
                return ret;
            else                // if there is no piece with that serial, return null
                return null;
            
        } catch (SQLException e) {
            
            return null;        // something wrong happened, return null
            
        }
        
        
    }
 
    // START - PRIVATE METHODS FOR EDITING
    
    // edit the serial number of the spare part used in repair with specific id
    private PreparedStatement editSerial (Connection c, String sn) throws SQLException {
        
        PreparedStatement ret = null;
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("usage");
        q.append(Constants.SET);
        q.append("serial = ?");
        q.append(Constants.W);
        q.append("serial = ?;");
        
        ret = c.prepareStatement(new String(q));
        ret.setString(1, sn);
        ret.setString(2, this.serial);
        
        return ret;
        
    }
    
    // edit the number of unit used in repair with specific id
    private PreparedStatement editUsed (Connection c) throws SQLException {
        
        PreparedStatement ret = null;
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("usage");
        q.append(Constants.SET);
        q.append("used = ?");
        q.append(Constants.W);
        q.append("serial = ?");
        
        ret = c.prepareStatement(new String(q));
        ret.setInt(1, this.used);
        ret.setString(2, this.serial);
        
        return ret;
        
    }
    
    // END - PRIVATE METHODS FOR EDITING
    
    // edit a record for spare parts usage details
    public int edit (Connection c, Usage u) throws SQLException {
        // call it on the object that has to be modified
        // Usage u is the object with the new informations
        
        ArrayList<PreparedStatement> to = new ArrayList<PreparedStatement>(2);
        
        if(!this.serial.equals(u.serial)) {
            // edit serial number of the spare part used in this repair
            to.add(this.editSerial(c, u.serial));
            this.serial = u.serial;
        }
        
        if(this.used != u.used) {
            // edit number of the spare part used in this repair
            this.used = u.used;
            to.add(this.editUsed(c));
        }
        
        try {
         
             c.setAutoCommit(false);

             // TODO check if the update was successful counting rows updated? (executeQuery)
             for(PreparedStatement q : to)
                 q.execute();

             c.setAutoCommit(true);
             
             return 0;      // ok
             
         } catch (SQLException e) {
             
             return 1;      // something wrong happened
         }
        
    }

    // TODO write a delete method
    
}
