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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;

import com.slic.utils.HSSFUtil;

public class ex {
	public static void main(String[] args) {
		/**
		 * @see <a
		 *      href="http://poi.apache.org/hssf/quick-guide.html#NewWorkbook">For
		 *      more</a>
		 */
		// 创建新的Excel 工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
		HSSFSheet sheet = workbook.createSheet();
		// HSSFSheet sheet = workbook.createSheet("SheetName");



		HSSFRow row5 = sheet.createRow((short) 0);
		
		// 14.56 * 40 =1cm; 14.56 * 20 = 0.5cm
//		row5.setHeight((short) (14.56 * 20));
		row5.setHeight((short) HSSFUtil.cmToRowHeightUnit(2));
//		row5.setHeightInPoints((float)(2/2.54*72));
//		row5.setHeight((short)(2/2.54*72*20));
		
//		row5.setHeight((short) (15.325 * 38)); // 38px * 15.325 = 1cm
//		row5.setHeightInPoints(29); // 1cm = 28.50 = 38px; 1mm=2.835磅 
		
		// 40px * 36.2 =1cm; 20px * 36.2 = 0.5cm 
		short v = (short) (34.5 * 80); 	
		sheet.setColumnWidth(0, v);    // 1400 = 1cm =40px
	    sheet.setColumnWidth(1, HSSFUtil.cmToSheetWidthUnit(0.5));    
	    sheet.setColumnWidth(2, 1800);  
		sheet.setMargin(HSSFSheet.TopMargin, HSSFUtil.mmToSheetMarginUnit(3));//1 = 2.5cm  0.4 = 1cm
		sheet.setMargin(HSSFSheet.BottomMargin, HSSFUtil.mmToSheetMarginUnit(3));// ҳ�߾ࣨ�£�
		sheet.setMargin(HSSFSheet.LeftMargin,HSSFUtil.mmToSheetMarginUnit(3));// ҳ�߾ࣨ��
		sheet.setMargin(HSSFSheet.RightMargin,HSSFUtil.mmToSheetMarginUnit(3));// ҳ�߾ࣨ�ң�
		//设置单元格边框
        HSSFCellStyle style = workbook.createCellStyle();      
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框        
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框        
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框        
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框   
        row5.createCell(0).setCellStyle(style);
		

		/**************************** 导出execl ****************************/
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream("d:/344.xls");
			workbook.write(fileOut);
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				fileOut.close();
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
