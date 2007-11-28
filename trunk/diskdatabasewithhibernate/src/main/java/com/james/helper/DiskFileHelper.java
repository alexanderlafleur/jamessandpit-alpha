package com.james.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.DiskFileDao;
import com.james.fileItems.Dir;
import com.james.fileItems.DiskFile;
import com.james.main.DiskFileSearchCriteria;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.DiskFileDTO;
import com.james.ui.swing.node.FileInfo;

public class DiskFileHelper {
    private DiskFileDao dao;

    private DirHelper dirHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    public void create(DiskFile file) {
        dao.create(file);
    }

    public DiskFile fromDTO(DiskFileDTO dto, Dir parent) {
        DiskFile file = new DiskFile();

        file.setId(Long.parseLong(dto.getId()));
        file.setName(dto.getName());
        file.setPath(dto.getPath());
        file.setSize(Long.parseLong(dto.getSize()));
        file.setType(dto.getType());

        file.setParent(parent);

        return file;
    }

    public DiskFileDao getDao() {
        return dao;
    }

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    public DiskFile load(long fileId) {
        return dao.getFile(fileId);
    }

    public DiskFileDTO loadDTO(String id) {
        DiskFile file = dao.getFile(Long.parseLong(id));

        return toDTO(file);
    }

    public DiskFileDTO populateFromFields(FileInfo info) {
        Map fields = info.getFields();

        DiskFileDTO dto = new DiskFileDTO();

        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();

            if (FileInfo.FIELD_NAME.equals(key)) {
                dto.setName((String) fields.get(key));
            } else if (FileInfo.FIELD_PATH.equals(key)) {
                dto.setPath((String) fields.get(key));
            } else if (FileInfo.FIELD_SIZE.equals(key)) {
                dto.setSize((String) fields.get(key));
            } else if (FileInfo.FIELD_TYPE.equals(key)) {
                dto.setType((String) fields.get(key));
            } else {
                logger.error("Unknown field: " + key);
            }

        }

        return dto;
    }

    public List search(DiskFileSearchCriteria criteria, int maxResults) {
        List files = dao.search(criteria);

        if (files.size() > maxResults) {
            files = files.subList(0, maxResults);
        }

        List<DiskFileDTO> dtos = new ArrayList<DiskFileDTO>();

        for (Iterator i = files.iterator(); i.hasNext();) {
            DiskFile file = (DiskFile) i.next();
            dtos.add(toDTO(file));
        }
        return dtos;
    }

    public void setDao(DiskFileDao dao) {
        this.dao = dao;
    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public DiskFileDTO toDTO(DiskFile file) {
        return toDTO(file, getDirHelper().toDTO(file.getParent(), false));
    }

    public DiskFileDTO toDTO(DiskFile file, DirDTO parent) {
        DiskFileDTO dto = new DiskFileDTO();

        dto.setId(String.valueOf(file.getId()));
        dto.setName(file.getName());
        dto.setPath(file.getPath());
        dto.setSize(String.valueOf(file.getSize()));
        dto.setType(file.getType());

        dto.setParent(parent);

        return dto;
    }

    public void update(DiskFile file) {
        logger.debug("Updating file: " + file.getId());
        dao.update(file);
    }

    public void update(DiskFileDTO dto) {
        Dir parent = getDirHelper().load(dto.getParent());

        DiskFile file = fromDTO(dto, parent);

        update(file);
    }

    // public void verify(DiskFile file) throws InvalidFileItemException {
    // if (file == null) {
    // return;
    // }
    //
    // if (file.getParent() == null) {
    // throw new InvalidFileItemException("DiskFile " + file
    // + " has not parent.");
    // }
    // }
}