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
        
        // set foreign keys ON (default from SQLite 3.6.19 version is OFF)
        Statement s = c.createStatement();
        s.execute("PRAGMA foreign_keys=on;");
        
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
        q.append("repair");
        q.append(Constants.ID);
        q.append("id_c INTEGER");
        q.append(Constants.REF);
        q.append("customer(id), id_d INTEGER");
        q.append(Constants.REF);
        q.append("device(id), date_in TEXT");
        q.append(Constants.DEF_DATE);
        q.append(", date_out TEXT, status INTEGER DEFAULT 0, optional TEXT,");
        q.append(Constants.UNI);
        q.append("id_c, id_d, date_in));");
        
        s.executeUpdate(new String(q));
        
        // fourth query, details of each repair
        q = new StringBuilder(Constants.CRTABLE);
        q.append("details(rep_id INTEGER, date_start TEXT, declared TEXT, found TEXT, spare_price REAL, work_price REAL, note TEXT,");
        q.append(Constants.FOR);
        q.append("rep_id)");
        q.append(Constants.REF);
        q.append("repair(id));");
        
        s.executeUpdate(new String(q));
        
        // fifth query, warehouse with the spare parts
        q = new StringBuilder(Constants.CRTABLE);
        q.append("warehouse ");
        q.append("(serial TEXT PRIMARY KEY, name TEXT, available INTEGER, unit_price REAL, note TEXT);");
        
        s.executeUpdate(new String(q));
        
        // last query, usage: spare parts used in each repair
        q = new StringBuilder(Constants.CRTABLE);
        q.append("usage (rep_id INTEGER, serial TEXT");
        q.append(Constants.REF);
        q.append("warehouse(serial), used INTEGER,");
        q.append(Constants.FOR);
        q.append("rep_id)");
        q.append(Constants.REF);
        q.append("repair(id));");
       
        s.executeUpdate(new String(q));
        
    }
    
    // creates objects from a searching repair query
    public static ArrayList<CustomerRepList> repairResults (ResultSet r) throws SQLException {
        
        ArrayList<CustomerRepList> list = new ArrayList<CustomerRepList>();
        
        while(r.next()) {
            
            int id_c = r.getInt(1);
            int rep_id = r.getInt(6);
            int id_d = r.getInt(11);
            int type = r.getInt(14);
            String serial;
            
            if(type == Constants.MOBILE)
                serial = r.getString(15);
            else
                serial = r.getString(16);
            
            // create the customer
            Customer c = new Customer(id_c, r.getString(2), r.getString(3), r.getString(4), r.getString(5), "");
            // create the repair status
            Repair rep = new Repair(rep_id, id_c, id_d, r.getString(7), r.getString(8), r.getInt(9), r.getString(10));
            // create the device
            Device d = new Device(id_d, r.getString(12), r.getString(13), type, serial);
            
            list.add(new CustomerRepList(c, rep, d));
            
        }
        
        return list;
        
    }
            
    // manages the search result of a repair
    public static void displayResults (ArrayList<CustomerRepList> x) {
        
        for(CustomerRepList v : x) {
            
            System.out.println(v.getCustomer().toString());
            System.out.println(v.getDevice().toString());
            System.out.println(v.getRepair().toString());
            System.out.println();
            
        }

    }
    
    // manages how the dates are displayed
    public static String formatDate (String date) {
        
        if(date != null) {
            
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            String day = date.substring(8, 10);
            String hour = date.substring(11);
        
            return day.concat("/").concat(month).concat("/").concat(year).concat(" alle ").concat(hour);
        
        } else {
        
            return "non registrata";
        
        }
    
    }
        
    }