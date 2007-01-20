package com.james.sudoku.event.impl;

import com.james.sudoku.event.ClickType;

public class GridType implements ClickType {
	private int row;

	private int col;

	public GridType(int row, int col) {
		setRow(row);
		setCol(col);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}