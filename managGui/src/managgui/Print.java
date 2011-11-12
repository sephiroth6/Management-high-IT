/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
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
        grap.drawString("SCHEDA RIPARAZIONE #".concat(Integer.toString(this.r.getID())), firstX, firstY);
        grap.drawString("INFORMAZIONI CLIENTE", firstX, firstY + heightLines(3));
        grap.setFont(font.deriveFont(Font.PLAIN));
        
        grap.drawString("Cognome: ".concat(this.c.getSurname()), firstX + 20, firstY + heightLines(4));
        grap.drawString("Nome: ".concat(this.c.getName()), firstX + 20, firstY + heightLines(5));
        grap.drawString("Recapito telefonico: ".concat(this.c.getTelephone()), firstX + 20, firstY + heightLines(6));
        
        grap.drawLine(x + 100, firstY + heightLines(8), width - 100 , firstY + heightLines(8));
        
        grap.setFont(font.deriveFont(Font.BOLD));
        grap.drawString("INFORMAZIONI DISPOSITIVO", firstX, firstY + heightLines(10));
        grap.setFont(font.deriveFont(Font.PLAIN));
        grap.drawString("Modello: ".concat(this.d.getModel()), firstX + 20, firstY + heightLines(11));
        grap.drawString("Categoria: ".concat(deviceType(this.d.getType())), firstX + 20, firstY + heightLines(12));
        grap.drawString("IMEI/Serial Number: ".concat(this.d.getIdentification()), firstX + 20, firstY + heightLines(13));
        
        grap.drawLine(x + 100, firstY + heightLines(15), width - 100 , firstY + heightLines(15));
        
        grap.setFont(font.deriveFont(Font.BOLD));
        grap.drawString("DETTAGLI SCHEDA", firstX, firstY + heightLines(17));
        grap.setFont(font.deriveFont(Font.PLAIN));
        grap.drawString("Data e ora ingresso: ".concat(this.r.getDateIn()), firstX + 20, firstY + heightLines(18));
        grap.drawString("Difetto dichiarato: ".concat(this.de.getDeclared()), firstX + 20, firstY + heightLines(19));
        grap.drawString("Accessori consegnati: ".concat(optional), firstX + 20, firstY + heightLines(20));
        
        grap.drawLine(x + 100, firstY + heightLines(22), width - 100 , firstY + heightLines(22));
        
        firstY = firstY + heightLines(23);
        Graphics2D grapdd = (Graphics2D)grap;
        FontRenderContext frc = grapdd.getFontRenderContext();
        font = font.deriveFont(8,0);
        
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "Il centro non è responsabile per eventuali accessori consegnati e non dichiarati al momento dell’accettazione. Si avvisa il cliente di "), firstX, firstY);
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "realizzare delle copie di sicurezza dei dati presenti nella memoria del telefono, prima di consegnare il telefono medesimo al centro di "), firstX, firstY + halfHeightLines(1));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "assistenza per la riparazione, onde evitare la perdita degli stessi. "), firstX, firstY + halfHeightLines(2));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "Ai sensi dell’articolo 13 del D.lgs n. 196 del 30 giugno 2003, si rende noto che i dati personali rilasciati dal cliente saranno oggetto di "), firstX, firstY + halfHeightLines(3));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "trattamento, nel rispetto della normativa sopra richiamata e degli obblighi di riservatezza. "), firstX, firstY + halfHeightLines(4));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "Titolare del trattamento dei dati è MR. COOPER di Pette Davide con sede in Via Michele di Lando 22/24 00162 Roma (RM). "), firstX, firstY + halfHeightLines(5));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "ACQUISIZIONE DEL CONSENSO dichiaro di aver acquisito le informazioni fornite dal titolare del trattamento ai sensi dell’art. 13 del "), firstX, firstY + halfHeightLines(6));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "D.lgs n 196/2003 e di prestare il mio consenso al trattamento dei dati personali. Presto inoltre il consenso alla comunicazione dei dati "), firstX, firstY + halfHeightLines(7));
        grapdd.drawGlyphVector(font.createGlyphVector(frc, "personali ai soggetti indicati nell’informativa. "), firstX, firstY + halfHeightLines(8));
        
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
