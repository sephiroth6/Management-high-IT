/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

/**
 *
 * @author Claudio
 */
public class NewPageException extends Exception {
    
    /**
     * The first table element for the new page
     */
    private int tableElement;
    
    public NewPageException (int i) {
        this.tableElement = i;
    }
    
    
    
}
