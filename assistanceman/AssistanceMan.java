/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Claudio
 */
public class AssistanceMan {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        Connection c = Utils.dbConnection(Constants.DBNAME);
        Utils.createTables(c);
        
        Customer x = new Customer("Claudio", "Rauso", "via Mascagni 186", "0697602153", "nulla");
        Customer a = new Customer("Claudio", "Ranieri", "pinetina", "020202", "debitore");
        Device y = new Device("lg", "optimus chat", Constants.MOBILE, "123456789");
        Device b = new Device("asus", "k50j", Constants.COM, "987654321");
        Repair z = null;
        Repair o = null;
        Details w = null;
        
        // search results in this array
        ArrayList<CustomerRepList> results = null;
        
        // insert the two customers and the two devices
        x.setID(x.dbInsert(c));
        y.setID(y.dbInsert(c));
        a.setID(a.dbInsert(c));
        b.setID(b.dbInsert(c));
        
        z = new Repair(x, y, "batteria e auricolare");
        z.setID(z.dbInsert(c));
        
        o = new Repair(a, b, "batteria");
        o.setID(o.dbInsert(c));
        
        o = new Repair(x, b, "batteria");
        o.setID(o.dbInsert(c));
        
        w = new Details(z, "niente da dichiarare", "nessuna nota");
        w.dbInsert(c);
        
        // search only with customer's name
        Customer search = new Customer("Claudio", null, null, null, null);
        
        results = Utils.repairResults(Repair.search(c, search, null, null));
        
        //Utils.displayResults(results);
        
        Warehouse p = new Warehouse("abc123", "chip 98", 12, 2.5, "ottimi");
        p.dbInsert(c);
        
        Warehouse pe = new Warehouse(p.search(c));
        Warehouse p_mod = new Warehouse("abc123", "", 42, 0, "");
        
        
        pe = new Warehouse(p.search(c));
        System.out.println(pe.toString());
  
        
    }
}
