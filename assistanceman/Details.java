/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Claudio
 */
public class Details {
    
    private int rep_id;
    private String start;
    private String declared;        // defect declared
    private String found;           // defect found
    private double spare_price;     // price of the spare parts
    private double work_price;
    private String note;
    
    // useful when creating new repair's details
    // (called when accepting a device)
    public Details (Repair r, String dec, String n) {
    
        this.rep_id = r.getID();
        this.declared = dec.toUpperCase();
        this.note = n.toUpperCase();
        
    }
    
    // useful when retrieving informations from database
    public Details (ResultSet r) throws SQLException {
        
        while (r.next()) {
            
            this.rep_id = r.getInt(1);
            this.start = r.getString(2);
            this.declared = r.getString(3);
            this.found = r.getString(4);
            this.spare_price = r.getDouble(5);
            this.work_price = r.getDouble(6);
            this.note = r.getString(7);
            
        }
        
    }
    
    // SETTERS
    
    public void setDeclared (String d) {
        
        this.declared = d;
        
    }
    
    public void setFound (String f) {
        
        this.found = f;
        
    }
    
    public void setSpare (double p) {
        
        this.spare_price = p;
        
    }
    
    public void setWork (double p) {
        
        this.work_price = p;
        
    }
    
    // END SETTERS
    // GETTERS
    
    public String getDateStart () {
        
        return this.start;
        
    }
    
    public String getDeclared () {
        
        return this.declared;
        
    }
    
    public String getFound () {
        
        return this.found;
        
    }
    
    public double getSpare () {
        
        return this.spare_price;
        
    }
    
    public double getWork () {
        
        return this.work_price;
        
    }
    
    public String getNote () {
        
        return this.note;
        
    }
    
    // END GETTERS
    
    @Override
    public String toString () {
        
        StringBuilder ret = new StringBuilder(Integer.toString(this.rep_id));
        ret.append("\n");
        ret.append(this.start);
        ret.append("\n");
        ret.append(this.declared);
        ret.append("\n");
        ret.append(this.found);
        ret.append("\n");
        ret.append(this.spare_price);
        ret.append("\n");
        ret.append(this.work_price);
        ret.append("\n");
        ret.append(this.note);
        ret.append("\n");
        
        return new String(ret);
        
    }
    
    public void dbInsert (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.INS);
        q.append("details(rep_id, declared, note)");
        q.append(Constants.VAL);
        q.append("?, ?, ?);");
        
        // statement to execute
        PreparedStatement s = c.prepareStatement(new String(q));
        
        // bind values
        s.setInt(1, this.rep_id);
        s.setString(2, this.declared);
        s.setString(3, this.note);
        
        s.execute();
        
    }
    
    // searches the details about a given repair
    // id_c, id_d and date_in are the primary key for the details
    public ResultSet search (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.SEL);
        q.append("details");
        q.append(Constants.W);
        q.append("rep_id = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, this.rep_id);
        return s.executeQuery();
        
    }
    
    // TODO edit start date of repair, prices and notes
    public void setStart (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("details");
        q.append(Constants.SET);
        q.append("date_start = CURRENT_TIMESTAMP");
        q.append(Constants.W);
        q.append("rep_id = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, this.rep_id);
        
        s.execute();
        
    }
    
}
