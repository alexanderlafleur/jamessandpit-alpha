package com.james.ui.swing.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.james.dao.hibernate.MP3SearchCriteria;
import com.james.helper.DiskFileHelper;
import com.james.helper.MP3Helper;
import com.james.main.DiskFileSearchCriteria;

public class SearchHelper {
    protected static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });

    private static final int MAX_RESULTS = 100;

    private DiskFileHelper fileHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    private MP3Helper mp3Helper;

    public SearchHelper() {
        mp3Helper = (MP3Helper) CONTEXT.getBean("mp3Helper");
        fileHelper = (DiskFileHelper) CONTEXT.getBean("diskFileHelper");
    }

    public DiskFileHelper getFileHelper() {
        return fileHelper;
    }

    public MP3Helper getMp3Helper() {
        return mp3Helper;
    }

    public List search(String text, int maxResults) {
        logger.info("Searching for: " + text);
        List results = new ArrayList();

        List files = searchForFiles(text);
        List mp3s = searchForMP3s(text);

        results.addAll(files);
        results.addAll(mp3s);

        logger.debug("Search completed.");

        return results;
    }

    public List searchForFiles(String simple) {
        DiskFileSearchCriteria criteria = new DiskFileSearchCriteria(simple, null, null, null, null);

        List files = getFileHelper().search(criteria, MAX_RESULTS);

        return files;
    }

    List searchForMP3s(String simple) {
        MP3SearchCriteria criteria = new MP3SearchCriteria(simple, null, null, null, null, null);

        List mp3s = getMp3Helper().search(criteria, MAX_RESULTS);

        return mp3s;
    }

    public void setFileHelper(DiskFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public void setMp3Helper(MP3Helper mp3Helper) {
        this.mp3Helper = mp3Helper;
    }

}
