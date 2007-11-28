package com.james.ui.swing.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.ui.dto.DTO;
import com.james.ui.swing.node.BaseInfo;
import com.james.ui.swing.node.BaseNodeHelper;
import com.james.ui.swing.node.FormField;

public class SearchController implements ActionListener, ListSelectionListener {
    public static final String SEARCH_COMMAND = "SEARCH";

    public static final String SEARCH_FILE_COMMAND = "FILE SEARCH";

    public static final String SEARCH_MP3_COMMAND = "MP3 SEARCH";

    private static final int MAX_RESULT_SIZE = 100;

    private DetailPane detailPane;

    private SearchFields fields;

    private BaseNodeHelper helper;

    private InfoHelper infoHelper = new InfoHelper();

    protected final Log logger = LogFactory.getLog(getClass());

    private SearchResults results;

    private SearchHelper searchHelper = new SearchHelper();

    public SearchController(DetailPane detailPane, BaseNodeHelper helper) {
        this.fields = new SearchFields(this);
        this.results = new SearchResults(this, helper);
        this.detailPane = detailPane;
        this.helper = helper;
    }

    public void actionPerformed(ActionEvent e) {
        List dtoResults;
        if (SEARCH_COMMAND.equals(e.getActionCommand())) {
            FormField field = getFields().getNameField();

            dtoResults = getSearchHelper().search(field.getField().getText(), MAX_RESULT_SIZE);

        } else if (SEARCH_MP3_COMMAND.equals(e.getActionCommand())) {
            FormField field = getFields().getNameField();

            dtoResults = getSearchHelper().searchForMP3s(field.getField().getText());

        } else if (SEARCH_FILE_COMMAND.equals(e.getActionCommand())) {
            FormField field = getFields().getNameField();

            dtoResults = getSearchHelper().searchForFiles(field.getField().getText());

        } else {
            System.err.println("Unknown command " + e);
            dtoResults = new ArrayList();
        }

        logger.info("Found " + dtoResults.size() + " results");

        Vector results = wrap(dtoResults);

        getResults().setResults(results);

    }

    public DetailPane getDetailPane() {
        return detailPane;
    }

    public SearchFields getFields() {
        return fields;
    }

    public BaseNodeHelper getHelper() {
        return helper;
    }

    public InfoHelper getInfoHelper() {
        return infoHelper;
    }

    public SearchResults getResults() {
        return results;
    }

    public SearchHelper getSearchHelper() {
        return searchHelper;
    }

    public void setDetailPane(DetailPane detailPane) {
        this.detailPane = detailPane;
    }

    public void setFields(SearchFields fields) {
        this.fields = fields;
    }

    public void setHelper(BaseNodeHelper helper) {
        this.helper = helper;
    }

    public void setInfoHelper(InfoHelper infoHelper) {
        this.infoHelper = infoHelper;
    }

    public void setResults(SearchResults results) {
        this.results = results;
    }

    public void setSearchHelper(SearchHelper searchHelper) {
        this.searchHelper = searchHelper;
    }

    public void showDetail(BaseInfo info) {
        getDetailPane().show(info);
    }

    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();

        BaseInfo info = (BaseInfo) list.getSelectedValue();

        if (info != null) {
            showDetail(info);
        }
    }

    private Vector wrap(List dtoResults) {
        Vector infoList = new Vector();

        for (Iterator i = dtoResults.iterator(); i.hasNext();) {
            DTO dto = (DTO) i.next();

            BaseInfo info = getInfoHelper().convert(dto, getHelper());

            infoList.add(info);
        }

        return infoList;
    }

    // /**
    // * Convert a list of BaseInfo (FileInfo or MP3Info) objects into a full
    // tree
    // * of infonodes, wrapped.
    // *
    // * @param dtoResults
    // * @return
    // */
    // private List convert(List dtoResults) {
    // List wallets = new ArrayList();
    //
    // for (Iterator i = dtoResults.iterator(); i.hasNext();) {
    // DTO dto = (DTO) i.next();
    //
    // BaseInfoWrapper wrapper = getInfoHelper().wrap(dto, getHelper());
    //
    // WalletInfo info = wrapper.convertToPartialTree();
    //
    // wallets.add(info);
    //
    // }
    //
    // return wallets;
    // }

}
