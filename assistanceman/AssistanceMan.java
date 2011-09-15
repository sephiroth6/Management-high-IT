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
        
        /*
        Customer x = new Customer("Claudio", "Rauso", "via Mascagni 186", "060606", "");
        Customer y = new Customer("Claudio", "Rauso", "via Mascagni 186", "060606", "puzza");
       
        int a = x.dbInsert(c);
        int b = y.dbInsert(c);
        /*
        Customer z = new Customer("Claudio", "Urquinaona");
        ArrayList<Customer> ret = Customer.results(z.search(c));
        
        for (Customer mammeta : ret)         
            System.out.println(mammeta.toString());
        
        if(a == 0)
            System.out.println("Primo inserimento andato");
        
        if(b == 1)
            System.out.println("Utente già esistente");
        */
        
        Device d = new Device("LG", "Optimus Chat", Constants.MOBILE, "123456789");
        
        int a = d.dbInsert(c);
        int b = d.dbInsert(c);
        
        if(a == 0)
            System.out.println("Primo inserimento andato");
        
        if(b == 1)
            System.out.println("Device già esistente");
        
    }
}
