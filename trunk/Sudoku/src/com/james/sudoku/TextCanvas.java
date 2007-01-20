/*
 * TextCanvas.java
 *
 * Created on 5 October 2006, 09:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.james.sudoku;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

public class TextCanvas extends Canvas {
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        
        // Create a black background
        g.setColor(0);
        g.fillRect(0, 0, width, height);
        
        // Top-left of canvas
        g.setColor(0xffffff);
        g.drawString("Top left", 0, 0, Graphics.TOP | Graphics.LEFT);
        
        // Draw another string one line below
        Font font = g.getFont();
        g.drawString("Below top left", 0, font.getHeight(), Graphics.TOP
                | Graphics.LEFT);
        
        // Bottom-right of canvas
        g.drawString("Bottom right", width, height, Graphics.BOTTOM
                | Graphics.RIGHT);
        
        // Mixed fonts and colors
        String str = "Multi-font ";
        font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_UNDERLINED,
                Font.SIZE_LARGE);
        g.setFont(font);
        g.drawString(str, 0, height / 2, Graphics.LEFT | Graphics.BASELINE);
        
        int x = font.stringWidth(str);
        g.setColor(0x00ff00);
        g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD
                | Font.STYLE_ITALIC, Font.SIZE_MEDIUM));
        g.drawString("and multi-color", x, height / 2, Graphics.LEFT
                | Graphics.BASELINE);
    }
}
