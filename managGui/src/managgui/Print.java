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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    private final boolean receipt;
    // is it a billing?
    private final int billing;
    
    private final String totalImp;
    private final String percentage;
    private final String total;
    private final String date;
    private final String number;
    
    // object containing the info to print
    private final SharedClasses.Customer c;
    private final SharedClasses.BillingCustomer bc;
    private final SharedClasses.Repair r;
    private final SharedClasses.Device d;
    private final SharedClasses.Details de;
    private final javax.swing.JTable t;
     
    public Print (SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de, boolean re) {
        // repair
        this.c = c;
        this.r = r;
        this.d = d;
        this.de = de;
        this.receipt = re;
        this.billing = -1;
        this.bc = null;
        this.t = null;
        this.totalImp = null;
        this.percentage = null;
        this.total = null;
        this.date = null;
        this.number = null;
    }
    
    public Print (int n, SharedClasses.Customer c, SharedClasses.BillingCustomer bc, int ty, JTable table, String imp, String iva, String tot, String dt) {
        // billing
        this.number = new Integer(n).toString();
        this.c = c;
        this.bc = bc;
        this.receipt = false;
        this.billing = ty;
        this.t = table;
        this.totalImp = imp;
        this.percentage = iva;
        this.total = tot;
        this.date = dt;
        this.r = null;
        this.d = null;
        this.de = null;
    }
    
    private static PrinterJob initPrinting (int i) {
        String name = "Stampa ";
        
        PrinterJob ret = PrinterJob.getPrinterJob();
        ret.setJobName(name.concat(Integer.toString(i)));
        ret.setCopies(2);
        ret.printDialog();
        
        return ret;
    }
      
    public static void repairPrint (int i, SharedClasses.Customer c, SharedClasses.Repair r, SharedClasses.Device d, SharedClasses.Details de, boolean re) throws PrinterException {
        // used when printing repair detail
        PrinterJob pj = initPrinting(i);
            
        pj.setPrintable(new Print(c, r, d, de, re));
        
        pj.print();
    }
    
    public static void repairPrint (int i, SharedClasses.Customer c, SharedClasses.BillingCustomer bc, int ty, JTable t, String imp, String iva, String tot, String dt) throws PrinterException {
        // used when printing billing info
        PrinterJob pj = initPrinting(i);
        
        pj.setPrintable(new Print(i, c, bc, ty, t, imp, iva, tot, dt));
        
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
        Graphics2D grapdd = (Graphics2D) grap;
        Font f = new Font("Sans Serif", Font.PLAIN, 10);
        Font b = new Font("Sans Serif", Font.BOLD, 10);
        Font p = new Font("Serif", Font.PLAIN, 10);
        Font footer = new Font("Sans Serif", Font.PLAIN, 8);
        FontRenderContext frc = grapdd.getFontRenderContext();     
        
        // logo and header
        drawLogo(imageX, imageY, grapdd);
        this.writeHeader(grapdd, frc, b, firstX, firstY);
        
        if(this.billing < 0) {                                         // don't print billing informations
            // Customer info
            this.writeCustomer(grapdd, frc, b, f, firstX, firstY);
            // add space and line
            firstY = firstY + heightLines(5);
            grap.drawLine(x + 100, firstY, width - 100 , firstY);
            // Device info
            this.writeDevice(grapdd, frc, b, f, firstX, firstY);
             // add space and line
            firstY = firstY + heightLines(5);
            grap.drawLine(x + 100, firstY, width - 100 , firstY);
            // Repair details
            String optional = this.r.getOptional();
            if(optional == null || optional.equals(""))
                optional = "NESSUNO";
            this.writeDetailsIN(grapdd, frc, b, f, firstX, firstY, optional);
            // add space and line
            firstY = firstY + heightLines(7);
            grap.drawLine(x + 100, firstY, width - 100 , firstY);

            if(this.receipt) {              // print the privacy info...
                try {
                    Print.privacy(x, firstX, firstY + 1, width, grap, grapdd, p, frc);
                } catch (InterruptedException e) {

                }
            } else {                        // ...or the repair details
                String dateOut = this.r.getDateOut();
                if(dateOut == null)
                    dateOut = "";
                this.writeDetailsOUT(grapdd, frc, b, f, firstX, firstY, dateOut);
            }

            this.writeFooter(grapdd, frc, footer, firstX, height);
            
        } else {                                                    // billing informations
            writeBillingInfo(grapdd, frc, f, firstX, firstY);
            firstY += shortHeightLines(6);
            this.writeBillingCustomer(grapdd, frc, b, f, firstX, firstY);
            firstY += shortHeightLines(8);
            firstY = this.writeTable(grapdd, frc, b, footer, firstX, firstY, width);
            firstY += heightLines(1);
            this.writeBillingFooter(grapdd, frc, b, f, firstX, firstY);
        }
        
        return PAGE_EXISTS;
    }
     
     private static int heightLines (int i) {
         return i * 20;
     }
     
     private static int shortHeightLines (int i) {
         return i * 15;
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
 
     private static void drawLogo (int x, int y, Graphics2D g) {
         // draw Mr. Cooper logo
         Image logo = ManagGuiView.createImageIcon("images/logo100.png", "logo MR. Cooper").getImage();
         g.drawImage(logo, x, y, null);
     }
     
     private void writeHeader (Graphics2D g, FontRenderContext fRend, Font b, int x, int y) {
         String header;
        
         if(this.billing >= 0) {
             // TODO add billing number and manage different cases (fattura, ritenuta d'acconto, nota di credito)
             header = this.billingName();
             g.drawGlyphVector(b.createGlyphVector(fRend, header.concat(this.number.concat(" - DATA EMISSIONE: ".concat(this.date)))), x, y);
         } else {
             
             if(this.receipt)
                header = "SCHEDA RIPARAZIONE #";
             else
                header = "RIEPILOGO RIPARAZIONE #";
             
             g.drawGlyphVector(b.createGlyphVector(fRend, header.concat(Integer.toString(this.r.getID()))), x, y);
         }
     }
     
     private String billingName () {
         switch (this.billing) {
             case ComClasses.Constants.BILL:
                 return "FATTURA #";
                 
             case ComClasses.Constants.NDC:
                 return "NOTA DI CREDITO #";
                 
             case ComClasses.Constants.RDA:
                 return "RITENUTA D'ACCONTO #";
                 
             default:
                 return null;
         }
     }
     
     private void writeCustomer (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y) {
         g.drawGlyphVector(b.createGlyphVector(fRend, "INFORMAZIONI CLIENTE"), x, y + heightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Cognome: ".concat(this.c.getSurname())), x + 20, y + heightLines(2));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Nome: ".concat(this.c.getName())), x + 20, y + heightLines(3));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Recapito telefonico: ".concat(this.c.getTelephone())), x + 20, y + heightLines(4));
     }
     
     private void writeDevice (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y) {
         g.drawGlyphVector(b.createGlyphVector(fRend, "INFORMAZIONI DISPOSITIVO"), x, y + heightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Modello: ".concat(this.d.getModel())), x + 20, y + heightLines(2));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Categoria: ".concat(deviceType(this.d.getType()))), x + 20, y + heightLines(3));
         g.drawGlyphVector(f.createGlyphVector(fRend, "IMEI/Serial Number: ".concat(this.d.getIdentification())), x + 20, y + heightLines(4));
     }

     private void writeFooter (Graphics2D g, FontRenderContext fRend, Font f, int x, int h) {
         g.drawGlyphVector(f.createGlyphVector(fRend, "MR. Cooper di Pette Davide - via Michele di Lando 22/24 Roma, 00162 - P.IVA 11549761002 - CF PTTDVD85E20H501E"), x, h - 20);
         g.drawGlyphVector(f.createGlyphVector(fRend, "Tel.: 06/98933294 - Mail: telefonia.mrcooper@gmail.com"), x, h - 5);
     }

     private void writeDetailsIN (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y, String o) {
         g.drawGlyphVector(b.createGlyphVector(fRend, "DETTAGLI SCHEDA"), x, y + heightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Data e ora ingresso: ".concat(this.r.getDateIn())), x + 20, x + heightLines(2));
         printDeclared(x, x + heightLines(3), g, f, fRend);
         g.drawGlyphVector(f.createGlyphVector(fRend, "Accessori consegnati: ".concat(o)), x + 20, x + heightLines(6));
     }
 
     private void writeDetailsOUT (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y, String d) {
         g.drawGlyphVector(b.createGlyphVector(fRend, "DETTAGLI RIPARAZIONE"), x, y + heightLines(1));
         printFound(x, y + heightLines(2), g, f, fRend);
         printDone(x, y + heightLines(5), g, f, fRend);
         g.drawGlyphVector(f.createGlyphVector(fRend, "Data ritiro: ".concat(d)), x + 20, y + heightLines(8));
     }
 
     private static void writeBillingInfo (Graphics2D g, FontRenderContext fRend, Font f, int x, int y) {
         g.drawGlyphVector(f.createGlyphVector(fRend, "MR. Cooper di Pette Davide"), x + 20, y + shortHeightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, "via Michele di Lando 22/24"), x + 20, y + shortHeightLines(2));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Roma, 00162"), x + 20, y + shortHeightLines(3));
         g.drawGlyphVector(f.createGlyphVector(fRend, "P.IVA 11549761002"), x + 20, y + shortHeightLines(4));
         g.drawGlyphVector(f.createGlyphVector(fRend, "CF PTTDVD85E20H501E"), x + 20, y + shortHeightLines(5));
     }
     
     private void writeBillingCustomer (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y) {
         g.drawGlyphVector(b.createGlyphVector(fRend, "DATI CLIENTE"), x, y + shortHeightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Cognome: ".concat(this.c.getSurname())), x + 20, y + shortHeightLines(2));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Nome: ".concat(this.c.getName())), x + 20, y + shortHeightLines(3));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Codice Fiscale: ".concat(this.bc.getCF())), x + 20, y + shortHeightLines(4));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Partita IVA: ".concat(this.bc.getIVA())), x + 20, y + shortHeightLines(5));
         g.drawGlyphVector(f.createGlyphVector(fRend, "Indirizzo: ".concat(customerAddress(this.bc))), x + 20, y + shortHeightLines(6));
     }
 
     private static String customerAddress (SharedClasses.BillingCustomer info) {
         // return a formatted String with customer address
         StringBuilder ret = new StringBuilder();
         ret.append(info.getAddress().concat(", "));
         ret.append(info.getCAP().concat(" "));
         ret.append(info.getCity().concat(" "));
         ret.append("(");
         ret.append(info.getProv().concat(")"));
         
         return ret.toString();
     }
     
     private int writeTable (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y, int w) {
         // print the billing table
         DefaultTableModel model = (DefaultTableModel)this.t.getModel();
         int n = model.getRowCount();
         int m = model.getColumnCount();
         
         writeColumnsNames(g, fRend, b, x, y);
         // horizontal lines
         g.drawLine(x - 5, y - 10, w - 15, y - 10);     // up
         g.drawLine(x - 5, y + 5, w - 15, y + 5);       // down
         // vertical lines
         g.drawLine(x - 5, y - 10, x - 5, y + 5);       // left margin
         g.drawLine(w - 15, y - 10, w - 15, y + 5);     // right margin
         g.drawLine(x + 49, y - 10, x + 49, y + 5);     // between "TIPOLOGIA" and "CODICE"
         g.drawLine(x + 148, y - 10, x + 148, y + 5);   // between "CODICE" and "DESCRIZIONE"
         g.drawLine(x + 380, y - 10, x + 380, y + 5);   // between "DESCRIZIONE" and "Q.TA'"
         g.drawLine(x + 416, y - 10, x + 416, y + 5);   // between "Q.TA'" and "PREZZO"
         g.drawLine(x + 481, y - 10, x + 481, y + 5);   // between "PREZZO" and "TOT"
         g.drawLine(x + 526, y - 10, x + 526, y + 5);   // between "TOT" and "IVA"
         y += shortHeightLines(1);
         
         for(int i = 0; i < n; i++) {
             int auxYA = y + 5 - shortHeightLines(1);
             int auxYB = y + 5;
             // vertical lines
             g.drawLine(x - 5, auxYA, x - 5, auxYB);          // left
             g.drawLine(w - 15, auxYA, w - 15, auxYB);        // right
             g.drawLine(x + 49, auxYA, x + 49, auxYB);
             g.drawLine(x + 148, auxYA, x + 148, auxYB);
             g.drawLine(x + 380, auxYA, x + 380, auxYB);
             g.drawLine(x + 416, auxYA, x + 416, auxYB);
             g.drawLine(x + 481, auxYA, x + 481, auxYB);
             g.drawLine(x + 526, auxYA, x + 526, auxYB);
             printRow(g, fRend, f, x, y, i, m, model);
             // horizontal line
             g.drawLine(x - 5, y + 5, w - 15, y + 5);
             y += shortHeightLines(1);
         }         
         return y;
     }
     
     private static void printRow (Graphics2D g, FontRenderContext fRend, Font f, int x, int y, int row, int col, DefaultTableModel m) {
         // print a billing row
         for(int i = 0; i < col; i++)
            printColumn(g, fRend, f, x, y, row, i, m);
     }
     
     private static void printColumn (Graphics2D g, FontRenderContext fRend, Font f, int x, int y, int row, int col, DefaultTableModel m) {
         // print an element of a row
         switch(col) {
             case 0:    // type
                 g.drawGlyphVector(f.createGlyphVector(fRend, elementType(m.getValueAt(row, col).toString())), x, y);
                 break;
                 
             case 1:    // code
                 g.drawGlyphVector(f.createGlyphVector(fRend, m.getValueAt(row, col).toString()), x + 51, y);
                 break;
                 
             case 2:    // description
                 g.drawGlyphVector(f.createGlyphVector(fRend, m.getValueAt(row, col).toString()), x + 151, y);
                 break;
                 
             case 3:   // quantity
                 try {
                    Integer val = (Integer)m.getValueAt(row, col);
                    g.drawGlyphVector(f.createGlyphVector(fRend, val.toString()), x + 384, y);
                 } catch (java.lang.ClassCastException e) {
                     g.drawGlyphVector(f.createGlyphVector(fRend, (String)m.getValueAt(row, col)), x + 384, y);
                 }
                 break;
                 
             case 4:    // price
                 g.drawGlyphVector(f.createGlyphVector(fRend, m.getValueAt(row, col).toString()), x + 420, y);
                 break;
                 
             case 5:    // tax?
                 Boolean aux = (Boolean)m.getValueAt(row, col);
                 if(!aux)
                     g.drawGlyphVector(f.createGlyphVector(fRend, "sì"), x + 530, y);
                 else
                     g.drawGlyphVector(f.createGlyphVector(fRend, "no"), x + 530, y);
                 break;
                 
             case 6:    // total price (price * quantity)
                 g.drawGlyphVector(f.createGlyphVector(fRend, m.getValueAt(row, col).toString()), x + 485, y);
         }
     }
     
     private static String elementType (String s) {
         // return the type of a billing element
         if(s.contains("Numero"))
             return "Riparazione";
         else if(s.contains("Codice"))
             return "Articolo";
         else
             return "Altro";
     }
     
     private static void writeColumnsNames (Graphics2D g, FontRenderContext fRend, Font f, int x, int y) {
         // print names of the billing table columns
         g.drawGlyphVector(f.createGlyphVector(fRend, "CAT."), x, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "CODICE"), x + 51, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "DESCRIZIONE"), x + 151, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "Q.TA'"), x + 384, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "PREZZO (€)"), x + 420, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "TOT (€)"), x + 485, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, "IVA"), x + 530, y);
     }
     
     private void writeBillingFooter (Graphics2D g, FontRenderContext fRend, Font b, Font f, int x, int y) {
         // write final billing values
         g.drawGlyphVector(b.createGlyphVector(fRend, "Totale Imponibile"), x, y);
         g.drawGlyphVector(f.createGlyphVector(fRend, this.totalImp.concat("€")), x + 100, y);
         g.drawGlyphVector(b.createGlyphVector(fRend, "Aliquota"), x, y + heightLines(1));
         g.drawGlyphVector(f.createGlyphVector(fRend, this.percentage.concat("%")), x + 100, y + heightLines(1));
         g.drawGlyphVector(b.createGlyphVector(fRend, "Totale Fattura"), x, y + heightLines(2));
         g.drawGlyphVector(f.createGlyphVector(fRend, this.total.concat("€")), x + 100, y + heightLines(2));
     }
     
}