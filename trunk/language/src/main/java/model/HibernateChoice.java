package model;

public class HibernateChoice {
	private int attempted;

	private String chinese;

	private int correct;

	private String english;

	private long id;

	private String pronunciation;

	public HibernateChoice() {

	}

	public HibernateChoice(long id, String chinese, String english,
			String pronunciation) {
		setId(id);

		setChinese(chinese);
		setEnglish(english);
		setPronunciation(pronunciation);
	}

	public int getAttempted() {
		return attempted;
	}

	public String getChinese() {
		return chinese;
	}

	public int getCorrect() {
		return correct;
	}

	public String getEnglish() {
		return english;
	}

	public long getId() {
		return id;
	}

	public String getPronunciation() {
		return pronunciation;
	}

	public void setAttempted(int attempted) {
		this.attempted = attempted;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPronunciation(String pronunciation) {
		this.pronunciation = pronunciation;
	}

}
