package util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.lang.StringEscapeUtils;

public class UnicodeLoader {

	public static void main(String[] args) {

		try {
			load();

		} catch (Exception e) {
			System.out.print(e.getMessage());
			System.exit(1);
		}
	}

	public static String load() throws Exception {

		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"UnicodeLoader.txt"), "UTF8");
		Reader in = new BufferedReader(isr);
		char buffer[] = new char[1024];
		int numRead = in.read(buffer);
		String text = new String(buffer, 0, numRead);

		System.out.println(text);

		// Writer out = new OutputStreamWriter(new FileOutputStream("test.txt"),
		// "UTF8");

		String escaped = StringEscapeUtils.unescapeJava(text);

		return escaped;
		// String inFile = "UnicodeLoader.txt";
		// String from = "UTF8";
		//
		// // , "BIG5"
		//
		// InputStream in = new FileInputStream(inFile);
		//
		// // Set up character stream
		// Reader r = new BufferedReader(new InputStreamReader(in));// , from));
		// char buffer[] = new char[1024];
		//
		// int numRead = r.read(buffer);
		//
		// String msg = new String(buffer, 0, numRead);

		// --------------------

		// StringBuffer buffer = new StringBuffer();
		//
		// FileInputStream fis = new FileInputStream("UnicodeLoader.txt");
		// InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		// Reader in = new BufferedReader(isr);
		// int ch;
		// while ((ch = in.read()) > -1) {
		// buffer.append((char) ch);
		// }
		// in.close();
		//
		// System.out.println(buffer.toString());
		//
		// FileOutputStream fos = new FileOutputStream("test.txt");
		// Writer out = new OutputStreamWriter(fos, "UTF8");
		// out.write(buffer.toString());
		// out.close();

	}
}
