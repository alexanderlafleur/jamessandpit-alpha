package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.main.DetailPane;

public class DirInfo extends BaseInfo {

    public static final String FIELD_NAME = "Name";

    private static final long serialVersionUID = 1L;

    private DirDTO dirDTO;

    protected final Log logger = LogFactory.getLog(getClass());

    public DirInfo(String id, String label, DirDTO dirDTO, BaseNodeHelper helper) {
        super(id, label, helper);

        this.dirDTO = dirDTO;

        createFields(dirDTO);
        createButtons();
    }

    private void createButtons() {
        getButtons().clear();

        DataButton save = new DataButton(SaveHelper.SAVE_DIR, this);
        save.addActionListener(getHelper().getSaveHelper());

        getButtons().add(save);

    }

    private void createFields(DirDTO dir) {
        getFields().clear();

        getFields().put(FIELD_NAME, new FormField(FIELD_NAME, dir.getName()));
    }

    public DirDTO getDto() {
        return dirDTO;
    }

    public List loadChildren() {
        getHelper().loadChildren(dirDTO);

        List childNodes = new ArrayList();

        for (Iterator i = dirDTO.getFiles().iterator(); i.hasNext();) {
            DiskFileDTO file = (DiskFileDTO) i.next();

            // create a leaf
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileInfo(
                    file.getId(), file.getName(), file, getHelper()), false);

            childNodes.add(childNode);
        }

        for (Iterator i = dirDTO.getMp3s().iterator(); i.hasNext();) {
            MP3DTO mp3 = (MP3DTO) i.next();

            // create a leaf
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new MP3Info(mp3.getId(),
                    mp3.getName(), mp3, getHelper()), false);

            childNodes.add(childNode);
        }

        for (Iterator i = dirDTO.getDirs().iterator(); i.hasNext();) {
            DirDTO dir = (DirDTO) i.next();

            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new DirInfo(dir.getId(),
                    dir.getName(), dir, getHelper()));

            childNodes.add(childNode);
        }

        return childNodes;
    }

    public void setDirDTO(DirDTO dirDTO) {
        this.dirDTO = dirDTO;
    }

    public void show(DetailPane detail) {
        detail.removeAll();
        JComponent predecessor = null;

        for (Iterator i = getFields().values().iterator(); i.hasNext();) {
            FormField field = (FormField) i.next();

            predecessor = getLayoutHelper().layoutField(field.getLabel(), field.getField(),
                    predecessor, detail);
        }

        for (Iterator i = getButtons().iterator(); i.hasNext();) {
            JButton button = (JButton) i.next();

            predecessor = getLayoutHelper().layoutButton(button, predecessor, detail);
        }
    }
}