package dao;

public class ChoiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChoiceException(String msg) {
		super(msg);
	}

	public ChoiceException(String msg, Exception e) {
		super(msg, e);
	}
}
