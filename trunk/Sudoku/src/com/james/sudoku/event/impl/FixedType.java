package com.james.sudoku.event.impl;

import com.james.sudoku.event.ClickType;

public class FixedType implements ClickType {
	private int i;

	public FixedType(int i) {
		this.setI(i);
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
}