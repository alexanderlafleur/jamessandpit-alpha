package util;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Vector;

public class FontFinder {

	public Vector loadChineseFonts() {
		// Determine which fonts support Chinese here ...
		Vector<Font> chineseFonts = new Vector<Font>();
		Font[] allfonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAllFonts();
		int fontcount = 0;
		String chinesesample = "\u4e00";
		for (Font element : allfonts) {
			if (element.canDisplayUpTo(chinesesample) == -1) {
				Font font = element;

				System.out.println(font.getFontName() + " " + font.getName()
						+ " " + font.getPSName());
				chineseFonts.add(font);

				// Font loadedFont = Font.getFont(font.getFontName());
				// loadedFont = Font.getFont(font.getName());
				// loadedFont = Font.getFont(font.getPSName());

			}
			fontcount++;
		}
		return chineseFonts;
	}
}
