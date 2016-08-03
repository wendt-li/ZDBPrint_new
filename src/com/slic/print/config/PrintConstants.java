package com.slic.print.config;

/**
 * 
 * 
 * @author liurong
 * @Date 2016-5-23
 * @notes 打印常量
 * 
 */
public class PrintConstants {

	/**
	 * 页面网格每行高度 单位0.1mm
	 */
	public static final int PAGE_BASE_ROW_HEIGHT = 50;

	/**
	 * 页面切换需要保留的纸张高度
	 */
	public static int PAGE_SWITCH_HEIGHT = 120;

	/**
	 * 即打即停每行商品校准微调值 10
	 */
	public static int NOPAGE_GOODROW_CORRECT_HEIGHT = 2;

	/**
	 * 最小底部页边距
	 */
	public static int MIN_MARGIN_BOTTON = 30;

	/**
	 * 即打即停 界面高度校准值
	 */
	public static int NOPAGE_CORRECT_HEIGHT = 20;

	/**
	 * 默认纸张高度 cm
	 */
	public static final double PAGE_DEFAULT_HEIGHT = 13.90;
	/**
	 * 默认纸张宽度 cm
	 */
	public static final double PAGE_DEFAULT_WIDTH = 21.00;

	/**
	 * 默认纸张比率
	 */
	public static final int PAGE_DEFAULT_SCALE = 70;

	/**
	 * 默认打印方向 1-横向,0-纵向 ,默认纵向
	 */
	public static final int PAGE_DEFAULT_PRINTDIRECTION = 0;

	/**
	 * 最大页面高度cm
	 */
	public static final int PAGE_MAX_HEIGHT = 550;

	/**
	 * 最小页面高度cm
	 */
	public static final int PAGE_MIN_HEIGHT = 80;

	/**
	 * 页面行类型
	 * 
	 * @author liurong
	 * @Date 2016-5-23
	 * @notes
	 * 
	 */
	public final static class RowType {
		/**
		 * 页头行
		 */
		public static final int HEDER = 2;

		/**
		 * 行包含表格
		 */
		public static final int DETAIL = 10;
		/**
		 * 汇总行
		 */
		public static final int SUMMARY = 11;
		/**
		 * 页尾行
		 */
		public static final int FOOTER = 13;
	}

}
