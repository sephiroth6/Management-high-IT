/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author Claudio
 */
public class Print implements Printable {
    
    private SharedClasses.Customer c;
    
    public Print (SharedClasses.Customer c) {
        this.c = c;
    }
    
    public static void repairPrint (int i, SharedClasses.Customer c) throws PrinterException {
        
        String name = "Scheda ";
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(name.concat(Integer.toString(i)));
        pj.printDialog();
        pj.setPrintable(new Print(c));
        pj.print();
        
    }
    
     public int print(Graphics grap, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if(pageIndex > 0)
            return NO_SUCH_PAGE;

        int x = (int)pageFormat.getImageableX();
        int y = (int)pageFormat.getImageableY();
        int width = (int)pageFormat.getImageableWidth();
        int height = (int)pageFormat.getImageableHeight();
        int firstX = x + (width/20);
        int firstY = y + (height/20);
        
        grap.setColor(Color.BLACK);
        grap.drawString("Cognome: ".concat(this.c.getSurname()), firstX, firstY);
        grap.drawString("Nome: ".concat(this.c.getName()), firstX, firstY + 20);
        grap.drawString("Recapito telefonico: ".concat(this.c.getTelephone()), firstX, firstY + 40);
        grap.drawLine(x + 100, firstY + 60, width - 100 , firstY + 60);
        /*
        grap.drawString(this.c.getName(), pageIndex, pageIndex);
        grap.setColor(Color.GREEN);
        grap.drawLine((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY(), (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());      
        */
        return PAGE_EXISTS;
    }
     
}
