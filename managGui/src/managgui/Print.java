/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Color;
import java.awt.Font;
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
        
        String name = "Scheda ";
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(name.concat(Integer.toString(i)));
        pj.printDialog();
        pj.setPrintable(new Print(c, r, d, de));
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
        Font font = grap.getFont();

        String optional = this.r.getOptional();
        
        if(optional == null)
            optional = "NESSUNO";

        grap.setColor(Color.BLACK);
        
        grap.setFont(font.deriveFont(Font.BOLD));
        grap.drawString("MR. Cooper di Pette Davide", firstX, firstY);
        grap.drawString("via Michele di Lando 22/24", firstX, firstY + heightLines(1));
        grap.drawString("Roma, 00162", firstX, firstY + heightLines(2));
        grap.drawString("P.IVA 11549761002", firstX, firstY + heightLines(3));
        grap.drawString("CF PTTDVD85E20H501E", firstX, firstY + heightLines(4));
        grap.drawString("SCHEDA RIPARAZIONE #".concat(Integer.toString(this.r.getID())), firstX, firstY + heightLines(6));
        grap.drawString("INFORMAZIONI CLIENTE", firstX, firstY + heightLines(8));
        grap.setFont(font.deriveFont(Font.PLAIN));
        
        grap.drawString("Cognome: ".concat(this.c.getSurname()), firstX + 20, firstY + heightLines(9));
        grap.drawString("Nome: ".concat(this.c.getName()), firstX + 20, firstY + heightLines(10));
        grap.drawString("Recapito telefonico: ".concat(this.c.getTelephone()), firstX + 20, firstY + heightLines(11));
        
        grap.drawLine(x + 100, firstY + heightLines(12), width - 100 , firstY + heightLines(12));
        
        grap.setFont(font.deriveFont(Font.BOLD));
        grap.drawString("INFORMAZIONI DISPOSITIVO", firstX, firstY + heightLines(13));
        grap.setFont(font.deriveFont(Font.PLAIN));
        grap.drawString("Modello: ".concat(this.d.getModel()), firstX + 20, firstY + heightLines(14));
        grap.drawString("Categoria: ".concat(deviceType(this.d.getType())), firstX + 20, firstY + heightLines(15));
        grap.drawString("IMEI/Serial Number: ".concat(this.d.getIdentification()), firstX + 20, firstY + heightLines(16));
        
        grap.drawLine(x + 100, firstY + heightLines(17), width - 100 , firstY + heightLines(17));
        
        grap.setFont(font.deriveFont(Font.BOLD));
        grap.drawString("DETTAGLI SCHEDA", firstX, firstY + heightLines(18));
        grap.setFont(font.deriveFont(Font.PLAIN));
        grap.drawString("Data e ora ingresso: ".concat(this.r.getDateIn()), firstX + 20, firstY + heightLines(19));
        grap.drawString("Difetto dichiarato: ".concat(this.de.getDeclared()), firstX + 20, firstY + heightLines(20));
        grap.drawString("Accessori consegnati: ".concat(optional), firstX + 20, firstY + heightLines(21));
        
        grap.drawLine(x + 100, firstY + heightLines(22), width - 100 , firstY + heightLines(22));
        
        firstY = firstY + heightLines(23);
        
        grap.drawString("Il centro non è responsabile per eventuali accessori consegnati e non dichiarati al momento dell’", firstX, firstY);
        grap.drawString("accettazione. Si avvisa il cliente di realizzare delle copie di sicurezza dei dati presenti nella memoria ", firstX, firstY + heightLines(1));
        grap.drawString("del telefono, prima di consegnare il telefono medesimo al centro di assistenza per la riparazione, ", firstX, firstY + heightLines(2));
        grap.drawString("onde evitare la perdita degli stessi. Ai sensi dell’articolo 13 del D.lgs n. 196 del 30 giugno 2003, si ", firstX, firstY + heightLines(3));
        grap.drawString("rende noto che i dati personali rilasciati dal cliente saranno oggetto di trattamento, nel rispetto della ", firstX, firstY + heightLines(4));
        grap.drawString("normativa sopra richiamata e degli obblighi di riservatezza. Titolare del trattamento dei dati è ", firstX, firstY + heightLines(5));
        grap.drawString("MR. COOPER di Pette Davide con sede in Via Michele di Lando 22/24 00162 Roma (RM).", firstX, firstY + heightLines(6));
        grap.drawString("ACQUISIZIONE DEL CONSENSO", firstX, firstY + heightLines(8));
        grap.drawString("Dichiaro di aver acquisito le informazioni fornite dal titolare del trattamento ai sensi dell’art. 13 del ", firstX, firstY + heightLines(9));
        grap.drawString("D.lgs n 196/2003 e di prestare il mio consenso al trattamento dei dati personali. Presto inoltre il ", firstX, firstY + heightLines(10));
        grap.drawString("consenso alla comunicazione dei dati personali ai soggetti indicati nell’informativa. ", firstX, firstY + heightLines(11));
        
        return PAGE_EXISTS;
    }
     
     private static int heightLines (int i) {
         return i * 20;
     }
     
     private static int halfHeightLines (int i) {
         return i * 10;
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
     
}
