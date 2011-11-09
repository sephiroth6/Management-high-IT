/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Color;
<<<<<<< HEAD
=======
import java.awt.Font;
>>>>>>> 484315bbc8a83593a47b1c66f8fee38723390935
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
<<<<<<< HEAD
    
    public Print (SharedClasses.Customer c) {
        this.c = c;
    }
    
    public static void repairPrint (int i, SharedClasses.Customer c) throws PrinterException {
=======
    private SharedClasses.Repair r;
    private SharedClasses.Device d;
    private SharedClasses.Details de;
    
    public Print (SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de) {
        this.c = c;
        this.r = r;
        this.d = d;
        this.de = de;
    }
    
    public static void repairPrint (int i, SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de) throws PrinterException {
>>>>>>> 484315bbc8a83593a47b1c66f8fee38723390935
        
        String name = "Scheda ";
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(name.concat(Integer.toString(i)));
        pj.printDialog();
<<<<<<< HEAD
        pj.setPrintable(new Print(c));
=======
        pj.setPrintable(new Print(c, r, d, de));
>>>>>>> 484315bbc8a83593a47b1c66f8fee38723390935
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
<<<<<<< HEAD
        
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
     
=======
        String optional = this.r.getOptional();
        
        if(optional == null)
            optional = "NESSUNO";
        
        grap.setColor(Color.BLACK);
        
        grap.setFont(grap.getFont().deriveFont(Font.BOLD));
        grap.drawString("SCHEDA RIPARAZIONE #".concat(Integer.toString(this.r.getID())), firstX, firstY);
        grap.drawString("INFORMAZIONI CLIENTE", firstX, firstY + heightLines(3));
        grap.setFont(grap.getFont().deriveFont(Font.PLAIN));
        
        grap.drawString("Cognome: ".concat(this.c.getSurname()), firstX + 20, firstY + heightLines(4));
        grap.drawString("Nome: ".concat(this.c.getName()), firstX + 20, firstY + heightLines(5));
        grap.drawString("Recapito telefonico: ".concat(this.c.getTelephone()), firstX + 20, firstY + heightLines(6));
        
        grap.drawLine(x + 100, firstY + heightLines(8), width - 100 , firstY + heightLines(8));
        
        grap.setFont(grap.getFont().deriveFont(Font.BOLD));
        grap.drawString("INFORMAZIONI DISPOSITIVO", firstX, firstY + heightLines(10));
        grap.setFont(grap.getFont().deriveFont(Font.PLAIN));
        grap.drawString("Modello: ".concat(this.d.getModel()), firstX + 20, firstY + heightLines(11));
        grap.drawString("Categoria: ".concat(deviceType(this.d.getType())), firstX + 20, firstY + heightLines(12));
        grap.drawString("IMEI/Serial Number: ".concat(this.d.getIdentification()), firstX + 20, firstY + heightLines(13));
        
        grap.drawLine(x + 100, firstY + heightLines(15), width - 100 , firstY + heightLines(15));
        
        grap.setFont(grap.getFont().deriveFont(Font.BOLD));
        grap.drawString("DETTAGLI SCHEDA", firstX, firstY + heightLines(17));
        grap.setFont(grap.getFont().deriveFont(Font.PLAIN));
        grap.drawString("Data e ora ingresso: ".concat(this.r.getDateIn()), firstX + 20, firstY + heightLines(18));
        grap.drawString("Difetto dichiarato: ".concat(this.de.getDeclared()), firstX + 20, firstY + heightLines(19));
        grap.drawString("Accessori consegnati: ".concat(optional), firstX + 20, firstY + heightLines(20));
        
        grap.drawLine(x + 100, firstY + heightLines(22), width - 100 , firstY + heightLines(22));
        
        System.out.println(grap.getFont().getSize());
        grap.setFont(grap.getFont().deriveFont(8,0));
        grap.drawString("Prova testo scritto più piccolo: qui possiamo piazzare l'informativa sulla privacy.", firstX, firstY + heightLines(23));

        return PAGE_EXISTS;
    }
     
     private static int heightLines (int i) {
         
         return i * 20;
         
     }
     
     private static String deviceType (int i) {
         
         switch(i) {
            
             case ComClasses.Constants.MOBILE:
                return "TELEFONIA";
                 
             case ComClasses.Constants.COM:
                 return "INFORMATICA";
                 
             default:
                 return "ALTRO";
         
         }
     }
     
     
>>>>>>> 484315bbc8a83593a47b1c66f8fee38723390935
}
