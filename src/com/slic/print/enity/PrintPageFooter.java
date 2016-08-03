package com.slic.print.enity;

import java.util.List;

/**
 * 页脚
 * 
 * @author liurong
 * @Date 2016-5-23
 * @notes 页脚
 * 
 */
public class PrintPageFooter {

	private int height;
	private int width;
	private PrintMargin margin;
	private boolean isPrintOnNewPage;
	private List<PrintPageRow> rows;

	public PrintPageFooter() {

	}

	public PrintPageFooter(PrintMargin margin, boolean isPrintOnNewPage,
			List<PrintPageRow> rows) {
		super();
		this.margin = margin;
		this.isPrintOnNewPage = isPrintOnNewPage;
		this.rows = rows;
	}

	/**
	 * 初始化高度与是否重打
	 */
	public void init() {
		// 计算高度
		height = 0;
		if (rows == null || rows.isEmpty()) {
			return;
		}
		for (PrintPageRow row : rows) {
			height += row.getHeight();
			if (row.isPrintOnNewPage()) {
				// 脚数据只要有一行选择了,就全部重打
				isPrintOnNewPage = true;
			}
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

	public boolean isPrintOnNewPage() {
		return isPrintOnNewPage;
	}

	public void setPrintOnNewPage(boolean isPrintOnNewPage) {
		this.isPrintOnNewPage = isPrintOnNewPage;
	}

	public List<PrintPageRow> getRows() {
		return rows;
	}

	public void setRows(List<PrintPageRow> rows) {
		this.rows = rows;
	}

}
