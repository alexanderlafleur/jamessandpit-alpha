package com.james.model;

import java.io.Serializable;
import java.util.List;

public class SearchForm implements Serializable {

	private static final long serialVersionUID = 6648526475308442438L;

	private String formAction;

	private String nameCriteria;

	private List results;

	public String getFormAction() {
		return formAction;
	}

	public String getNameCriteria() {
		return nameCriteria;
	}

	public List getResults() {
		return results;
	}

	public void setFormAction(String action) {
		this.formAction = action;
	}

	public void setNameCriteria(String nameCriteria) {
		this.nameCriteria = nameCriteria;
	}

	public void setResults(List results) {
		this.results = results;
	}
}
