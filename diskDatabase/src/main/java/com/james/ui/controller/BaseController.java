package com.james.ui.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseController {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

}
