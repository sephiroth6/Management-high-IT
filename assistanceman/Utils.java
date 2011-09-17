/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Claudio
 */
public class Utils {
    
    // opens the connection with the database
    public static Connection dbConnection (String db) throws ClassNotFoundException, SQLException {
        
        Class.forName("org.sqlite.JDBC");
        
        Connection c = DriverManager.getConnection(db);
        
        return c;
        
    }
    
    // closes the connection with the database
    public static void closeConnection (Connection c) throws SQLException {
        
        c.close();
        
    }
    
    // creates the tables of the database
    public static void createTables (Connection c) throws SQLException {
        
        // new statement to execute
        Statement s = c.createStatement();
        
        // first query, customer
        StringBuilder q = new StringBuilder(Constants.CRTABLE);
        q.append("customer");
        q.append(Constants.ID);
        q.append("name TEXT, surname TEXT, address TEXT, tel TEXT, note TEXT,");
        // two users can't have the same name, surname e address combination
        q.append(Constants.UNI);
        q.append("name, surname, address));");
        
        // execution
        s.executeUpdate(new String(q));
        
        // second query, device
        q = new StringBuilder(Constants.CRTABLE);
        q.append("device");
        q.append(Constants.ID);
        q.append("producer TEXT, model TEXT, type INTEGER, imei TEXT UNIQUE, serial TEXT UNIQUE);");
        
        s.executeUpdate(new String(q));
        
        // third query, repair
        q = new StringBuilder(Constants.CRTABLE);
        q.append("repair (id_c INTEGER");
        q.append(Constants.REF);
        q.append("customer(id), id_d INTEGER");
        q.append(Constants.REF);
        q.append("device(id), date_in NUMERIC");
        q.append(Constants.DEF_DATE);
        q.append(", date_out NUMERIC, status INTEGER DEFAULT 0, optional TEXT,");
        q.append(Constants.PKEY);
        q.append("(id_c, id_d, date_in));");
        
        s.executeUpdate(new String(q));
        
        // fourth query, details of each repair
        q = new StringBuilder(Constants.CRTABLE);
        q.append("details (id_c INTEGER");
        q.append(Constants.REF);
        q.append("customer(id), id_d INTEGER");
        q.append(Constants.REF);
        q.append("device(id), date_in NUMERIC");
        q.append(Constants.REF);
        q.append("repair(date_in), date_start NUMERIC, declared TEXT, found TEXT, spare_price REAL, work_price REAL, note TEXT,");
        q.append(Constants.PKEY);
        q.append("(id_c, id_d, date_in));");
        
        s.executeUpdate(new String(q));
        
        // fifth query, warehouse with the spare parts
        q = new StringBuilder(Constants.CRTABLE);
        q.append("warehouse ");
        q.append("(serial TEXT PRIMARY KEY, name TEXT, available INTEGER, unit_price REAL, note TEXT);");
        
        s.executeUpdate(new String(q));
        
        // last query, usage: spare parts used in each repair
        q = new StringBuilder(Constants.CRTABLE);
        q.append("usage ");
        q.append("(id_c INTEGER");
        q.append(Constants.REF);
        q.append("customer(id), id_d INTEGER");
        q.append(Constants.REF);
        q.append("device(id), date_in NUMERIC");
        q.append(Constants.REF);
        q.append("repair(date_in), serial TEXT");
        q.append(Constants.REF);
        q.append("warehouse(serial), used INTEGER,");
        q.append(Constants.PKEY);
        q.append("(id_c, id_d, date_in, serial));");
        
        s.executeUpdate(new String(q));
        
    }
    
    // create objects from a searching repair query
    public static ArrayList<Object> repairResults (ResultSet r) throws SQLException {
        
        ArrayList<Object> ret = new ArrayList<Object>();
        
        while(r.next()) {
            
            int id_c = r.getInt(1);
            int id_d = r.getInt(10);
            int type = r.getInt(12);
            String serial;
            
            if(type == Constants.MOBILE)
                serial = r.getString(13);
            else
                serial = r.getString(14);
            
            // create the customer
            Customer c = new Customer(id_c, r.getString(2), r.getString(3), r.getString(4), r.getString(5), "");
            ret.add(c);
            
            // create the repair status
            Repair rep = new Repair(id_c, id_d, r.getString(6), r.getString(7), r.getInt(8), r.getString(9));
            ret.add(rep);
          
            // create the device
            Device d = new Device(id_d, r.getString(10), r.getString(11), type, serial);
            ret.add(d);
            
        }
        // TODO manage objects inserted into ArrayList
        return ret;
        
    }
      
        
    }