package com.james.sudoku.view.grid;

import com.james.sudoku.model.ModelCell;
import com.james.sudoku.view.BaseDrawableImpl;
import com.james.sudoku.view.Renderer;
import java.util.Enumeration;
import javax.microedition.lcdui.Graphics;

public class GridCellNumber extends BaseDrawableImpl {

	private static final int LIGHT_GREY = 0x777777;

	private static final int WHITE = 0xffffff;

	private static final int RED = 0xff0000;

	private static final int GREEN = 0x00ff00;

	private static final int BLUE = 0x0000ff;

	private static final int CYAN = 0x00ffff;

	private static final int YELLOW = 0xffff00;

	private static final int MAGENTA = 0xff00ff;

	private static final int ORANGE = 0xff7f00;

	private static final int VIOLET = 0x8b00ff;

	private static final int GREY = 0xaaaaaa;

	private static int COLOURS[] = new int[] { RED, GREEN, BLUE, CYAN, ORANGE,
			MAGENTA, YELLOW, VIOLET, GREY };

	public static int getColour(int num) {
		return COLOURS[num - 1];
	}

	public GridCellNumber(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	private int getColor(boolean fixed, int val) {
		int cellColor;
		// if (fixed) {
		// cellColor = LIGHT_GREY;
		// } else {
		cellColor = COLOURS[val - 1];
		// }

		return cellColor;
	}

	public void draw(Graphics g, ModelCell model, boolean fixed) {

		int i = 0;

		int size = model.getValues().size();

		for (Enumeration e = model.getValues().elements(); e.hasMoreElements();) {

			Integer integerVal = (Integer) e.nextElement();

			int intVal = integerVal.intValue();

			g.setColor(getColor(fixed, intVal));

			if (size > 6) {
				drawAsBoxes(g, i);
			} else {
				drawAsNumbers(g, model, size, intVal, i, fixed);
			}
			if (intVal != 0) {
				i++;
			}

		}
	}

	private void drawAsBoxes(Graphics g, int i) {

		int width = getWidth() / 3;
		int height = getHeight() / 3;

		int x = getX() + (i % 3) * width;
		int y = getY() + (i / 3 * height);

		g.fillRect(x, y, width, height);
	}

	private void drawAsNumbers(Graphics g, ModelCell model, int size,
			int intVal, int i, boolean fixed) {
		if (size == 1) {
			if (model.isFixed()) {
				g.setFont(Renderer.LARGE_FONT);
			} else {
				g.setFont(Renderer.LARGE_BOLD_FONT);
			}

		} else {
			g.setFont(Renderer.SMALL_FONT);
		}

		int fontWidth = g.getFont().charWidth('2');
		int fontHeight = g.getFont().getHeight();

		if (intVal != 0) {
			int y1;
			int x1;

			if (size > 1) {
				x1 = getX() + (i % 3) * fontWidth;
				y1 = getY() + (i / 3 * (fontHeight - 4));

			} else {
				x1 = getX() + getWidth() / 2 - fontWidth / 2;
				y1 = getY();

			}

			g.drawString(String.valueOf(intVal), x1, y1, Graphics.TOP
					| Graphics.LEFT);
		}
	}
}
