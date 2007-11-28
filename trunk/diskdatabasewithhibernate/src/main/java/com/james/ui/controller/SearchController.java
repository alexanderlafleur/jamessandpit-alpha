package com.james.ui.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.james.helper.DiskFileHelper;
import com.james.main.DiskFileSearchCriteria;

/**
 * Only controller for subscriber information portlet.
 * 
 * @author jdunwoody
 */
public class SearchController extends BaseController implements Controller {

    private static final int MAX_RESULTS = 100;

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Entered SearchHandler.handleRequest()");
        Map model = new HashMap();

        String name = request.getParameter("title");
        String path = request.getParameter("path");
        String type = request.getParameter("type");
        String sLow = request.getParameter("low");
        String sHigh = request.getParameter("high");
        Long low;

        if (sLow == null) {
            low = null;
        } else {
            low = new Long(sLow);
        }
        Long high;
        if (sHigh == null) {
            high = null;
        } else {
            high = new Long(sHigh);
        }
        ModelAndView modelAndView;

        List results = search(new DiskFileSearchCriteria(name, type, path, low, high), MAX_RESULTS);
        model.put("results", results);

        modelAndView = new ModelAndView("mp3", model);

        return modelAndView;
    }

    private List search(DiskFileSearchCriteria criteria, int maxResults) {
        DiskFileHelper diskFileHelper = (DiskFileHelper) CONTEXT.getBean("diskFileHelper");

        return diskFileHelper.search(criteria, maxResults);
    }
}
