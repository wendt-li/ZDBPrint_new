package test ;

import java.awt.image.BufferedImage ;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException ;

import javax.imageio.ImageIO ;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor ;
import org.apache.poi.hssf.usermodel.HSSFPatriarch ;
import org.apache.poi.hssf.usermodel.HSSFSheet ;
import org.apache.poi.hssf.usermodel.HSSFWorkbook ;

public class ExcelImageTest {  
    public static void main(String[] args) {  
         FileOutputStream fileOut = null;     
         BufferedImage bufferImg = null;     
        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
        try {  
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
            bufferImg = ImageIO.read(new File("d:/1416299501.png"));     
            ImageIO.write(bufferImg, "jpg", byteArrayOut);  
              
            HSSFWorkbook wb = new HSSFWorkbook();     
            HSSFSheet sheet1 = wb.createSheet("test picture");    
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
            HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();     
            //anchor主要用于设置图片的属性  

//参数 说明 dx1 第1个单元格中x轴的偏移量
// dy1 第1个单元格中y轴的偏移量
// dx2 /第2个单元格中x轴的偏移量
// dy2 第2个单元格中y轴的偏移量
// col1 第1个单元格的列号
// row1 第1个单元格的行号
// col2 第2个单元格的列号
// row2 第2个单元格的行号
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,(short) 1, 1, (short) 5, 10);     
            anchor.setAnchorType(3);     
            //插入图片    
            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));   
            fileOut = new FileOutputStream("D:/测试Excel.xls");     
            // 写入excel文件     
             wb.write(fileOut);     
             System.out.println("----Excle文件已生成------");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            if(fileOut != null){  
                 try {  
                    fileOut.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
} 