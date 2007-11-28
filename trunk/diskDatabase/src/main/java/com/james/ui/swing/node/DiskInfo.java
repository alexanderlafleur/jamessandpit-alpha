package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.main.DetailPane;

public class DiskInfo extends BaseInfo {

	public static final String FIELD_MEDIA_NUMBER = "Media Number";

	public static String FIELD_NAME = "Name";

	private static final long serialVersionUID = -1L;

	private DiskDTO diskDTO;

	protected final Log logger = LogFactory.getLog(getClass());

	public DiskInfo(String id, String label, DiskDTO diskDTO,
			BaseNodeHelper helper) {
		super(id, label, helper);
		this.diskDTO = diskDTO;

		createFields(diskDTO);
		createButtons();

	}

	private void createButtons() {
		getButtons().clear();

		DataButton save = new DataButton(SaveHelper.SAVE_DISK, this);
		save.addActionListener(getHelper().getSaveHelper());

		getButtons().add(save);

	}

	private void createFields(DiskDTO disk) {
		getFields().put(FIELD_NAME, new FormField(FIELD_NAME, disk.getLabel()));
		getFields().put(
				FIELD_MEDIA_NUMBER,
				new FormField(FIELD_MEDIA_NUMBER, String.valueOf(disk
						.getMediaNumber())));

	}

	public DiskDTO getDto() {
		return diskDTO;
	}

	public List loadChildren() {

		// populate root's children (1 level)
		DirDTO root = getHelper().loadRoot(diskDTO.getId());

		getHelper().loadChildren(root);

		List childNodes = new ArrayList();

		for (Iterator i = root.getFiles().iterator(); i.hasNext();) {
			DiskFileDTO file = (DiskFileDTO) i.next();

			// create a leaf
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					new FileInfo(file.getId(), file.getName(), file,
							getHelper()), false);

			childNodes.add(childNode);
		}

		for (Iterator i = root.getMp3s().iterator(); i.hasNext();) {
			MP3DTO mp3 = (MP3DTO) i.next();

			// create a leaf
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					new MP3Info(mp3.getId(), mp3.getName(), mp3, getHelper()),
					false);

			childNodes.add(childNode);
		}

		for (Iterator i = root.getDirs().iterator(); i.hasNext();) {
			DirDTO dir = (DirDTO) i.next();

			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
					new DirInfo(dir.getId(), dir.getName(), dir, getHelper()),
					true);

			childNodes.add(childNode);
		}

		return childNodes;
	}

	public void setDiskDTO(DiskDTO diskDTO) {
		this.diskDTO = diskDTO;
	}

	public void show(DetailPane detail) {
		detail.removeAll();
		JComponent predecessor = null;

		for (Iterator i = getFields().values().iterator(); i.hasNext();) {
			FormField field = (FormField) i.next();

			predecessor = getLayoutHelper().layoutField(field.getLabel(),
					field.getField(), predecessor, detail);
		}

		for (Iterator i = getButtons().iterator(); i.hasNext();) {
			JButton button = (JButton) i.next();

			predecessor = getLayoutHelper().layoutButton(button, predecessor,
					detail);
		}
	}
}