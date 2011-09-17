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
public class Repair {
    
    private int id_c;
    private int id_d;
    private String in;
    private String out;
    private int status;
    private String optional;
    
    // useful when inserting a new case into database
    public Repair (Customer c, Device d, String o) {
        
        // date_in and state have a default value, date_out is not known
        this.id_c = c.getID();
        this.id_d = d.getID();
        this.optional = o.toUpperCase();
        
    }
    
    // useful when retrieving data from database
    public Repair (int c, int d, String in, String out, int s, String o) {
        
        this.id_c = c;
        this.id_d = d;
        this.in = in;
        this.out = out;
        this.status = s;
        this.optional = o;
        
    }
    
    // Setters
    
    public void setCusID (int i) {
        
        this.id_c = i;
        
    }
    
    public void setDevID (int i) {
        
        this.id_d = i;
        
    }
    
    public void setStatus (int s) {
        
        this.status = s;
        
    }
    
    public void setOptional (String o) {
        
        this.optional = o;
        
    }
    
    // end Setters
    // Getters
    
    public int getCusID () {
        
        return this.id_c;
        
    }
    
    public int getDevID () {
        
        return this.id_d;
        
    }
    
    public String getDateIn () {
        
        return this.in;
        
    }
    
    public String getDateOut () {
        
        return this.out;
        
    }
    
    public int getStatus () {
        
        return this.status;
        
    }
    
    public String getOptional () {
        
        return this.optional;
        
    }
    
    // end Getters
    
    @Override
    public String toString () {
        
        StringBuilder ret = new StringBuilder(this.id_c);
        ret.append("\n");
        ret.append(this.id_d);
        ret.append("\n");
        ret.append(this.in.toString());
        ret.append("\n");
        ret.append(this.out);
        ret.append("\n");
        ret.append(this.optional);
        ret.append("\n");
        ret.append(this.status);
        
        return new String(ret);
        
    }
    
    public void dbInsert (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.INS);
        q.append("repair(id_c, id_d, optional)");
        q.append(Constants.VAL);
        q.append("?, ?, ?);");
        
        // statement to execute
        PreparedStatement s = c.prepareStatement(new String(q));
        
        // bind values
        s.setInt(1, this.id_c);
        s.setInt(2, this.id_d);
        s.setString(3, this.optional);
        
        // execute query
        s.execute();
  
    }
    
    // START - PRIVATE METHODS FOR REPAIRS' EDITING
    
    private PreparedStatement modifyStatus (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("repair");
        q.append(Constants.SET);
        q.append("status = ?");
        q.append(Constants.W);
        q.append("id_c = ? AND id_d = ? AND date_in = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setInt(1, this.status);
        r.setInt(2, this.id_c);
        r.setInt(3, this.id_d);
        r.setString(4, this.in);
        
        return r;
           
     }
    
    private PreparedStatement modifyOptional (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("repair");
        q.append(Constants.SET);
        q.append("optional = ?");
        q.append(Constants.W);
        q.append("id_c = ? AND id_d = ? AND date_in = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.optional);
        r.setInt(2, this.id_c);
        r.setInt(3, this.id_d);
        r.setString(4, this.in);
        
        return r;
           
     }
    
    // END - PRIVATE METHODS FOR REPAIRS' EDITING
    
    // can edit work status and optional provided with the device
    public void edit (Connection c, Repair r) throws SQLException {
        // call it on the object that has to be modified
        // Repair r is the object with the new informations
        
        ArrayList<PreparedStatement> to = new ArrayList<PreparedStatement>(2);
        
        if(!(this.status == r.status)) {
            
            this.status = r.status;
            to.add(this.modifyStatus(c));
            
        }
        
        if(!(this.optional.equals(r.optional))) {
            
            this.optional = r.optional;
            to.add(this.modifyOptional(c));
            
        }
            
        c.setAutoCommit(false);
         
         for(PreparedStatement q : to)
             q.execute();
         
         c.setAutoCommit(true);
        
    }
    
    // TODO search repair through device; set date_out (device exits)
    
    // search repair through customer
    public static ResultSet search (Connection c, Customer x) throws SQLException {
        
        StringBuilder q = new StringBuilder("SELECT ");
        // customer's informations that have to be retrieved
        q.append("customer.id, customer.name, customer.surname, customer.address, customer.tel, ");
        // repair's informations that have to be retrieved
        q.append("repair.date_in, repair.date_out, repair.status, repair.optional, ");
        // device's informations that have to be retrieved
        q.append("device.id, device.producer, device.model, device.type, device.imei, device.serial ");
        // inner join
        q.append("FROM customer INNER JOIN repair INNER JOIN device ");
        // join conditions
        q.append("ON customer.id = repair.id_c AND repair.id_d = device.id AND customer.id = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, x.getID());
        
        return s.executeQuery();
        
    }
    
    
    
}
