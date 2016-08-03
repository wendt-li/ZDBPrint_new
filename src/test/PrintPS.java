package test;

import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;

public class PrintPS {

        public static void main(String args[]) {
               new PrintPS();
        }
        public PrintPS() {
                /* Construct the print request specification.
                 * The print data is Postscript which will be 
                 * supplied as a stream.  The media size 
                 * required is A4, and 2 copies are to be printed
                 */
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                PrintRequestAttributeSet aset = 
                        new HashPrintRequestAttributeSet();
                aset.add(MediaSizeName.ISO_A4);
                aset.add(new Copies(2));
                aset.add(Sides.TWO_SIDED_LONG_EDGE);
                aset.add(Finishings.STAPLE);

                /* locate a print service that can handle it */
                PrintService[] pservices =
                        PrintServiceLookup.lookupPrintServices(flavor, aset);
                if (pservices.length > 0) {
                        System.out.println("selected printer " + pservices[0].getName());

                        /* create a print job for the chosen service */
                        DocPrintJob pj = pservices[0].createPrintJob();
                        try {
                                /* 
                                * Create a Doc object to hold the print data.
                                * Since the data is postscript located in a disk file,
                                * an input stream needs to be obtained
                                * BasicDoc is a useful implementation that will if requested
                                * close the stream when printing is completed.
                                */
                                FileInputStream fis = new FileInputStream("example.ps");
                                Doc doc = new SimpleDoc(fis, flavor, null);

                                /* print the doc as specified */
                                pj.print(doc, aset);

                                /*
                                * Do not explicitly call System.exit() when print returns.
                                * Printing can be asynchronous so may be executing in a
                                * separate thread.
                                * If you want to explicitly exit the VM, use a print job 
                                * listener to be notified when it is safe to do so.
                                */

                        } catch (IOException ie) { 
                        	ie.printStackTrace();
                        } catch (PrintException e) { 
                        	e.printStackTrace() ;
                        }
                }else{
                	System.out.println("not exists printers");
                }
        }
}

