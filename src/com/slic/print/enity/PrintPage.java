package com.slic.print.enity;


/**
 * 整页
 * 
 * @author liurong
 * @Date 2016-05-23
 * @notes 页面
 * 
 */
public class PrintPage {

	/**
	 * 打印方向: 0- 纵向 1-横向
	 */
	private int printDirection;
	/**
	 * 是否分页: true-分页,false-不分页
	 */
	private boolean isPagination;
	
	/**
	 * 整页高度
	 */
	private int height;
	
	/**
	 * 整页宽度
	 */
	private int width;
	/**
	 * 打印比率
	 */
	private int printScale;
	
	/**
	 * 整页间距
	 */
	private PrintMargin margin;

	/**
	 * 顶部
	 */
	private PrintPageRow topRow;
	
	/**
	 * 打印头部
	 */
	private PrintPageHeader header;
	
	/**
	 * 打印表格
	 */
	private PrintPageDetail detail;
	
	/**
	 * 打印汇总
	 */
	private PrintPageSummary summary;
	
	/**
	 * 打印尾部
	 */
	private PrintPageFooter footer;
	
	/**
	 * 底部
	 */
	private PrintPageRow bottomRow;
	
	public PrintPage(int height, int width, PrintMargin margin) {
		super();
		this.height = height;
		this.width = width;
		this.margin = margin;
	}

	/**
	 * 初始化页面每个区域
	 */
	public void init() {
		isPagination = height != 0;
		// 计算头高度与是否重
		if (header != null) {
			header.init();
		}
		// 计算脚高度与是否重打
		if (footer != null) {
			footer.init();
		}
		// 计算汇总区域高度
		if (summary != null) {
			summary.init();
		}
		// 计算汇总区域高度
		if (detail != null) {
			detail.init();
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public PrintMargin getMargin() {
		return margin;
	}

	public void setMargin(PrintMargin margin) {
		this.margin = margin;
	}

	public boolean isPagination() {
		return isPagination;
	}

	public void setPagination(boolean isPagination) {
		this.isPagination = isPagination;
	}

	
	public int getPrintDirection() {
		return printDirection;
	}
	
	public void setPrintDirection(int printDirection) {
		this.printDirection = printDirection;
	}

	public PrintPageHeader getHeader() {
		return header;
	}

	public void setHeader(PrintPageHeader header) {
		this.header = header;
	}

	public PrintPageDetail getDetail() {
		return detail;
	}

	public void setDetail(PrintPageDetail detail) {
		this.detail = detail;
	}

	public PrintPageSummary getSummary() {
		return summary;
	}

	public void setSummary(PrintPageSummary summary) {
		this.summary = summary;
	}

	public PrintPageFooter getFooter() {
		return footer;
	}

	public void setFooter(PrintPageFooter footer) {
		this.footer = footer;
	}

	/**
	 * 打印比率
	 * 
	 * @return
	 */
	public int getPrintScale() {
		return printScale;
	}

	public void setPrintScale(int printScale) {
		this.printScale = printScale;
	}

	public PrintPageRow getTopRow() {
		return topRow;
	}

	public void setTopRow(PrintPageRow topRow) {
		this.topRow = topRow;
	}

	public PrintPageRow getBottomRow() {
		return bottomRow;
	}

	public void setBottomRow(PrintPageRow bottomRow) {
		this.bottomRow = bottomRow;
	}
}
