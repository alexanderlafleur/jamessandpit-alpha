package controller;

import java.util.Collections;
import java.util.List;

import model.Choice;
import ui.Category;

public class ChineseCategory extends Category {

	private static final long serialVersionUID = 1L;

	public ChineseCategory(AppController controller) {
		super(controller);
	}

	@Override
	public String getChoiceValue(Choice choice) {
		return choice.getChinese();
	}

	@Override
	public void shuffle() {
		if (!isFixed()) {
			List<String> currentData = toList(getModel());
			Collections.shuffle(currentData);

			setListData(currentData.toArray());
		}
	}

}
