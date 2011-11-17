/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
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
        pj.setCopies(2);
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
        String optional = this.r.getOptional();
        
        if(optional == null)
            optional = "NESSUNO";

        Graphics2D grapdd = (Graphics2D) grap;
        Font f = new Font("Sans Serif", Font.PLAIN, 10);
        Font b = new Font("Sans Serif", Font.BOLD, 10);
        Font p = new Font("Serif", Font.PLAIN, 10);
        Font footer = new Font("Sans Serif", Font.PLAIN, 8);
        FontRenderContext frc = grapdd.getFontRenderContext();
        Image logo = ManagGuiView.createImageIcon("images/logo100.png", "logo MR. Cooper").getImage();
        grapdd.drawImage(logo, firstX, firstY, null);

        grapdd.drawGlyphVector(footer.createGlyphVector(frc, "MR. Cooper di Pette Davide - via Michele di Lando 22/24 Roma, 00162 - P.IVA 11549761002 - CF PTTDVD85E20H501E"), firstX, height - 20);
        grapdd.drawGlyphVector(footer.createGlyphVector(frc, "Tel.: 06/98933294 - Mail: telefonia.mrcooper@gmail.com"), firstX, height - 5);
        // Customer info
        firstY = firstY + heightLines(6);
        grapdd.drawGlyphVector(b.createGlyphVector(frc, "SCHEDA RIPARAZIONE #".concat(Integer.toString(this.r.getID()))), firstX, firstY);
        grapdd.drawGlyphVector(b.createGlyphVector(frc, "INFORMAZIONI CLIENTE"), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Cognome: ".concat(this.c.getSurname())), firstX + 20, firstY + heightLines(2));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Nome: ".concat(this.c.getName())), firstX + 20, firstY + heightLines(3));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Recapito telefonico: ".concat(this.c.getTelephone())), firstX + 20, firstY + heightLines(4));
        
        // Device info
        firstY = firstY + heightLines(5);
        grap.drawLine(x + 100, firstY, width - 100 , firstY);
        grapdd.drawGlyphVector(b.createGlyphVector(frc, "INFORMAZIONI DISPOSITIVO"), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Modello: ".concat(this.d.getModel())), firstX + 20, firstY + heightLines(2));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Categoria: ".concat(deviceType(this.d.getType()))), firstX + 20, firstY + heightLines(3));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "IMEI/Serial Number: ".concat(this.d.getIdentification())), firstX + 20, firstY + heightLines(4));
        
        // Repair details
        firstY = firstY + heightLines(5);
        grap.drawLine(x + 100, firstY, width - 100 , firstY);
        grapdd.drawGlyphVector(b.createGlyphVector(frc, "DETTAGLI SCHEDA"), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Data e ora ingresso: ".concat(this.r.getDateIn())), firstX + 20, firstY + heightLines(2));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Difetto dichiarato: ".concat(this.de.getDeclared())), firstX + 20, firstY + heightLines(3));
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Accessori consegnati: ".concat(optional)), firstX + 20, firstY + heightLines(6));
        
        // responsability
        firstY = firstY + heightLines(7);
        grap.drawLine(x + 100, firstY, width - 100 , firstY);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "Il centro non è responsabile per eventuali accessori consegnati e non dichiarati al momento dell’accettazione. Si avvisa il cliente "), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "di realizzare delle copie di sicurezza dei dati presenti nella memoria del telefono, prima di consegnare il telefono medesimo al "), firstX, firstY + heightLines(2));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "centro di assistenza per la riparazione, onde evitare la perdita degli stessi. Ai sensi dell’articolo 13 del D.lgs n. 196 del "), firstX, firstY + heightLines(3));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "30 giugno 2003, si rende noto che i dati personali rilasciati dal cliente saranno oggetto di trattamento, nel rispetto della normativa"), firstX, firstY + heightLines(4));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "sopra richiamata e degli obblighi di riservatezza. Titolare del trattamento dei dati è MR. COOPER di Pette Davide con sede in "), firstX, firstY + heightLines(5));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "via Michele di Lando 22/24 00162 Roma (RM)."), firstX, firstY + heightLines(6));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "FIRMA"), width - 150, firstY + heightLines(7));
        grap.setColor(Color.LIGHT_GRAY);
        grap.drawLine(width - 200, firstY + heightLines(8), width - 67, firstY + heightLines(8));
        grap.setColor(Color.BLACK);
        
        // privacy
        firstY = firstY + heightLines(9);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "ACQUISIZIONE DEL CONSENSO"), firstX, firstY);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "Dichiaro di aver acquisito le informazioni fornite dal titolare del trattamento ai sensi dell’art. 13 del D.lgs n 196/2003 e di prestare "), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "il mio consenso al trattamento dei dati personali. Presto inoltre il consenso alla comunicazione dei dati personali ai soggetti "), firstX, firstY + heightLines(2));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "indicati nell’informativa. "), firstX, firstY + heightLines(3));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "FIRMA"), width - 150, firstY + heightLines(4));
        grap.setColor(Color.LIGHT_GRAY);
        grap.drawLine(width - 200, firstY + heightLines(5), width - 67, firstY + heightLines(5));
        grap.setColor(Color.BLACK);
        
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
