package test;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.image.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class Print2DPrinterJob implements Printable {

	@Override
	public int print(Graphics g,PageFormat pf,int pageIndex) {
	
	        if (pageIndex == 0) {
	                Graphics2D g2d= (Graphics2D)g;
	                g2d.translate(pf.getImageableX(), pf.getImageableY()); 
	                g2d.setColor(Color.black);
	                g2d.drawString("example string", 250, 250);
	                g2d.fillRect(0, 0, 200, 200);
	                return Printable.PAGE_EXISTS;                                   
	        } else {
	                return Printable.NO_SUCH_PAGE;
	        }
	}
	
        public Print2DPrinterJob() {

                /* Construct the print request specification.
                * The print data is a Printable object.
                * the request additonally specifies a job name, 2 copies, and
                * landscape orientation of the media.
                */
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.LANDSCAPE);
                aset.add(new Copies(2));
                aset.add(new JobName("My job", null));
                

                /* Create a print job */
                PrinterJob pj = PrinterJob.getPrinterJob();       
                pj.setPrintable(this);
                /* locate a print service that can handle the request */
                PrintService[] services =
                        PrinterJob.lookupPrintServices();

                if (services.length > 0) {
                        System.out.println("selected printer " + services[0].getName());
                        try {
                                pj.setPrintService(PrintServiceLookup.lookupDefaultPrintService());
                                pj.pageDialog(aset);
//                                if(pj.printDialog(aset)) {
                                        pj.print(aset);
//                                }
                        } catch (PrinterException pe) { 
                                System.err.println(pe);
                        }
                }
        }

        

        public static void main(String arg[]) {

                Print2DPrinterJob sp = new Print2DPrinterJob();
        }
}