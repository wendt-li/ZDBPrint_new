package com.slic.print.enity;

import java.util.List;

/**
 * 汇总
 * 
 * @author liurong
 * @Date 2016-5-23
 * @notes 汇总
 * 
 */
public class PrintPageSummary {

	private int height;
	private int width;
	private PrintMargin margin;
	private List<PrintPageRow> rows;

	public PrintPageSummary() {

	}

	public PrintPageSummary(PrintMargin margin, List<PrintPageRow> rows) {
		super();
		this.margin = margin;
		this.rows = rows;
	}

	/**
	 * 初始化高度
	 */
	public void init() {
		// 计算高度
		height = 0;
		if (rows == null || rows.isEmpty()) {
			return;
		}
		for (PrintPageRow row : rows) {
			height += row.getHeight();
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

	public List<PrintPageRow> getRows() {
		return rows;
	}

	public void setRows(List<PrintPageRow> rows) {
		this.rows = rows;
	}

}
