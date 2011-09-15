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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Claudio
 */
public class Customer {
    
    private int id;
    private String name;
    private String surname;
    private String address;
    private String tel;
    private String note;
    
    // useful when creating a new customer
    public Customer (String na, String s, String a, String t, String no) {
        // every string to upper case, to simplify the management of informations
        this.name = na.toUpperCase();
        this.surname = s.toUpperCase();
        this.address = a.toUpperCase();
        this.tel = t;
        this.note = no.toUpperCase();
        
    }
    
    // useful when a customer is being modified
    public Customer (int id, String na, String s, String a, String t, String no) {
        
        this(na, s, a, t, no);
        // when write on database, specifify WHERE id = this.id
        this.id = id;
        
    }
    
    // useful to search into database
    public Customer (String n, String s) {
        
        this.name = n.toUpperCase();
        this.surname = s.toUpperCase();
        
    }
    
    // TODO getters and setters are useful? maybe getters more
    
    // setters
    public void setID (int i) {
        
        this.id = i;
        
    }
    
    public void setName (String s) {
        // TODO exception if numbers are entered?
        this.name = s;
        
    }
    
    public void setSurname (String s) {
        // TODO exception if numbers are entered?
        this.surname = s;
        
    }
    
    public void setAddress (String s) {
        // no control needed
        this.address = s;
        
    }
    
    public void setTelephone (String s) {
        // TODO exception if letters are entered?
        if(!StringUtils.isAlpha(s))
            this.tel = s;
        
    }
    
    public void setNote (String s) {
        // no control needed
        this.note = s;
        
    }
    
    // getters
    public int getID () {
        
        return this.id;
        
    }
    
    public String getName () {
        
        return this.name;
        
    }
    
    public String getSurname () {
        
        return this.surname;
        
    }
    
    public String getAddress () {
        
        return this.address;
        
    }
    
    public String getTelephone () {
        
        return this.tel;
        
    }
    
    public String getNote () {
        
        return this.note;
       
    }
    
    // TODO toString can be useful or not?
    @Override
    public String toString () {
        
        StringBuilder ret = new StringBuilder(this.name);
        ret.append("\n");
        ret.append(this.surname);
        ret.append("\n");
        ret.append(this.address);
        ret.append("\n");
        ret.append(this.tel);
        ret.append("\n");
        ret.append(this.note);
        
        return new String(ret);
        
    }
    
    // database methods

    public int dbInsert (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.INS);
        q.append("customer(name, surname, address, tel, note)");
        q.append(Constants.VAL);
        q.append("?, ?, ?, ?, ?);");
        
        // statement to execute
        PreparedStatement s = c.prepareStatement(new String(q));
        
        // bind values
        s.setString(1, this.name.toUpperCase());
        s.setString(2, this.surname.toUpperCase());
        s.setString(3, this.address.toUpperCase());
        s.setString(4, this.tel.toUpperCase());
        s.setString(5, this.note.toUpperCase());
        
