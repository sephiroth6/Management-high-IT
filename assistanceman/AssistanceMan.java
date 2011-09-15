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
        Customer x = new Customer("Claudio", "Rauso", "via Mascagni 186", "060606", "");
        Customer y = new Customer("Jaume", "Rauso", "via Mascagni 186", "060606", "puzza");
       
        int a = x.dbInsert(c);
        int b = y.dbInsert(c);
        
        x.setID(a);
        y.setID(b);
        
        Device z = new Device("LG", "Optimus chat", Constants.MOBILE, "123456789");
        
        int d = z.dbInsert(c);
        
        z.setID(d);
        
        Repair r = new Repair(x, z, "batteria");
        
        r.dbInsert(c);
        
    }
}
