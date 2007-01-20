package dao;

import java.util.ArrayList;
import java.util.List;

import model.Choice;
import model.HibernateChoice;

import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import controller.UnicodeUtils;

public class ChoiceHelper extends HibernateDaoSupport {

	public void clear() {
		List<HibernateChoice> all = loadAllHibernateChoices();
		getHibernateTemplate().deleteAll(all);
	}

	public Choice load(String id) throws ChoiceException {
		try {
			HibernateChoice hChoice = (HibernateChoice) getHibernateTemplate()
					.load(HibernateChoice.class, new Long(id));

			return toChoice(hChoice);
		} catch (ObjectNotFoundException e) {
			return null;
		}
	}

	public List<Choice> loadAll() {
		List<HibernateChoice> hChoices = loadAllHibernateChoices();
		return toChoices(hChoices);
	}

	public List<HibernateChoice> loadAllHibernateChoices() {
		return getHibernateTemplate().loadAll(HibernateChoice.class);
	}

	public Choice loadChinese(String c) {
		List<HibernateChoice> list = getHibernateTemplate().find(
				"from HibernateChoice where chinese like ? ",
				new String[] { UnicodeUtils.escape(UnicodeUtils.escape(c)) });

		if (list.size() == 0) {
			throw new ChoiceException("Chinese not found for: " + c);

		} else if (list.size() == 1) {
			return toChoice(list.get(0));

		} else {
			throw new ChoiceException("More than 1 chinese found for: " + c);
		}
	}

	public Choice loadEnglish(String e) {
		List<HibernateChoice> list = getHibernateTemplate().find(
				"from HibernateChoice where english = ? ", e);

		if (list.size() == 0) {
			throw new ChoiceException("English not found for: " + e);

		} else if (list.size() == 1) {
			return toChoice(list.get(0));

		} else {
			throw new ChoiceException("More than 1 english found for: " + e);
		}
	}

	public Choice loadPronunciation(String p) {
		List<HibernateChoice> list = getHibernateTemplate().find(
				"from HibernateChoice where pronunciation = ? ", p);

		if (list.size() == 0) {
			throw new ChoiceException("Pronunciation not found for: " + p);
		} else if (list.size() == 1) {
			return toChoice(list.get(0));
		} else {
			throw new ChoiceException("More than 1 pronunciation found for: "
					+ p);
		}
	}

	public void save(Choice choice) {
		HibernateChoice hChoice = toChoice(choice);

		getHibernateTemplate().save(hChoice);
	}

	private HibernateChoice toChoice(Choice choice) {
		HibernateChoice hChoice = new HibernateChoice();

		hChoice.setChinese(UnicodeUtils.escape(choice.getChinese()));
		hChoice.setEnglish(choice.getEnglish());
		hChoice.setId(choice.getId());
		hChoice.setPronunciation(choice.getPronunciation());
		hChoice.setCorrect(choice.getCorrect());
		hChoice.setAttempted(choice.getAttempted());

		return hChoice;
	}

	private Choice toChoice(HibernateChoice hChoice) {
		Choice choice = new Choice();

		choice.setId(hChoice.getId());
		choice.setChinese(UnicodeUtils.unescape(hChoice.getChinese()));
		choice.setEnglish(hChoice.getEnglish());
		choice.setPronunciation(hChoice.getPronunciation());
		choice.setCorrect(hChoice.getCorrect());
		choice.setAttempted(hChoice.getAttempted());

		return choice;
	}

	private List<Choice> toChoices(List<HibernateChoice> hChoices) {
		List<Choice> choices = new ArrayList<Choice>();

		for (HibernateChoice hChoice : hChoices) {
			choices.add(toChoice(hChoice));
		}

		return choices;
	}

	public void update(Choice choice) {
		HibernateChoice hChoice = toChoice(choice);

		getHibernateTemplate().update(hChoice);
	}
}
