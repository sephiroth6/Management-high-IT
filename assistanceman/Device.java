/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Claudio
 */
public class Device {
    
    private int id;
    private String producer;
    private String model;
    private int type;
    private String IMEI;
    private String serial;
    
    // useful when creating a new device
    public Device (String p, String m, int t, String n) {
        
        this.producer = p;
        this.model = m;
        this.type = t;
        
        if(t == Constants.MOBILE)
            this.IMEI = n;
        else
            this.serial = n;
        
    }
    
    // useful when interacting with database
    public Device (int i, String p, String m, int t, String n) {
        
        this(p, m, t, n);
        this.id = i;
        
    }
    
    // TODO setID needed
    public void setID (int i) {
        
        this.id = i;
        
    }
    
    
    public int getID() {
        
        return this.id;
        
    }
    
    public int getType() {
        
        return this.type;
        
    }
    
    // gives the imei or the serial number, according to the type of device
    public String getSerial () {
        
        if(this.type == Constants.MOBILE)
            return this.IMEI;
        
        return this.serial;
        
    }
    
    @Override
    public String toString() {
        
        StringBuilder ret = new StringBuilder(Integer.toString(this.id));
        ret.append(" - ");
        
        if(this.type == Constants.MOBILE)
            ret.append("TELEFONO");
        else if(this.type == Constants.COM)
            ret.append("COMPUTER");
        else
            ret.append("ALTRO");
        
        ret.append(" - ");
        ret.append(this.producer);
        ret.append(" ");
        ret.append(this.model);
        ret.append(" - ");
        ret.append(this.getSerial());
        
        return new String(ret);
        
    }
    
    // database methods
    // TODO search into device table? I don't think it's necessary
    
    public int dbInsert (Connection c) throws SQLException {
        
        StringBuilder q = new StringBuilder(Constants.INS);
        String code;
        
        if(this.type == Constants.MOBILE) {
            q.append("device(producer, model, type, imei)");
            code = this.IMEI;
        } else {
            q.append("device(producer, model, type, serial)");
            code = this.serial;
        }
            
        q.append(Constants.VAL);
        q.append("?, ?, ?, ?);");
        
        // statement to execute
        PreparedStatement s = c.prepareStatement(new String(q));
        
        // bind values
        s.setString(1, this.producer.toUpperCase());
        s.setString(2, this.model.toUpperCase());
        s.setInt(3, this.type);    
        s.setString(4, code.toUpperCase());
        
        // execute query
        try {
            
            s.execute();
            // if it's all ok, returns the id of the inserted row
            return s.getGeneratedKeys().getInt(1);
            
        } catch (SQLException e) {
            
            // if the device already exists, returns 0
            if(e.getMessage().contains(Constants.EXC_US))
                return 0;
            else    // another exception occourred
                return -1;
            
        }
        
    }
    
}
