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
    
    // printing constants
    private static final int DECLARED = 0;
    private static final int FOUND = 1;
    private static final int DONE = 2;
    
    // is it a copy of a receipt?
    private boolean receipt;
    
    // object containing the info to print
    private SharedClasses.Customer c;
    private SharedClasses.Repair r;
    private SharedClasses.Device d;
    private SharedClasses.Details de;
    
    public Print (SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de, boolean re) {
        this.c = c;
        this.r = r;
        this.d = d;
        this.de = de;
        this.receipt = re;
    }
    
    public static void repairPrint (int i, SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de, boolean re) throws PrinterException {
        
        String name = "Scheda ";
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(name.concat(Integer.toString(i)));
        pj.setCopies(2);
        pj.printDialog();
        pj.setPrintable(new Print(c, r, d, de, re));
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
        int imageX = width - (width/20) - 100;
        int imageY = firstY - heightLines(1);
        String optional = this.r.getOptional();
        
        if(optional == null || optional.equals(""))
            optional = "NESSUNO";

        Graphics2D grapdd = (Graphics2D) grap;
        Font f = new Font("Sans Serif", Font.PLAIN, 10);
        Font b = new Font("Sans Serif", Font.BOLD, 10);
        Font p = new Font("Serif", Font.PLAIN, 10);
        Font footer = new Font("Sans Serif", Font.PLAIN, 8);
        FontRenderContext frc = grapdd.getFontRenderContext();
        Image logo = ManagGuiView.createImageIcon("images/logo100.png", "logo MR. Cooper").getImage();
        grapdd.drawImage(logo, imageX, imageY, null);
        String header;
        
        if(this.receipt)
            header = "SCHEDA RIPARAZIONE #";
        else
            header = "RIEPILOGO RIPARAZIONE #";
        
        String dateOut = this.r.getDateOut();
        if(dateOut == null)
            dateOut = "";

        grapdd.drawGlyphVector(footer.createGlyphVector(frc, "MR. Cooper di Pette Davide - via Michele di Lando 22/24 Roma, 00162 - P.IVA 11549761002 - CF PTTDVD85E20H501E"), firstX, height - 20);
        grapdd.drawGlyphVector(footer.createGlyphVector(frc, "Tel.: 06/98933294 - Mail: telefonia.mrcooper@gmail.com"), firstX, height - 5);
        
        
        // Customer info
        grapdd.drawGlyphVector(b.createGlyphVector(frc, header.concat(Integer.toString(this.r.getID()))), firstX, firstY);
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
        printDeclared(firstX, firstY + heightLines(3), grapdd, f, frc);
        grapdd.drawGlyphVector(f.createGlyphVector(frc, "Accessori consegnati: ".concat(optional)), firstX + 20, firstY + heightLines(6));
        firstY = firstY + heightLines(7);
        grap.drawLine(x + 100, firstY, width - 100 , firstY);
        
        if(this.receipt) {              // print the privacy info...
            try {
                Print.privacy(x, firstX, firstY + 1, width, grap, grapdd, p, frc);
            } catch (InterruptedException e) {
                
            }
        } else {                        // ...or the repair details
            grapdd.drawGlyphVector(b.createGlyphVector(frc, "DETTAGLI RIPARAZIONE"), firstX, firstY + heightLines(1));
            printFound(firstX, firstY + heightLines(2), grapdd, f, frc);
            printDone(firstX, firstY + heightLines(5), grapdd, f, frc);
            grapdd.drawGlyphVector(f.createGlyphVector(frc, "Data ritiro: ".concat(dateOut)), firstX + 20, firstY + heightLines(8));
        }
        
        return PAGE_EXISTS;
    }
     
     private static int heightLines (int i) {
         return i * 20;
     }
     
     private static int halfHeightLines (int i) {
         return i * 10;
     }
  
     private void printDeclared (int x, int y, Graphics2D g, Font f, FontRenderContext frc) {
         this.printDefect (x, y, g, f, frc, Print.DECLARED);
     }
     
     private void printFound (int x, int y, Graphics2D g, Font f, FontRenderContext frc) {
         this.printDefect (x, y, g, f, frc, Print.FOUND);
     }
     
     private void printDone (int x, int y, Graphics2D g, Font f, FontRenderContext frc) {
         this.printDefect (x, y, g, f, frc, Print.DONE);
     }
     
     private void printDefect(int x, int y, Graphics2D g, Font f, FontRenderContext frc, int type) {
         
         String start;
         String s = null;
         
         switch (type) {
             case Print.DECLARED:
                 start = "Difetto dichiarato: ";
                 s = this.de.getDeclared();
                 break;
                 
             case Print.FOUND:
                 start = "Difetto riscontrato: ";
                 s = this.de.getFound();
                 break;
                 
             default:
                 start = "Lavorazione effettuata: ";
                 s = this.de.getDone();
         }
         
         if(s == null)
             s = "NULLA";
         
         if(s.length() < 53) {              // only one line needed
         
             g.drawGlyphVector(f.createGlyphVector(frc, start.concat(s)), x + 20, y);
         
         } else {                           // more lines
         
             g.drawGlyphVector(f.createGlyphVector(frc, start.concat(s.substring(0, 52))), x + 20, y);
             
             if (s.length() < 121) {        // two lines needed 
                  g.drawGlyphVector(f.createGlyphVector(frc, s.substring(53, s.length())), x + 20, y + heightLines(1));
              } else {                      // three lines needed
                                            
                  g.drawGlyphVector(f.createGlyphVector(frc, s.substring(53, 120)), x + 20, y + heightLines(1));
             
                  if (s.length() < 188)     // three lines needed, without appending "..."
                      g.drawGlyphVector(f.createGlyphVector(frc, s.substring(116, s.length())), x + 20, y + heightLines(2));
                  else                      // three lines needed, appending "..."
                     g.drawGlyphVector(f.createGlyphVector(frc, s.substring(116, 185).concat("...")), x + 20, y + heightLines(2)); 
                
             }
        }
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
     
     // print privacy declaration
     private static void privacy (int x, int firstX, int firstY, int width, Graphics grap, Graphics2D grapdd, Font p, FontRenderContext frc) throws InterruptedException {
         // responsability
        //Thread.sleep(500);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, " "), 0, 0);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "Il centro non è responsabile per eventuali accessori consegnati e non dichiarati al momento dell’accettazione. Si avvisa il cliente"), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "di realizzare delle copie di sicurezza dei dati presenti nella memoria del telefono, prima di consegnare il telefono medesimo"), firstX, firstY + heightLines(2));
        //Thread.sleep(500);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, " "), 0, 0);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "al centro di assistenza per la riparazione, onde evitare la perdita degli stessi. Ai sensi dell’articolo 13 del D.lgs n. 196"), firstX, firstY + heightLines(3));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "del 30 giugno 2003, si rende noto che i dati personali rilasciati dal cliente saranno oggetto di trattamento, nel rispetto della"), firstX, firstY + heightLines(4));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "normativa sopra richiamata e degli obblighi di riservatezza."), firstX, firstY + heightLines(5));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "Titolare del trattamento dei dati è MR. COOPER di Pette Davide con sede in via Michele di Lando 22/24 00162 Roma (RM)."), firstX, firstY + heightLines(6));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "FIRMA"), width - 150, firstY + heightLines(8));
        grap.setColor(Color.LIGHT_GRAY);
        grap.drawLine(width - 200, firstY + heightLines(10), width - 67, firstY + heightLines(10));
        grap.setColor(Color.BLACK);
        // privacy
        firstY = firstY + heightLines(11);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "ACQUISIZIONE DEL CONSENSO"), firstX, firstY);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, " "), 0, 0);
        //Thread.sleep(500);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "Dichiaro di aver acquisito le informazioni fornite dal titolare del trattamento ai sensi dell’art. 13 del D.lgs n 196/2003"), firstX, firstY + heightLines(1));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "e di prestare il mio consenso al trattamento dei dati personali. Presto inoltre il consenso alla comunicazione dei dati"), firstX, firstY + heightLines(2));
        //Thread.sleep(500);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, " "), 0, 0);
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "personali ai soggetti indicati nell’informativa."), firstX, firstY + heightLines(3));
        grapdd.drawGlyphVector(p.createGlyphVector(frc, "FIRMA"), width - 150, firstY + heightLines(5));
        grap.setColor(Color.LIGHT_GRAY);
        grap.drawLine(width - 200, firstY + heightLines(7), width - 67, firstY + heightLines(7));
        grap.setColor(Color.BLACK);
     }
     
}
