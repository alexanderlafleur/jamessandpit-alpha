package com.james.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.DirDao;
import com.james.fileItems.Dir;
import com.james.fileItems.DiskFile;
import com.james.fileItems.MP3;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.node.DirInfo;

/**
 * @author James
 */
public class DirHelper {
    private DirDao dao;

    private DiskFileHelper fileHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    private MP3Helper mp3Helper;

    public void create(Dir dir) {
        dao.create(dir);
    }

    public void createDTO(DirDTO dto, Dir parent) {
        dao.create(fromDTO(dto, parent, true));
    }

    public void delete(String id) {
        dao.delete(load(Long.parseLong(id)));
    }

    public Dir fromDTO(DirDTO dto, Dir parent, boolean children) {
        if (dto == null) {
            return null;
        }
        Dir dir = new Dir();

        dir.setId(Long.parseLong(dto.getId()));
        dir.setName(dto.getName());

        dir.setParent(parent);

        if (children) {
            for (Iterator i = dto.getDirs().iterator(); i.hasNext();) {
                DirDTO childDirDTO = (DirDTO) i.next();

                dir.addDir(fromDTO(childDirDTO, dir, false));
            }
        }

        for (Iterator i = dto.getFiles().iterator(); i.hasNext();) {
            DiskFileDTO childFileDTO = (DiskFileDTO) i.next();

            dir.addFile(getFileHelper().fromDTO(childFileDTO, dir));
        }

        for (Iterator i = dto.getMp3s().iterator(); i.hasNext();) {
            MP3DTO childMP3DTO = (MP3DTO) i.next();

            dir.addMp3(getMp3Helper().fromDTO(childMP3DTO, dir));
        }

        return dir;
    }

    // public Dir fromDTO(DirDTO dto) {
    // return fromDTO(dto, null);
    // }

    public DirDao getDao() {
        return dao;
    }

    public DiskFileHelper getFileHelper() {
        return fileHelper;
    }

    public MP3Helper getMp3Helper() {
        return mp3Helper;
    }

    public Dir load(DirDTO dto) {
        return load(Long.parseLong(dto.getId()));
    }

    public Dir load(long dirId) {
        Dir dir = dao.load(dirId);

        // dao.loadChildren(dir);

        return dir;
    }

    public void loadChildren(Dir dir, boolean children) {
        dao.loadChildren(dir, children);
    }

    public void loadChildren(long id, boolean children) {
        Dir dir = load(id);
        dao.loadChildren(dir, children);
    }

    public DirDTO loadDTO(long id) {
        Dir dir = load(id);

        return toDTO(dir, false);
    }

    public DirDTO populateFromFields(DirInfo info) {
        Map fields = info.getFields();

        DirDTO dto = new DirDTO();

        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();

            if (DirInfo.FIELD_NAME.equals(key)) {
                dto.setName((String) fields.get(key));
            } else {
                logger.error("Unknown field: " + key);
            }
        }

        return dto;
    }

    public void readDirs(DirDTO root) {
        List dirs = dao.loadDirs(Long.parseLong(root.getId()));

        root.setDirs(new ArrayList<DirDTO>());
        for (Iterator i = dirs.iterator(); i.hasNext();) {
            Dir dir = (Dir) i.next();

            DirDTO dirDTO = toDTO(dir, false);

            root.addDir(dirDTO);
        }
    }

    public void readFiles(DirDTO root) {
        List files = dao.loadFiles(Long.parseLong(root.getId()));

        root.setFiles(new ArrayList<DiskFileDTO>());

        for (Iterator i = files.iterator(); i.hasNext();) {
            DiskFile file = (DiskFile) i.next();

            DiskFileDTO fileDTO = getFileHelper().toDTO(file);

            root.addFile(fileDTO);
        }
    }

    public void readMP3s(DirDTO root) {
        List mp3s = dao.loadMp3s(Long.parseLong(root.getId()));

        root.setMp3s(new ArrayList<MP3DTO>());

        for (Iterator i = mp3s.iterator(); i.hasNext();) {
            MP3 mp3 = (MP3) i.next();

            MP3DTO mp3DTO = getMp3Helper().toDTO(mp3);

            root.addMp3(mp3DTO);
        }
    }

    public List search(String name) {
        return dao.search(name, null, null);
    }

    public void setDao(DirDao dao) {
        this.dao = dao;
    }

    public void setFileHelper(DiskFileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public void setMp3Helper(MP3Helper mp3Helper) {
        this.mp3Helper = mp3Helper;
    }

    public DirDTO toDTO(Dir dir, boolean children) {

        DirDTO parent = null;
        if (dir.getParent() != null) {
            parent = toDTO(dir.getParent(), false);
        }

        return toDTO(dir, parent, children);
    }

    public DirDTO toDTO(Dir dir, DirDTO parent, boolean children) {
        DirDTO dto = new DirDTO();

        dto.setId(String.valueOf(dir.getId()));
        dto.setName(dir.getName());

        if (dir.getParent() != null) {
            dto.setParent(parent);
        }

        if (children) {
            for (Iterator i = dir.getDirs().iterator(); i.hasNext();) {
                Dir childDir = (Dir) i.next();

                dto.addDir(toDTO(childDir, dto, false));
            }

            for (Iterator i = dir.getFiles().iterator(); i.hasNext();) {
                DiskFile childFile = (DiskFile) i.next();

                dto.addFile(getFileHelper().toDTO(childFile, dto));
            }

            for (Iterator i = dir.getMp3s().iterator(); i.hasNext();) {
                MP3 mp3 = (MP3) i.next();

                dto.addMp3(getMp3Helper().toDTO(mp3, dto));
            }
        }

        return dto;
    }

    public void update(Dir dir) {
        dao.update(dir);
    }

    public void update(DirDTO dto) {
        Dir parent = load(dto.getParent());

        dao.update(fromDTO(dto, parent, true));
    }

    public void updateDTO(DirDTO dto, Dir parent) {
        dao.update(fromDTO(dto, parent, true));
    }
}