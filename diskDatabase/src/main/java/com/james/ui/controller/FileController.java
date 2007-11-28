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

import com.james.fileItems.DiskFile;
import com.james.helper.DiskFileHelper;


public class FileController extends BaseController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered FileHandler.handleRequest()");
        Map model = new HashMap();

        String fileId = request.getParameter("fileId");

        ModelAndView modelAndView;

        DiskFile file = loadFile(fileId);
        model.put("file", file);

        modelAndView = new ModelAndView("file", model);

        return modelAndView;
    }

    private DiskFile loadFile(String fileId) {
        DiskFileHelper diskFileHelper = (DiskFileHelper) CONTEXT
                .getBean("diskFileHelper");

        return diskFileHelper.load(Integer.parseInt(fileId));
    }

}