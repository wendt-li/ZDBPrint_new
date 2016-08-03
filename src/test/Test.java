package test;

import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
public class Test {
public static void main(String[] args) {
   // TODO Auto-generated method stub
        // step 1: creation of a document-object
        Document document = new Document();      
        try {
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter.getInstance(document, new FileOutputStream("D:\\ChinesePDF005_"+new
java.util.Date().getTime()+".pdf"));
          
            // step 3: we open the document
            document.open();
          
           //  step 4: we add content to the document
            String fontPath = "C:\\Windows\\Fonts";
           // 楷体字
            BaseFont bfComic0 = BaseFont.createFont(fontPath+"\\msyh.ttf",
			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			           // 方正舒体
//			            BaseFont bfComic2 = BaseFont.createFont(fontPath+"\\FZSTK.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			           // 方正姚体
//			            BaseFont bfComic3 = BaseFont.createFont(fontPath+"\\FZYTK.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			//
//			         //   仿宋体
//			            BaseFont bfComic4 = BaseFont.createFont(fontPath+"\\SIMFANG.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			          //  黑体
//			            BaseFont bfComic5 = BaseFont.createFont(fontPath+"\\SIMHEI.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			           // 华文彩云
//			            BaseFont bfComic6 = BaseFont.createFont(fontPath+"\\STCAIYUN.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			            //华文仿宋
//			            BaseFont bfComic7 = BaseFont.createFont(fontPath+"\\STFANGSO.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			           // 华文细黑
//			            BaseFont bfComic8 = BaseFont.createFont(fontPath+"\\STXIHEI.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			          //  华文新魏
//			            BaseFont bfComic9= BaseFont.createFont(fontPath+"\\STXINWEI.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			            //华文行楷
//			            BaseFont bfComic0 = BaseFont.createFont(fontPath+"\\STXINGKA.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			           // 华文中宋
//			            BaseFont bfComic99 = BaseFont.createFont(fontPath+"\\STZHONGS.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			            //隶书
//			            BaseFont bfComic11= BaseFont.createFont(fontPath+"\\SIMLI.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			            //宋体&新宋体    (这种字体的输出不了.有问题)
//			            BaseFont bfComic12 = BaseFont.createFont(fontPath+"\\SIMSUN.TTC", null,
//			BaseFont.NOT_EMBEDDED, BaseFont.NOT_EMBEDDED, null, null);
//			            //宋体-方正超大字符集
//			            BaseFont bfComic13 = BaseFont.createFont(fontPath+"\\SURSONG.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//			            //幼圆
//			            BaseFont bfComic14 = BaseFont.createFont(fontPath+"\\SIMYOU.TTF",
//			BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfComic0, 14);
            String text1 = "啊发生的发球特工是大哥是法国时的风格是This is the quite popular True Typefont (繁體字測試VS简体字测试) ==>"+new java.util.Date();
            document.add(new Paragraph(text1, font));
        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        }
        catch(IOException ioe) {
            System.err.println(ioe.getMessage());
        }      
        // step 5: we close the document
        document.close();
        System.out.println(">>> Export : "+"D:\\ChinesePDF005__.pdf");
}
}