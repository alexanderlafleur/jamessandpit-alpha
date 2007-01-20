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

import com.james.fileItems.MP3;
import com.james.helper.MP3Helper;


public class MP3Controller extends BaseController implements Controller {
    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered Mp3.handleRequest()");
        Map model = new HashMap();

        String fileId = request.getParameter("mp3Id");

        ModelAndView modelAndView;

        MP3 mp3 = loadMp3(fileId);

        model.put("mp3", mp3);

        modelAndView = new ModelAndView("mp3", model);

        return modelAndView;
    }

    private MP3 loadMp3(String fileId) {
        MP3Helper mp3Helper = (MP3Helper) CONTEXT.getBean("mp3Helper");

        return mp3Helper.load(Integer.parseInt(fileId));
    }
}