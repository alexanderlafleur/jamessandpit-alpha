package com.james.ui.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MainController implements Controller {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String mode = request.getParameter("mode");

		ModelAndView modelAndView;

		if ("search".equals(mode)) {
			modelAndView = new ModelAndView("search");

		} else {
			modelAndView = new ModelAndView("main");

		}

		return modelAndView;
	}
}