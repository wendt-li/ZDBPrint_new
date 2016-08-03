package com.slic.print.enity;

/**
 * 边距
 * @author liurong
 * @Date 2016-05-23
 * @notes 边距
 * 
 */
public class PrintMargin {

	private int top;
	private int right;
	private int bottom;
	private int left;

	public PrintMargin(int top, int right, int bottom, int left) {
		super();
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

}
