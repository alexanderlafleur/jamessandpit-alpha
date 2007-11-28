package com.james.ui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.james.fileItems.Dir;
import com.james.helper.DirHelper;

public class DirController extends BaseController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered DirHandler.handleRequest()");
        Map<String, Dir> model = new HashMap<String, Dir>();

        String dirId = request.getParameter("dirId");

        ModelAndView modelAndView;

        Dir dir = loadDir(dirId);
        model.put("dir", dir);

        modelAndView = new ModelAndView("dir", model);

        return modelAndView;
    }

    private Dir loadDir(String dirId) {
        DirHelper dirHelper = (DirHelper) CONTEXT.getBean("dirHelper");
        return dirHelper.load(Integer.parseInt(dirId));
    }
}