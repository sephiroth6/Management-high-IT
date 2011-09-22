/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

import java.sql.Connection;
import java.sql.ResultSet;
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

        /*
        
        // insert two customers; found_c will be used for the a search result
        Customer x = new Customer("Claudio", "Rauso", "via Mascagni 186", "060606", "");
        Customer y = new Customer("Jaume", "Rauso", "via Mascagni 186", "060606", "puzza");
        Customer found_c;
        
        // device and repair for search result
        Device found_d;
        Repair found_r;
       
        // insert customers
        int a = x.dbInsert(c);
        int b = y.dbInsert(c);

        // set IDs into objects
        x.setID(a);
        y.setID(b);
        
        // create a new device
        Device z = new Device("LG", "Optimus chat", Constants.MOBILE, "123456789");
        
        found_r = new Repair(x, z, "batteria");
        
        int d = z.dbInsert(c);
        
        z.setID(d);
        
        Repair r = new Repair(x, z, "batteria");
        
        r.dbInsert(c);
        
        ArrayList<Object> results = Utils.repairResults(Repair.search(c, x));
        
        if(!results.isEmpty()) {
            found_c = (Customer)results.get(0);
            System.out.println(found_c.toString());
            found_r = (Repair)results.get(1);
            System.out.println(found_r.toString());
            
        }
        
        found_r.deviceOut(c);
        
        results = Utils.repairResults(Repair.search(c, x));
        
        if(!results.isEmpty()) {
            found_c = (Customer)results.get(0);
            System.out.println(found_c.toString());
            found_r = (Repair)results.get(1);
            System.out.println(found_r.toString());
            
        }
        
        Details det = new Details(found_r, "niente da dichiarare", "nessuna nota");
        
        det.dbInsert(c);
        det.setStart(c);
        
        ResultSet r_det = det.search(c);
        
        System.out.println(new Details(r_det).toString());
         * 
         * 
         */
        
        Utils.insertUsage(c);
        
    }
}
