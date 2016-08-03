package com.slic.print.enity;

import org.jdom.Element;

/**
 * 行对象
 * 
 * @author liurong
 * @Date  2016年7月14日
 * @notes   
 *
 */
public class PrintPageRow {

	/**
	 * 每行高度
	 */
	private int height;
	
	/**
	 * 每行宽度
	 */
	private int width;
	/**
	 * 间距
	 */
	private PrintMargin margin;
	/**
	 * 是否重复打印
	 */
	private boolean isPrintOnNewPage;
	/**
	 * 行类型：2 行头  13行尾  10表格头  11表格内容
	 */
	private int rowType;
	/**
	 * 每行节点
	 */
	private Element rowEle;

	public PrintPageRow() {

	}

	public PrintPageRow(int height, int width, PrintMargin margin) {
		super();
		this.height = height;
		this.width = width;
		this.margin = margin;
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

	/**
	 * 是否在新界面打印(每一页都打印)
	 * 
	 * @return
	 */
	public boolean isPrintOnNewPage() {
		return isPrintOnNewPage;
	}

	public void setPrintOnNewPage(boolean isPrintOnNewPage) {
		this.isPrintOnNewPage = isPrintOnNewPage;
	}

	public int getRowType() {
		return rowType;
	}

	public void setRowType(int rowType) {
		this.rowType = rowType;
	}

	public Element getRowEle() {
		return rowEle;
	}

	public void setRowEle(Element rowEle) {
		this.rowEle = rowEle;
	}

}
