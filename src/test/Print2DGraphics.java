package test;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.image.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class Print2DGraphics implements Printable {

        public Print2DGraphics() {

                /* Construct the print request specification.
                * The print data is a Printable object.
                * the request additonally specifies a job name, 2 copies, and
                * landscape orientation of the media.
                */
                DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(OrientationRequested.LANDSCAPE);
                aset.add(new Copies(2));
                aset.add(new JobName("My job", null));

                /* locate a print service that can handle the request */
                PrintService[] services =
                        PrintServiceLookup.lookupPrintServices(flavor, aset);

                if (services.length > 0) {
                        System.out.println("selected printer " + services[0].getName());

                        /* create a print job for the chosen service */
                        DocPrintJob pj = services[0].createPrintJob();

                        try {
                                /* 
                                * Create a Doc object to hold the print data.
                                */
                                Doc doc = new SimpleDoc(this, flavor, null);

                                /* print the doc as specified */
                                pj.print(doc, aset);

                                /*
                                * Do not explicitly call System.exit() when print returns.
                                * Printing can be asynchronous so may be executing in a
                                * separate thread.
                                * If you want to explicitly exit the VM, use a print job 
                                * listener to be notified when it is safe to do so.
                                */

                        } catch (PrintException e) { 
                                System.err.println(e);
                        }
                }
        }

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

        public static void main(String arg[]) {
                Print2DGraphics sp = new Print2DGraphics();
        }
}