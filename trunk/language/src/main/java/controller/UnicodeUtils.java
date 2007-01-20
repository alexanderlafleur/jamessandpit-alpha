package controller;

import org.apache.commons.lang.StringEscapeUtils;

public class UnicodeUtils {

	public static String unescape(String escaped) {
		String chineseArray[] = escaped.split(";");

		String converted = new String();

		for (String element : chineseArray) {
			String c = element.trim();
			c = StringEscapeUtils.unescapeJava(c);
			converted += c + " ";
		}

		return converted.trim();
	}

	public static String escape(String unescaped) {
		String chineseArray[] = unescaped.split(" ");

		String converted = new String();

		for (int i = 0; i < chineseArray.length; i++) {
			String c = chineseArray[i].trim();
			c = StringEscapeUtils.escapeJava(c);
			converted += c + (i == chineseArray.length - 1 ? "" : ";");
		}

		return converted.trim();
	}

	// public static String escape(String chinese) {
	// return StringEscapeUtils.escapeHtml(chinese);
	// }

	// public static String fromDatabase(String c) {
	// return StringEscapeUtils.escapeJava(StringEscapeUtils.escapeJava(c));
	// }
	//
	// public static String fromFile(String c) {
	// return StringEscapeUtils.unescapeJava("\\u" + c);
	// }

	// public static String unescape(String chinese) {
	// return StringEscapeUtils.unescapeJava(chinese);
	// }

}
