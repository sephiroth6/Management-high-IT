package managgui;


import java.awt.print.*;
import java.awt.*;
/**
 *
 * @author Angelo
 */
public class Stampa implements Printable {

    public static void main(String ag[]) throws Exception {
          PrinterJob pj = PrinterJob.getPrinterJob();
          pj.setJobName("Tip's & Trick come stampare in java");
          pj.printDialog();

          pj.setPrintable(new Stampa());
          pj.print();
    }

    public int print(Graphics grap, PageFormat pageFormat, int pageIndex) throws PrinterException {

        if(pageIndex > 0)
            return NO_SUCH_PAGE;

        grap.setColor(Color.BLACK);
        grap.drawString("Ciao, sono stato stampato in java", (int)pageFormat.getImageableX(), (int)pageFormat.getImageableY()+5);
        grap.setColor(Color.GREEN);
        grap.drawLine((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY(), (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());      

        return PAGE_EXISTS;
    }
    
}
