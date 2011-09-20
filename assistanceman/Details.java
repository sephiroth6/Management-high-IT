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
    
    private int id_c;
    private int id_d;
    private String in;
    private String start;
    private String declared;        // defect declared
    private String found;           // defect found
    private double spare_price;     // price of the spare parts
    private double work_price;
    private String note;
    
    // useful when creating new repair's details
    // (called when accepting a device)
    public Details (Customer c, Device dev, Repair r, String dec, String n) {
        
        this.id_c = c.getID();
        this.id_d = dev.getID();
        this.in = r.getDateIn();
        this.declared = dec.toUpperCase();
        this.note = n.toUpperCase();
        
    }
    
    // useful when retrieving informations from database
    public Details (ResultSet r) throws SQLException {
        
        while (r.next()) {
            
            this.id_c = r.getInt(1);
            this.id_d = r.getInt(2);
            this.in = r.getString(3);
            this.start = r.getString(4);
            this.declared = r.getString(5);
            this.found = r.getString(6);
            this.spare_price = r.getDouble(7);
            this.work_price = r.getDouble(8);
            this.note = r.getString(9);
            
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
    
    public int getCusID () {
        
        return this.id_c;
        
    }
    
    public int getDevID () {
        
        return this.id_d;
        
    }
    
    public String getDateIn () {
        
        return this.in;
        
    }
    
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
        
        StringBuilder ret = new StringBuilder(Integer.toString(this.id_c));
        ret.append("\n");
        ret.append(Integer.toString(this.id_d));
        ret.append("\n");
        ret.append(this.in);
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
        q.append("details(id_c, id_d, date_in, declared, note)");
        q.append(Constants.VAL);
        q.append("?, ?, ?, ?, ?);");
        
        // statement to execute
        PreparedStatement s = c.prepareStatement(new String(q));
        
        // bind values
        s.setInt(1, this.id_c);
        s.setInt(2, this.id_d);
        s.setString(3, this.in);
        s.setString(4, this.declared);
        s.setString(5, this.note);
        
        s.execute();
        
    }
    
    // searches the details about a given repair
    public ResultSet search (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.SEL);
        q.append("details");
        q.append(Constants.W);
        q.append("id_c = ? AND id_d = ? AND date_in = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, id_c);
        s.setInt(2, id_d);
        s.setString(3, in);
        
        return s.executeQuery();
        
    }
    
}
