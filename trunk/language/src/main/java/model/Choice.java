package model;

public class Choice {
	private int attempted;

	private String chinese;

	private int chineseIndex;

	private int correct;

	private String english;

	private int englishIndex;

	private long id;

	private String pronunciation;

	private int pronunciationIndex;

	public Choice() {

	}

	public Choice(long id, String chinese, String english, String pronunciation) {
		setId(id);

		setChinese(chinese);
		setEnglish(english);
		setPronunciation(pronunciation);
	}

	@Override
	public boolean equals(Object o) {
		Choice choice = (Choice) o;

		return choice.getChinese().equals(getChinese())
				&& choice.getEnglish().equals(getEnglish())
				&& choice.getPronunciation().equals(getPronunciation());
	}

	public boolean equals(String c, String e, String p) {
		return c.equals(getChinese()) && e.equals(getEnglish())
				&& p.equals(getPronunciation());
	}

	public int getAttempted() {
		return attempted;
	}

	public String getChinese() {
		return chinese;
	}

	public int getChineseIndex() {
		return chineseIndex;
	}

	public int getCorrect() {
		return correct;
	}

	public String getEnglish() {
		return english;
	}

	public int getEnglishIndex() {
		return englishIndex;
	}

	public long getId() {
		return id;
	}

	public String getPronunciation() {
		return pronunciation;
	}

	public int getPronunciationIndex() {
		return pronunciationIndex;
	}

	public double getScore() {
		if (attempted == 0) {
			return 0.0;
		}
		return ((double) correct) / attempted;
	}

	public void incrementAttempted() {
		attempted++;
	}

	public boolean matches(String c, String e, String p) {
		if (getChinese().equals(c)) {
			if (getEnglish().equals(e)) {
				if (getPronunciation().equals(p)) {
					return true;
				}
			}
		}

		return false;
	}

	public void setAttempted(int attempted) {
		this.attempted = attempted;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public void setChineseIndex(int chineseIndex) {
		this.chineseIndex = chineseIndex;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public void setEnglishIndex(int englishIndex) {
		this.englishIndex = englishIndex;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPronunciation(String pronunciation) {
		this.pronunciation = pronunciation;
	}

	public void setPronunciationIndex(int pronunciationIndex) {
		this.pronunciationIndex = pronunciationIndex;
	}

	@Override
	public String toString() {
		return chinese + "(" + chineseIndex + "), " + english + "("
				+ englishIndex + "), " + pronunciation + "("
				+ pronunciationIndex + "), correct:" + correct + ", attempted:"
				+ attempted + " id:" + id + ", Score: " + getScore();

	}
}
