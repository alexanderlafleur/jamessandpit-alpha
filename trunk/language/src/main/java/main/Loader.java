package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Choice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import controller.UnicodeUtils;
import dao.ChoiceHelper;

public class Loader {

	protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	private static final String DATA_FILE = "data/data.csv";

	private static final String DATA_FILE_TEMP = "data/data.csv.tmp";

	private static String escape(String msg, String from, String to) {
		return msg.replaceAll(from, to);
	}

	public static final void main(String args[]) throws IOException {
		Loader main = (Loader) CONTEXT.getBean("loader");

		boolean loadFromFile = questionPrompt(
				"Do you want to load from file? Default No", false);

		if (loadFromFile) {
			boolean deleteFirst = questionPrompt(
					"Do you want to delete all data first?", false);
			if (deleteFirst) {
				main.clear();
			}
			main.loadFromFile();

			boolean list = questionPrompt(
					"Do you want to list all the loaded data?", true);
			if (list) {
				main.list();
			}

		} else {
			while (true) {
				boolean addAnother = questionPrompt("Add another... (Y/N)",
						true);

				if (!addAnother) {
					break;
				}

				String english = prompt("English definition: ");
				english = escape(english, ",", ";");

				String pronunciation = prompt("Pronunciation: ");

				String chinese = prompt("Unicode: ");

				main.save(new Choice(-1, UnicodeUtils.unescape(chinese).trim(),
						english, pronunciation));
			}

			main.saveToFile();
		}
	}

	private static String prompt(String msg) {
		while (true) {
			System.out.println(msg);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				String line = in.readLine().trim();

				return line;
			} catch (IOException e) {
			}
		}
	}

	private static boolean questionPrompt(String msg, boolean defaultValue) {
		while (true) {
			System.out.println(msg);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				String line = in.readLine().trim();

				if (line.length() == 0) {
					return defaultValue;
				}

				if ("yes".equalsIgnoreCase(line) || "y".equalsIgnoreCase(line)) {
					return true;
				} else {
					return false;
				}

			} catch (IOException e) {
			}
		}
	}

	private ChoiceHelper choiceHelper;

	public Loader() {
	}

	private void clear() {
		getChoiceHelper().clear();
	}

	private void copyFile(String data_file_temp2, String data_file2)
			throws IOException {

		System.out.println("Copying " + DATA_FILE_TEMP + " to " + DATA_FILE);
		FileInputStream fis = new FileInputStream(DATA_FILE_TEMP);
		FileOutputStream fos = new FileOutputStream(DATA_FILE);

		byte buffer[] = new byte[1000000];

		int num = fis.read(buffer);

		fos.write(buffer, 0, num);
	}

	public ChoiceHelper getChoiceHelper() {
		return choiceHelper;
	}

	private void list() {
		List<Choice> all = getChoiceHelper().loadAll();

		Collections.sort(all, new ChoiceComparator());

		for (Choice choice : all) {
			print(choice);

		}
	}

	private List<String[]> loadCSV(BufferedReader is) {
		String line;

		List<String[]> rows = new ArrayList<String[]>();
		while (true) {
			try {
				line = is.readLine();

				if (line == null) {
					break;
				}

				if (line.trim().length() == 0) {
					continue;
				}

				String triple[] = parse(line);

				if (triple.length != 3) {
					throw new LoaderException(
							"Length of triple is not 3, it is: "
									+ triple.length + " for line: " + line);

				}

				rows.add(triple);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return rows;
	}

	// private void loadFromDatabase() {
	//
	// }

	public void loadFromFile() throws IOException {
		BufferedReader is = new BufferedReader(new FileReader(DATA_FILE));

		List<String[]> rows = loadCSV(is);

		for (String s[] : rows) {
			String chineseConverted = UnicodeUtils.unescape(UnicodeUtils
					.unescape(s[0]));

			save(new Choice(-1, chineseConverted, s[1].trim(), s[2].trim()));
		}
	}

	private String[] parse(String line) {
		return line.split(",");
	}

	private void print(Choice choice) {
		System.out.println(choice);
	}

	private void save(Choice choice) {
		getChoiceHelper().save(choice);
	}

	private void saveToFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					DATA_FILE_TEMP));

			List<Choice> choices = getChoiceHelper().loadAll();

			for (Choice choice : choices) {

				out.write(UnicodeUtils.escape(choice.getChinese()) + ","
						+ choice.getEnglish() + "," + choice.getPronunciation()
						+ "\n");
			}

			out.close();

			copyFile(DATA_FILE_TEMP, DATA_FILE);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setChoiceHelper(ChoiceHelper choiceHelper) {
		this.choiceHelper = choiceHelper;
	}
}
