package ui;
import javax.swing.JLabel;

public class PronunciationComponent extends JLabel {

	private static final long serialVersionUID = 1L;

	public void updateView(String text) {
		this.setText(text);
	}
}
