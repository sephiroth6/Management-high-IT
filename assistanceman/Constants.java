/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assistanceman;

/**
 *
 * @author Claudio
 */

public class Constants {
    
    // database name
    public static final String DBNAME = "jdbc:sqlite:assistance.db";
    
    // query parts
    public static final String CRTABLE = "CREATE TABLE IF NOT EXISTS ";
    public static final String ID = " (id INTEGER PRIMARY KEY, ";
    public static final String REF = " REFERENCES ";
    public static final String DEL = "ON DELETE CASCADE";
    public static final String PKEY = " PRIMARY KEY";
    public static final String INS = "INSERT INTO ";
    public static final String VAL = " VALUES (";
    public static final String SEL = "SELECT * FROM ";
    public static final String UP = "UPDATE ";
    public static final String SET = " SET ";
    public static final String W = " WHERE ";
    public static final String UNI = " UNIQUE (";
    
    // exception messages
    public static final String EXC_UM = "are not unique";
    public static final String EXC_UO = "is not unique";
    
    // device type codes
    public static final int MOBILE = 0;
    public static final int COM = 1;
    public static final int OTH = 2;
    
}
