/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Claudio
 */
public class NewClass {
    
    public static void main (String[] args) {
        
        System.out.println(new BigDecimal(80).divide(new BigDecimal(44), 0, BigDecimal.ROUND_HALF_UP).intValue());
        System.out.println("VIA PIETRO MASCAGNI 186, 00199 ROMA (RM)".length());
    }
    
}
