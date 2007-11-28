package com.james.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.DiskDao;
import com.james.fileItems.Dir;
import com.james.fileItems.Disk;
import com.james.fileItems.DiskFile;
import com.james.fileItems.MP3;
import com.james.main.InvalidMp3Exception;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskDTO;
import com.james.ui.swing.node.DiskInfo;
import com.james.ui.swing.node.FormField;

public class DiskHelper {

	private class FileFilter implements java.io.FileFilter {

		private boolean dirs;

		public FileFilter(boolean dirs) {
			this.dirs = dirs;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return dirs;
			} else {
				return !dirs;
			}
		}
	}

	private DiskDao dao;

	private DirHelper dirHelper;

	private DiskFileHelper fileHelper;

	protected final Log logger = LogFactory.getLog(getClass());

	private MP3Helper mp3Helper;

	private String calculateType(String path) {
		int dotIndex = path.lastIndexOf(".") + 1;

		return path.substring(dotIndex);
	}

	public DiskDTO create(DiskDTO dto) {
		Disk disk = fromDTO(dto);
		dao.create(disk);

		return toDTO(disk, false);
	}

	public void delete(Disk disk) {
		dao.delete(disk);
	}

	public void delete(String id) {
		Disk disk = load(id);
		delete(disk);
	}

	public DiskDTO findByMediaNumber(long mediaNumber) {
		Disk disk = dao.findDiskByMediaNumber(mediaNumber);

		return toDTO(disk, false);
	}

	public DiskDTO findDisk(DirDTO dto) {
		Disk disk = dao.findDisk(Long.parseLong(dto.getId()));

		return toDTO(disk, false);
	}

	public Disk fromDTO(DiskDTO dto) {
		if (dto == null) {
			return null;
		}
		Disk disk = new Disk();

		if (dto.getId() != null) {
			disk.setId(Long.parseLong(dto.getId()));
		}
		disk.setLabel(dto.getLabel());
		disk.setMediaNumber(dto.getMediaNumber());

		disk.setRoot(getDirHelper().fromDTO(dto.getRoot(), null, false));

		return disk;
	}

	public DiskDao getDao() {
		return dao;
	}

	public DirHelper getDirHelper() {
		return dirHelper;
	}

	public DiskFileHelper getFileHelper() {
		return fileHelper;
	}

	public MP3Helper getMp3Helper() {
		return mp3Helper;
	}

	public List getOrphanedDisks() {
		return dao.getOrphanedDisks();
	}

	public List getOrphanedRoots() {
		List dirList = dao.getRootOrphanedRoots();

		List dtoList = new ArrayList();
		for (Iterator i = dirList.iterator(); i.hasNext();) {
			Dir dir = (Dir) i.next();

			dtoList.add(getDirHelper().toDTO(dir, false));
		}

		return dtoList;
	}

	public Disk load(long diskId) {
		return dao.load(diskId);
	}

	public Disk load(String diskId) {
		return load(Long.parseLong(diskId));
	}

	public Set loadAll(String walletId) {
		List diskList = dao.loadAll(Long.parseLong(walletId));

		Set results = new HashSet();
		for (Iterator i = diskList.iterator(); i.hasNext();) {
			Disk disk = (Disk) i.next();

			Dir root = disk.getRoot();
			if (root == null) {
				logger.warn("root is null for disk: " + disk.getId() + ": "
						+ disk.getLabel());
			} else {
				disk.setRoot(getDirHelper().load(disk.getRoot().getId()));
			}

			DiskDTO dto = toDTO(disk, false);
			results.add(dto);
		}

		return results;
	}

	public DiskDTO loadByName(String name, boolean children) {
		Disk disk = dao.loadByName(name);

		return toDTO(disk, children);
	}

	public DiskDTO loadDTO(long diskId) {
		return loadDTO(diskId, false);
	}

	public DiskDTO loadDTO(long diskId, boolean children) {
		Disk disk = dao.load(diskId);

		return toDTO(disk, children);
	}

	public DiskDTO loadDTO(String diskId) {
		return loadDTO(Long.parseLong(diskId));
	}

	public DirDTO loadRoot(String diskId) {
		Dir root = dao.loadRoot(diskId);

		return getDirHelper().toDTO(root, false);
	}

	public DiskDTO populateFromFields(DiskInfo info) {
		Map fields = info.getFields();

		DiskDTO dto = info.getDto();

		for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();

			FormField field = (FormField) fields.get(key);
			String text = field.getField().getText();

			if (DiskInfo.FIELD_NAME.equals(key)) {
				dto.setLabel(text);

			} else if (DiskInfo.FIELD_MEDIA_NUMBER.equals(key)) {
				dto.setMediaNumber(Long.parseLong(text));

			} else {
				logger.error("Unknown field: " + key);
			}
		}

		return dto;
	}

	public Dir read(File fDir, Dir parent) {
		logger.debug("Loading: " + fDir.getAbsolutePath());

		try {
			Dir curDir = new Dir();
			curDir.setParent(parent);
			curDir.setName(fDir.getName());

			dirHelper.create(curDir);

			readFiles(fDir, curDir);

			curDir.setDirs(readDirs(fDir, curDir));

			// dirHelper.update(curDir);

			return curDir;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads and saves the dirs that are in parentFileDir
	 * 
	 * @param parentFileDir
	 * @param parentDir
	 * @return
	 */
	private Set readDirs(File parentFileDir, Dir parentDir) {
		File[] dirs = parentFileDir.listFiles(new FileFilter(true));

		Set results = new HashSet();

		if (dirs != null) {
			List fileList = Arrays.asList(dirs);
			for (Iterator i = fileList.iterator(); i.hasNext();) {
				File fDir = (File) i.next();
				Dir dir = read(fDir, parentDir);

				results.add(dir);
			}
		}

		return results;
	}

	private DiskFile readFile(Dir dir, File file) {
		DiskFile df = new DiskFile();

		df.setParent(dir);
		df.setName(file.getName());
		df.setType(calculateType(file.getAbsolutePath()));
		df.setPath(file.getAbsolutePath());
		df.setSize(file.length());

		getFileHelper().create(df);

		return df;
	}

	private void readFiles(File fDir, Dir dir) {
		File[] files = fDir.listFiles(new FileFilter(false));

		if (files != null) {
			List fileList = Arrays.asList(files);
			for (Iterator i = fileList.iterator(); i.hasNext();) {
				File file = (File) i.next();

				String name = file.getName();

				if (name.toLowerCase().endsWith("mp3")) {
					try {
						MP3 mp3 = mp3Helper.createMp3(file, dir);

						getMp3Helper().create(mp3);

						dir.addMp3(mp3);

					} catch (InvalidMp3Exception e) {
						logger.info("Unable to load MP3 - " + e.getMessage()
								+ ". Loading as a file instead.");

						dir.addFile(readFile(dir, file));

					}

				} else {
					dir.addFile(readFile(dir, file));

				}
			}
		}
	}

	public List search(String label) {
		return dao.search(label);
	}

	public void setDao(DiskDao dao) {
		this.dao = dao;
	}

	public void setDirHelper(DirHelper dirHelper) {
		this.dirHelper = dirHelper;
	}

	public void setFileHelper(DiskFileHelper fileHelper) {
		this.fileHelper = fileHelper;
	}

	public void setMp3Helper(MP3Helper mp3Helper) {
		this.mp3Helper = mp3Helper;
	}

	public DiskDTO toDTO(Disk disk, boolean children) {
		DiskDTO dto = new DiskDTO();

		dto.setId(String.valueOf(disk.getId()));
		dto.setLabel(disk.getLabel());
		dto.setMediaNumber(disk.getMediaNumber());

		if (children) {
			Dir root = disk.getRoot();

			if (root == null) {

			} else {
				dto.setRoot(dirHelper.toDTO(root, false));
			}
		}

		return dto;
	}

	public void update(DiskDTO dto) {
		Disk disk = fromDTO(dto);
		dao.update(disk);
	}

	// private void verify(Dir dir) {
	// assert (dir != null);
	//
	// for (Iterator i = dir.getDirs().iterator(); i.hasNext();) {
	// Dir child = (Dir) i.next();
	//
	// assert (child.getParent().equals(dir));
	// }
	// }
}