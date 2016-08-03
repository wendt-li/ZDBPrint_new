package test.we;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.jdom.Attribute;

public class fffffff {

	public static void main(String[] args) {
        /** 
         * @see <a href="http://poi.apache.org/hssf/quick-guide.html#NewWorkbook">For more</a>
         */        
        // 创建新的Excel 工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
        HSSFSheet sheet = workbook.createSheet();
        //HSSFSheet sheet = workbook.createSheet("SheetName"); 
        
        // 用于格式化单元格的数据
        HSSFDataFormat format = workbook.createDataFormat();
        
        // 创建新行(row),并将单元格(cell)放入其中. 行号从0开始计算.
        HSSFRow row = sheet.createRow((short) 1);

        // 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 20); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
        font.setItalic(true); //是否使用斜体
        font.setStrikeout(true); //是否使用划线
      //下划线
        font.setUnderline(HSSFFont.U_NONE);

        // 设置单元格类型
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //水平布局：居中
        cellStyle.setWrapText(true);
        
        cellStyle.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        

        
        // 添加单元格注释
        // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器.
        HSSFPatriarch patr = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者. 当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("Xuys.");
        
        // 创建单元格
        HSSFCell cell = row.createCell((short) 1);
        HSSFRichTextString hssfString = new HSSFRichTextString("Hello World!");
        cell.setCellValue(hssfString);//设置单元格内容
        cell.setCellStyle(cellStyle);//设置单元格样式
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);//指定单元格格式：数值、公式或字符串
        cell.setCellComment(comment);//添加注释

        //格式化数据
        row = sheet.createRow((short) 2);
        cell = row.createCell((short) 2);
        cell.setCellValue(11111.25);
        cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(format.getFormat("0.0"));
        cellStyle.setFillForegroundColor(HSSFColor.SEA_GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        cellStyle.setFillBackgroundColor(HSSFColor.AQUA.index);     
//        cellStyle.setFillPattern(HSSFCellStyle.BIG_SPOTS);  
        cell.setCellStyle(cellStyle);

        row = sheet.createRow((short) 3);
        cell = row.createCell((short) 3);
        cell.setCellValue(9736279.073);
        cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(format.getFormat("#,##0.0000"));
        cell.setCellStyle(cellStyle);
        
        
        sheet.autoSizeColumn((short)0); //调整第一列宽度
        sheet.autoSizeColumn((short)1); //调整第二列宽度
        sheet.autoSizeColumn((short)2); //调整第三列宽度
        sheet.autoSizeColumn((short)3); //调整第四列宽度
        
        HSSFRow row5 = sheet.createRow((short) 1);
        row5.setHeight((short) (15.025 * 38));
        short v = (short) (36.5*38);
		sheet.setColumnWidth(5, v);
        
        /**************************** sheet2 ****************************/
//      workbook.getSheetAt(1)
        HSSFSheet sheet2 =  workbook.createSheet("hebing");  
        
        row = sheet2.createRow((short) 10);
        cell = row.createCell((short) 10);
        cell.setCellValue(11111.25);
        
        //获取列所在 行 列 
        System.out.println(cell.getRowIndex() + " " + cell.getColumnIndex());
        
        //设置单元格边框
        HSSFCellStyle style = workbook.createCellStyle();      
        style.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);//下边框        
        style.setBorderLeft(HSSFCellStyle.BORDER_DASH_DOT);//左边框        
        style.setBorderRight(HSSFCellStyle.BORDER_DASHED);//右边框        
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框    
        style.setDataFormat(format.getFormat("0.0"));
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//左右居中       
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//上下居中       
        cell.setCellStyle(style);
        
        // 合并从第1行1列 3行3列
        CellRangeAddress cellRangeAddress = new CellRangeAddress(30, 40, 10, 20);
        sheet2.addMergedRegion(cellRangeAddress); 
        // 设置合并边框
        RegionUtil.setBorderBottom(1, cellRangeAddress, sheet2, workbook);
        
        // 添加选框
        CellRangeAddressList regions = new CellRangeAddressList(0, 0, 0, 0);
        sheet2.createRow(0).createCell(0).setCellValue("man1");
			String enumValue = "man,wuman";
			//加载下拉列表内容
			DVConstraint constraint = DVConstraint.createExplicitListConstraint(enumValue.split(","));
			//数据有效性对象
			HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
			sheet2.addValidationData(dataValidation);
			
		/**************************** 导出execl ****************************/		
        FileOutputStream fileOut = null;
        try {
        	fileOut = new FileOutputStream("d:/3.xls");
            workbook.write(fileOut);
        } catch (Exception e) {
            System.out.println(e.toString());
        }finally{
        	try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
    }
}
