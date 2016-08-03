package test;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.swing.JFileChooser;

public class TestPrint {
//
//	/**
//	 * @param args
//	 */
//	 public static void main(String[] args) {  
//	        JFileChooser fileChooser = new JFileChooser(); //创建打印作业  
////	        int state = fileChooser.showOpenDialog(null);  
////	        if(state == fileChooser.APPROVE_OPTION){  
//	            File file = new File("D:/智店宝--打印设计.doc"); //获取选择的文件  
//	            //构建打印请求属性集  
//	            HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();  
//	            //设置打印格式，因为未确定类型，所以选择autosense  
//	            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;  
//	            //查找所有的可用的打印服务  
//	            PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);  
//	            //定位默认的打印服务  
//	            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();  
//	            //显示打印对话框  
//	            PrintService service = ServiceUI.printDialog(null, 200, 200, printService,   
//	                    defaultService, flavor, pras);  
//	            if(service != null){  
//	                try {  
//	                    DocPrintJob job = service.createPrintJob(); //创建打印作业  
//	                    FileInputStream fis = new FileInputStream(file); //构造待打印的文件流  
//	                    DocAttributeSet das = new HashDocAttributeSet();  
//	                    Doc doc = new SimpleDoc(fis, flavor, das);  
//	                    job.print(doc, pras);  
//	                } catch (Exception e) {  
//	                    e.printStackTrace();  
//	                }  
//	            }  
////	        }  
//	    }  
	public static void main(String[] args) throws Exception {
		testPrint3();
	}
	 
	 public static void testPrint() throws Exception {
		    DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
		    PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
//		    patts.add(Sides.DUPLEX);
		    PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
		    if (ps.length == 0) {
		        throw new IllegalStateException("No Printer found");
		    }
		    System.out.println("Available printers: " + Arrays.asList(ps));

		    PrintService myService = null;
		    for (PrintService printService : ps) {
		        if (printService.getName().equals("Your printer name")) {
		            myService = printService;
		            break;
		        }
		    }
		    myService  = myService==null?PrintServiceLookup.lookupDefaultPrintService():myService;  
		    if (myService == null) {
		        throw new IllegalStateException("Printer not found");
		    }

		    FileInputStream fis = new FileInputStream("D:/test.txt");
		    Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		    DocPrintJob printJob = myService.createPrintJob();
		    printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
		    fis.close();        
		}
	 
	 public static boolean testPrint2()throws Exception
	 {
		 PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);//deleted patts and flavor
		 if (ps.length == 0) {
			 throw new IllegalStateException("No Printer found");
		 }
		 PrintService myService = null;
		 String printer = "";
		 for (PrintService printService : ps) {
			 if (printService.getName().equals(printer)) {
			 myService = printService;
			 break;
			 }
		 }
		 myService  = myService==null?PrintServiceLookup.lookupDefaultPrintService():myService;  
		 if (myService == null) {
			 throw new IllegalStateException("Printer not found");
		 }
		 boolean status ;
		 File file  = new File("D:/test.txt");
		 FileInputStream fis = new FileInputStream(file);
		 Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		 DocPrintJob printJob = myService.createPrintJob();
		 printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
		 fis.close();
		 status = true;
		 return status;

	 }
	 
	 public static void testPrint3() throws Exception {
	        String filename = "D:/test.txt";
	        //PrintRequestAttributeSet实例。
	        //这用来弹出显示的对话框，并在对话框消失之前返回用户所作的任何更改。
	        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
	        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);//用户可选用的PrintService实例数组。
	        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService(); //默认的PrintService
	        /*为用户提供一个选择 PrintService（打印机）的对话框。
	            gc - 用于选择屏幕。null 意味着主屏幕或默认屏幕。
	            x - 对话框在屏幕坐标中的位置，包括边框
	            y - 对话框在屏幕坐标中的位置，包括边框
	            services - 可浏览的服务，必须不为 null。
	            defaultService - 要显示的初始 PrintService。
	            flavor - 要打印的 flavor，或者为 null。
	            attributes - 输入时为应用程序最初提供的首选项。这不能为 null，但可以为空。输出时为反映用户所作的更改的属性。
	        */ 
	        PrintService service = ServiceUI.printDialog(null, 200, 200,printService, defaultService, flavor, pras);
	        if (service != null) {
	            DocPrintJob job = service.createPrintJob();  //创建打印任务
	            FileInputStream fis = new FileInputStream(filename);
	            DocAttributeSet das = new HashDocAttributeSet();
	            /*
	             * 定义要打印的文档,SimpleDoc(,,)里有三个参数:
	             * 　　   ·Object 代表要打印的内容
	　　           *      ·DocFlavor的一个实例描述数据类型
	　　           *      ·可选的DocAttributeSet 包含打印时的属性
	             */
	            Doc doc = new SimpleDoc(fis, flavor, das);
	            /*   启动打印 job.print( , )
	             * doc - 要打印的文档。如果必须是一个 flavor，则此 PrintJob 必须支持它。
	             *  attributes - 应用到此 PrintJob 的作业属性。如果此参数为 null，则使用默认属性。 
	             * */
	            job.print(doc, pras);
	            Thread.sleep(10000);
	        }
	        System.exit(0);
	    }

}