        // execute query
        try {
            
            s.execute();
            // if it's all ok, returns the id of the inserted row
            return s.getGeneratedKeys().getInt(1);
            
        } catch (SQLException e) {
            
            // if the customer already exists, returns 0
            if(e.getMessage().contains(Constants.EXC_UM))
                return 0;
            else    // another exception occourred
                return -1;
            
        }
        
    }
    
    // search a customer with a specific name and/or surname
    public ResultSet search (Connection c) throws SQLException {
        
        ResultSet r;
        PreparedStatement s = c.prepareStatement("SELECT * FROM customer;");
        StringBuilder q = new StringBuilder(Constants.SEL);
        q.append("customer WHERE ");
        
        if(!this.name.equals("")) {
            // name provided
            q.append("name = ?");
            
            if(!this.surname.equals("")) {
                // surname too
                q.append("AND surname = ?;");
                s = c.prepareStatement(new String(q));
                s.setString(1, this.name.toUpperCase());
                s.setString(2, this.surname.toUpperCase());
            } else {
                // name only
                q.append(";");
                s = c.prepareStatement(new String(q));
                s.setString(1, this.name.toUpperCase());
            }
                
        } else {
            
            if(!this.surname.equals("")) {
                // surname only
                q.append("surname = ?;");
                s = c.prepareStatement(new String(q));
                s.setString(1, this.surname.toUpperCase());
            }
        
        }
        
        // returns the result of the search as a ResultSet
        r = s.executeQuery();
        
        return r;
            
    }
    
    // manages the ResultSet of Customer objects, creating an ArrayList
    // containing the found customers
    public static ArrayList<Customer> results (ResultSet r) throws SQLException {
        
        ArrayList<Customer> ret = new ArrayList<Customer>();
        
        while(r.next()) {
            
            Customer c = new Customer(r.getString("name"), r.getString("surname"), r.getString("address"), r.getString("tel"), r.getString("note"));
            ret.add(c);
            
        }
        
        return ret;
        
    }
    
    // START - PRIVATE METHODS FOR CUSTOMERS' EDITING
    // creates the query to modify the customer's name
    private PreparedStatement modifyName (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("customer");
        q.append(Constants.SET);
        q.append("name = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.name.toUpperCase());
        r.setInt(2, this.id);
        
        return r;
           
    }
     
    // creates the query to modify the customer's surname
     private PreparedStatement modifySurname (Connection c) throws SQLException {
         
         StringBuilder q = new StringBuilder(Constants.UP);
         q.append("customer");
         q.append(Constants.SET);
         q.append("surname = ?");
         q.append(Constants.W);
         q.append("id = ?");
         
         PreparedStatement r = c.prepareStatement(new String(q));
         r.setString(1, this.surname.toUpperCase());
         r.setInt(2, this.id);
        
        return r;
         
     }
    
     // creates the query to modify the customer's addresss
     private PreparedStatement modifyAddress (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("customer");
        q.append(Constants.SET);
        q.append("address = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.address.toUpperCase());
        r.setInt(2, this.id);
        
        return r;
           
    }
     
     // creates the query to modify the customer's telephone number
     private PreparedStatement modifyTelephone (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("customer");
        q.append(Constants.SET);
        q.append("tel = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.tel);
        r.setInt(2, this.id);
        
        return r;
           
    }
     
     // creates the query to modify the notes about a customer
     private PreparedStatement modifyNote (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("customer");
        q.append(Constants.SET);
        q.append("note = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.note.toUpperCase());
        r.setInt(2, this.id);
        
        return r;
           
     }
     // END - PRIVATE METHODS FOR CUSTOMERS' EDITING
     
     // executes the queries needed to edit a customer
     public void edit (Connection x, Customer c) throws SQLException {
         // call it on the object that has to be modified
         // Customer c is the object with the new informations
         
         ArrayList<PreparedStatement> to = new ArrayList<PreparedStatement>(5);
         
         if(!this.name.equals(c.name)) {
             // name must be modified
             this.name = c.name;
             to.add(this.modifyName(x));
         }
         
         if(!this.surname.equals(c.surname)) {
             // surname must be modified
             this.surname = c.surname;
             to.add(this.modifySurname(x));
         }
         
         if(!this.address.equals(c.address)) {
             // address must be modified
             this.address = c.address;
             to.add(this.modifyAddress(x));
         }
           
         if(!this.tel.equals(c.tel)) {
             // telephone number must be modified
             this.tel = c.tel;
             to.add(this.modifyTelephone(x));
         }
         
         if(!this.note.equals(c.note)) {
             // notes must be modified
             this.note = c.note;
             to.add(this.modifyNote(x));
         }
         
         x.setAutoCommit(false);
         
         for(PreparedStatement q : to)
             q.execute();
         
         x.setAutoCommit(true);
         
     }
     
}
