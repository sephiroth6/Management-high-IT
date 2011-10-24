/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

/**
 *
 * @author Claudio
 */
public class CustomerRepList {
    
    // class useful when searching repair
    private Customer owner;
    private Repair rep;
    private Device dev;
    
    public CustomerRepList (Customer c, Repair r, Device d) {
        
        this.owner = c;
        this.rep = r;
        this.dev = d;
        
    }

    public Customer getCustomer () {
        
        return this.owner;
        
    }
    
    public Repair getRepair () {
        
        return this.rep;
        
    }
    
    public Device getDevice () {
        
        return this.dev;
        
    }
    
}
