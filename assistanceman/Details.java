/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

/**
 *
 * @author Claudio
 */
public class Details {
    
    private int id_c;
    private int id_d;
    private String in;
    private String start;
    private String declared;
    private String found;
    private double spare_price;
    private double work_price;
    private String note;
    
    public Details (Customer c, Device dev, Repair r, String dec) {
        
        this.id_c = c.getID();
        this.id_d = dev.getID();
        this.in = r.getDateIn();
        this.declared = dec;
        
    }
    
}
