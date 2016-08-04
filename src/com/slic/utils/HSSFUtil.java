package com.slic.utils;

public class HSSFUtil {

//	 private static final short TWIPS_PER_PIEXL = 15; //1 Pixel = 1440 TPI / 96 DPI = 15 Twips  
//     
//	    public static short pixel2PoiHeight(int pixel) {  
//	        return (short) (pixel * TWIPS_PER_PIEXL);  
//	    }  
//	  
//	    public static int poiHeight2Pixel(short height) {  
//	        return height / TWIPS_PER_PIEXL;  
//	    }  
	    
	/**
	 * 毫米单位转换为像素单位 用户计算行高与宽高
	 * @param mmVal 毫米的10倍
	 * @return
	 */
	    public static float mm10ToPX(int mm10Val){
	    	return (float) (mm10Val*1.00/35.3*20*0.75);
	    }
	    
	    /**
	     * 毫米10倍转换为英寸
	     * @param mmVal
	     * @return
	     */
	    public static double  mm10ToInc(int mm10Val){
	    	return (float) (mm10Val*1.00/254);
	    }
	    
	    /****************** 单位换算约定  *******************/
	    /**
	     * 宽度约定：1cm = 40px
	     * @param cm
	     * @return
	     */
	    public static int cmWidthToPX(String cm) {
	    	return (int) (Float.parseFloat(cm)*40);
	    }
	    /**
	     * 宽度约定：1mm = 4px
	     * @param cm
	     * @return
	     */
	    public static int mmWidthToPX(String mm) {
	    	return (int) (Float.parseFloat(mm)*4);
	    }
	    /**
	     * 宽度约定：1px = 1/40cm
	     * @return
	     */
	    public static double pxToWidthCM(int px) {
	    	return (px/40.0);
	    }
	    /**
	     * 宽度约定：1px = 1/4mm
	     * @return
	     */
	    public static double pxToWidthMM(int px) {
	    	return (px/4.0);
	    }
	    /******************/
	    /**
	     * 高度约定：1cm = 50px
	     * @param cm
	     * @return
	     */
	    public static int cmHeightToPX(String cm) {
	    	return (int) (Float.parseFloat(cm)*50);
	    }
	    /**
	     * 高度约定：1mm = 5px
	     * @param cm
	     * @return
	     */
	    public static int mmHeightToPX(String mm) {
	    	return (int) (Float.parseFloat(mm)*5);
	    }
	    /**
	     * 高度约定：1px = 1/50cm
	     * @return
	     */
	    public static double pxToHeightCM(int px) {
	    	return (px/50.0);
	    }
	    /**
	     * 高度约定：1px = 1/5mm
	     * @return
	     */
	    public static double pxToHeightMM(int px) {
	    	return (px/5.0);
	    }
	    /****************** row高度单位转换  *******************/
	    /**
	     * row高度cm转换 height单位
	     * @param cm
	     * @return
	     */
	    public static short cmToRowHeightUnit(double cm) {
	    	//return (short) (Float.parseFloat(cm)*14.56*40);
	    	return (short) (cm/2.54*72*20);
	    }
	    /**
	     * row高度mm转换 height单位
	     * @param mm
	     * @return
	     */
	    public static short mmToRowHeightUnit(double mm) {
	    	//return (short) (Float.parseFloat(mm)*14.56*4);
	    	return (short) (mm/2.54*72*2);
	    }
	    
	    /****************** sheet列宽单位转换  *******************/
	    /**
	     * sheet宽度cm转换 width单位 
	     * @param cm
	     * @return
	     */
	    public static int cmToSheetWidthUnit(double cm) {
	    	return (int) (cm*36.1*40);
	    }
	    /**
	     * sheet宽度mm转换 width单位 
	     * @param mm
	     * @return
	     */
	    public static int mmToSheetWidthUnit(double mm) {
	    	return (int) (mm*36.1*4);
	    }
	    
	    /****************** sheet边距单位转换  *******************/
	    /**
	     * sheet边距 mm转换 margin单位( 0.4f = 1cm)
	     * @param cm
	     * @return
	     */
	    public static float mmToSheetMarginUnit(int mm) {
	    	return (float) (mm*0.04);
	    }
}
