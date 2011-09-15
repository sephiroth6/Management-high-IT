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
public class Repair {
    
    private int id_c;
    private int id_d;
    private String date_in;
    private String date_out;
    private int state;
    private String optional;
    
    // useful when inserting a new case into database
    public Repair (Customer c, Device d, String o) {
        
        // date_in and state have a default value, date_out is not known
        this.id_c = c.getID();
        this.id_d = d.getID();
        this.optional = o;
        
    }
    
    public String getDateIn () {
        
        return this.date_in;
        
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
        s.setString(3, this.optional.toUpperCase());
        
        // execute query
        s.execute();
  
    }
    
    // TODO search repair through device
    // TODO edit repair for date_out and state
    
    // search repair through customer
    public static ResultSet search (Connection c, Customer x) throws SQLException {
        
        StringBuilder q = new StringBuilder("SELECT");
        // customer's informations that have to be retrieved
        q.append("customer.name, customer.surname, customer.address ");
        // repair's informations that have to be retrieved
        q.append("repair.date_in, repair.date_out, repair.optional, repair.status ");
        // device's informations that have to be retrieved
        q.append("device.prodcer, device.model ");
        // inner join
        q.append("FROM customer INNER JOIN repair INNER JOIN device ");
        // join conditions
        q.append("ON customer.id = repair.id_c AND repair.id_d = device.id AND customer.id = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, x.getID());
        
        return s.executeQuery();
        // TODO results management
    }
    
}
