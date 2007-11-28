package com.james.ui;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExplorerValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(ExplorerForm.class);
    }

    public void validate(Object form, Errors errors) {

    }
}
