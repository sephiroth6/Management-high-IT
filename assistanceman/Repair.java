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
    
    private int id;
    private int id_c;
    private int id_d;
    private String in;
    private String out;
    private int status;
    private String optional;
    
    // useful when inserting a new case into database
    public Repair (Customer c, Device d, String o) {
        
        // date_in and state have a default value, date_out is not known
        if(c != null)
            this.id_c = c.getID();
        
        if(d != null)
            this.id_d = d.getID();
        
        if(o != null)
            this.optional = o.toUpperCase();
        
    }
    
    // useful when retrieving data from database
    public Repair (int i, int c, int d, String in, String out, int s, String o) { 
        
        this.id = i;
        this.id_c = c;
        this.id_d = d;
        this.in = in;
        this.out = out;
        this.status = s;
        this.optional = o;
        
    }
        
    // START - SETTERS
    
    public void setID (int i) {
        
        this.id = i;
        
    }
    
    // START - maybe not so useful
    
    public void setCusID (int i) {
        
        this.id_c = i;
        
    }
    
    public void setDevID (int i) {
        
        this.id_d = i;
        
    }
    
    // END - maybe not so useful
    
    public void setStatus (int s) {
        
        this.status = s;
        
    }
    
    public void setOptional (String o) {
        
        this.optional = o;
        
    }
    
    // END - SETTERS
    
    // START - GETTERS
    
    public int getID () {
        
        return this.id;
        
    }
    
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
    
    // END - GETTERS
    
    @Override
    public String toString () {
        
        StringBuilder ret = new StringBuilder(Integer.toString(this.id));
        ret.append(" - ");
        ret.append(this.id_c);
        ret.append(" - ");
        ret.append(this.id_d);
        ret.append(" - ingresso: ");
        ret.append(Utils.formatDate(this.in));
        ret.append(" - uscita: ");
        ret.append(Utils.formatDate(this.out));
        ret.append(" - accessori in consegna: ");
        ret.append(this.optional);
        ret.append(" - stato riparazione: ");
        ret.append(this.status);
        
        return new String(ret);
        
    }
    
    public int dbInsert (Connection c) throws SQLException {
        
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
    
    // START - PRIVATE METHODS FOR REPAIRS' EDITING
    
    private PreparedStatement modifyStatus (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("repair");
        q.append(Constants.SET);
        q.append("status = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setInt(1, this.status);
        r.setInt(2, this.id);
        
        return r;
           
     }
    
    private PreparedStatement modifyOptional (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("repair");
        q.append(Constants.SET);
        q.append("optional = ?");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement r = c.prepareStatement(new String(q));
        r.setString(1, this.optional);
        r.setInt(2, this.id);
        
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
    
    // searches a repair: it's possible to specify name, surname, repair's id or device's s/n
    public static ResultSet search (Connection c, Customer x, Repair y, Device z) throws SQLException {
        // pre-condition: in the GUI will be inserted one field or more
        boolean isName = false, isSurname = false, isID = false, isSerial = false, isFirst = true;
        int id = 0, set = 1;
        String name = null, surname = null, serial = null;
        PreparedStatement s = null;

        if(x != null) {
            name = x.getName();
            surname = x.getSurname();
        }
        
        if(y != null)
            id = y.getID();
        
        if(z != null)
            serial = z.getSerial();
        
        if(name != null)
            isName = true;
        
        if(surname != null)
            isSurname = true;
        
        if(id != 0)
            isID = true;
        
        if(serial != null)
            isSerial = true;
        
        StringBuilder q = new StringBuilder("SELECT ");
        // TODO customer's informations that have to be retrieved; select customer's note?
        q.append("customer.id, customer.name, customer.surname, customer.address, customer.tel, ");
        // repair's informations that have to be retrieved
        q.append("repair.id, repair.date_in, repair.date_out, repair.status, repair.optional, ");
        // device's informations that have to be retrieved
        q.append("device.id, device.producer, device.model, device.type, device.imei, device.serial ");
        // inner join
        q.append("FROM customer INNER JOIN repair INNER JOIN device ");
        // join conditions
        q.append("ON customer.id = repair.id_c AND repair.id_d = device.id AND ");
        // info provided by the user
        
        if(isName) {
            // name provided
            q.append(" customer.name = ?");
            isFirst = false;
        }
        
        if(isSurname) {
            // surname provided
            if(!isFirst) {
                // if a name was provided, add AND
                q.append(" AND");
                isFirst = false;
            }
            q.append(" customer.surname = ?");
        }
        
        if(isID) {
            // repair's id provided
            if(!isFirst) {
                q.append(" AND");
                isFirst = false;
            }
            q.append(" repair.id = ?");
        }
        
        if(isSerial) {
            // serial (or imei) provided
            if(!isFirst) {
                q.append(" AND");
                isFirst = false;
            }
            
            if(z.getType() == Constants.MOBILE)
                q.append(" device.imei = ?");
            else
                q.append(" device.serial = ?");
        }
        
        q.append(";");
        
        s = c.prepareStatement(new String(q));
        
        // variable set is the index of the variable into the query
        if(isName) {
            s.setString(set, name);
            set++;
        }
        
        if(isSurname) {
            s.setString(set, surname);
            set++;
        }
        
        if(isID) {
            s.setInt(set, id);
            set++;
        }
        
        if(isSerial) {
            s.setString(set, serial);
            set++;
        }
        
        return s.executeQuery();
        
    }
    
    // search repair through customer
    public static ResultSet search (Connection c, Customer x) throws SQLException {
        
        StringBuilder q = new StringBuilder("SELECT ");
        // customer's informations that have to be retrieved
        q.append("customer.id, customer.name, customer.surname, customer.address, customer.tel, ");
        // repair's informations that have to be retrieved
        q.append("repair.id, repair.date_in, repair.date_out, repair.status, repair.optional, ");
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
    
    // sets date_out for a repair
    public void deviceOut (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.UP);
        q.append("repair");
        q.append(Constants.SET);
        // happening in this moment
        q.append("date_out = CURRENT_TIMESTAMP");
        q.append(Constants.W);
        q.append("id = ?;");
        
        PreparedStatement s = c.prepareStatement(new String(q));
        
        s.setInt(1, this.id);
        s.execute();
        
    }
    
    
}
