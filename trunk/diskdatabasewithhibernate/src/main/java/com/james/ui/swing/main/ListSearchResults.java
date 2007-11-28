package com.james.ui.swing.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.node.BaseInfo;
import com.james.ui.swing.node.FileInfo;
import com.james.ui.swing.node.MP3Info;

public class ListSearchResults extends JPanel implements ListSelectionListener {

    private static final long serialVersionUID = -1L;

    private SearchController controller;

    private JList results = new JList();

    public ListSearchResults(SearchController controller) {
        this.controller = controller;

        add(results);

        results.addListSelectionListener(this);
    }

    public SearchController getController() {
        return controller;
    }

    public void setFileResults(List newResults) {
        List listData = new ArrayList();

        setFileResults(newResults, listData);
    }

    public void setFileResults(List newResults, List listData) {

        for (Iterator i = newResults.iterator(); i.hasNext();) {
            DiskFileDTO file = (DiskFileDTO) i.next();

            listData.add(new FileInfo(file.getId(), file.getName(), file, getController().getHelper()));
        }

        results.setListData(listData.toArray());

    }

    public void setMP3Results(List newResults) {
        List listData = new ArrayList();

        setMP3Results(newResults, listData);
    }

    public void setMP3Results(List newResults, List listData) {

        for (Iterator i = newResults.iterator(); i.hasNext();) {
            MP3DTO mp3 = (MP3DTO) i.next();

            listData.add(new MP3Info(mp3.getId(), mp3.getName(), mp3, getController().getHelper()));
        }

        results.setListData(listData.toArray());

    }

    public void setResults(List newResults) {
        results.setListData(newResults.toArray());
    }

    public void setSearchController(SearchController controller) {
        this.controller = controller;
    }

    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();

        BaseInfo info = (BaseInfo) list.getSelectedValue();
        if (info != null) {
            getController().showDetail(info);
        }
    }

}
