/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Claudio
 */
public class Warehouse {
    
    private String serial;
    private String name;
    private int available;
    private double unit_price;
    private String note;
    
    @Override
    public String toString () {
        
        if(this.serial != null) {
            // returns all informations
            StringBuilder ret = new StringBuilder("numero di serie: ");
            ret.append(this.serial);
            ret.append(" - nome: ");
            ret.append(this.name);
            ret.append(" - disponibilità: ");
            ret.append(Integer.toString(this.available));
            ret.append(" - prezzo unitario: ");
            ret.append(Double.toString(this.unit_price));
            ret.append(" - note: ");
            ret.append(this.note);

            return new String(ret);
        
        } else {
            // serial number not found when retrieving from database
            return "numero di serie inesistente";
            
        }
        
    }
    
    // useful when creating a record for a new piece
    public Warehouse (String s, String na, int a, double u, String no) {
        
        this.serial = s.toUpperCase();
        this.name = na.toUpperCase();
        this.available = a;
        this.unit_price = u;
        this.note = no.toUpperCase();
        
    }
    
    // useful when retrieving info from database
    public Warehouse (ResultSet r) throws SQLException {
        
        if(r != null) {     // r can be null (see search method)
                            // TODO probably it can be modified checking it before call constructor
        
            this.serial = r.getString(1);
            this.name = r.getString(2);
            this.available = r.getInt(3);
            this.unit_price = r.getDouble(4);
            this.note = r.getString(5);
        
        }
        
    }
    
    // insert into database
    public int dbInsert (Connection c) throws SQLException {
        
        PreparedStatement s = c.prepareStatement("INSERT INTO warehouse VALUES (?, ?, ?, ?, ?);");
        
        s.setString(1, this.serial);
        s.setString(2, this.name);
        s.setInt(3, this.available);
        s.setDouble(4, this.unit_price);
        s.setString(5, this.note);
        
        try {
            // insert the new record
            s.execute();
            return 0;   // ok
            
        } catch (SQLException e) {
            // something wrong's happened
            if(e.getMessage().contains(Constants.EXC_US))   // already exists
                return 1;
            else    // other problem
                return -1;
            
        }
        
    }
    
    // search spare parts through serial
    public ResultSet search (Connection c) throws SQLException {
        
        PreparedStatement s;
        ResultSet ret;
        StringBuilder q = new StringBuilder(Constants.SEL);
        q.append("warehouse");
        q.append(Constants.W);
        q.append("serial = ?;");
        
        s = c.prepareStatement(new String(q));
        
        s.setString(1, this.serial);
  
        try {
            
            ret = s.executeQuery();
        
            if(ret.next())  // if there is result, ok
                return ret;
            else            // if there is no piece with that serial, return null
                return null;
        
        } catch (SQLException e) {
            
            return null;    // in case of exception returns null too
                            // probably not good but I wanted to catch exception
        }
    
    }
    
    // delete a spare part from warehouse
    public int delete (Connection c) throws SQLException {
        
        PreparedStatement s = c.prepareStatement("DELETE FROM warehouse WHERE serial = ?;");
        
        s.setString(1, this.serial);
        
        try {
            
            s.execute();
            return 0;   // ok
            
        } catch (SQLException e) {
            
            return 1;   // something wrong happened
            
        }
        
    }
    
    // START - PRIVATE METHODS NEEDED TO EDIT A RECORD
    // set the availability for an existing spare part
    private PreparedStatement editAvailability (Connection c) throws SQLException {
        
        PreparedStatement s;
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("warehouse");
        q.append(Constants.SET);
        q.append("available = ?");
        q.append(Constants.W);
        q.append("serial = ?");
        
        s = c.prepareStatement(new String(q));
        s.setInt(1, this.available);    // the object contains the new value
        s.setString(2, this.serial);    // the serial number of spart part to be modified
        
        return s;
     
    }
    
    // set notes for an existing spare part
    private PreparedStatement editNote (Connection c) throws SQLException {
        
        PreparedStatement s;
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("warehouse");
        q.append(Constants.SET);
        q.append("note = ?");
        q.append(Constants.W);
        q.append("serial = ?");
        
        s = c.prepareStatement(new String(q));
        s.setString(1, this.note);      // the object contains the new value
        s.setString(2, this.serial);    // the serial number of spart part to be modified
        
        return s;
     
    }
    // END - PRIVATE METHODS NEEDED TO EDIT A RECORD
    
    // TODO edit all fields
    public int edit (Connection c, Warehouse w) throws SQLException {
        // call it on the object that has to be modified
        // Warehouse w is the object with the new informations
        
        ArrayList<PreparedStatement> to = new ArrayList<PreparedStatement>(5);
        
        if(this.available != w.available) {
            // availability has to be modified
            this.available = w.available;
            to.add(this.editAvailability(c));
        }
        
        if(!this.note.equals(w.note)) {
            // note field has to be modified
            this.note = w.note;
            to.add(this.editNote(c));
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
   
    // TODO write this method
    public void increaseAvailability (Connection c, int i) throws SQLException {
        // no control needed to increase availability
    }
    
    // TODO write this method
    public void decreaseAvailability (Connection c, int i) throws SQLException {
        // control if is possible to decrease availability (i <= available)
    }
    
}
