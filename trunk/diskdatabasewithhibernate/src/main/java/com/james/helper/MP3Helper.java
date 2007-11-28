package com.james.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.MP3Dao;
import com.james.dao.hibernate.MP3SearchCriteria;
import com.james.fileItems.Dir;
import com.james.fileItems.MP3;
import com.james.main.InvalidMp3Exception;
import com.james.ui.dto.DirDTO;
import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.node.MP3Info;

import de.vdheide.mp3.FrameDamagedException;
import de.vdheide.mp3.MP3File;

public class MP3Helper {
    private MP3Dao dao;

    private DirHelper dirHelper;

    protected final Log logger = LogFactory.getLog(getClass());

    private String calculateAlbum(MP3File mp3File, File file) {
        String album;
        try {
            album = mp3File.getAlbum().getTextContent();
        } catch (Exception e) {
            // System.err.println("Unable to set album because of " +
            // e.getMessage());
            album = "";
        }

        return album;
    }

    private String calculateArtist(MP3File f) {
        String composer;
        try {
            composer = f.getComposer().getTextContent();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            composer = "";
        }

        return composer;
    }

    private String calculateBand(MP3File f) {
        String band;
        try {
            band = f.getBand().getTextContent();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            band = "";
        }

        return band;
    }

    private int calculateBitrate(MP3File f) {
        int bitRate;
        try {
            bitRate = f.getBitrate();
        } catch (Exception e) {
            // System.err.println("Unable to set bitrate because of " +
            // e.getMessage());
            bitRate = 0;
        }
        return bitRate;
    }

    private String calculateComment(MP3File f) {
        String comment;
        try {
            comment = f.getComments().getTextContent();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            comment = "";
        }

        return comment;

    }

    private String calculateDate(MP3File f) {
        String date;
        try {
            date = f.getDate().getTextContent();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            date = "";
        }

        return date;
    }

    private String calculateGenre(MP3File mp3File) {
        String genre;
        try {
            genre = mp3File.getGenre().getTextContent();
        } catch (Exception e) {
            // System.err.println("Unable to set genre because of " +
            // e.getMessage());
            genre = "";
        }

        return genre;
    }

    // private long calculateFileSize(MP3File f, File file)
    // throws FrameDamagedException {
    // long fileSize = 0;
    // String sFileSize = f.getFilesize().getTextContent();
    //
    // if (sFileSize == null) {
    // fileSize = file.length();
    // } else {
    // try {
    // fileSize = Long.parseLong(sFileSize);
    // } catch (NumberFormatException e) {
    // fileSize = 0;
    // System.err.println("Unable to set filesize because of "
    // + e.getMessage());
    // }
    // }
    //
    // return fileSize;
    // }

    private long calculateLength(MP3File mp3File, File file) {
        long length;
        try {
            length = mp3File.getLength();
        } catch (Exception e) {
            // System.err.println("Unable to set length because of "+
            // e.getMessage());
            length = file.length();
        }

        return length;
    }

    private String calculateName(MP3File f, File file) {
        String name;
        try {
            name = f.getName();
        } catch (Exception e) {
            // System.err.println("Unable to set name because of " +
            // e.getMessage());
            name = file.getName();
        }

        return name;
    }

    private long calculateSize(MP3File f, File file) {
        long size;
        try {
            size = Long.parseLong(f.getFilesize().getTextContent());
        } catch (NumberFormatException e) {
            // System.err.println(e.getMessage());
            size = file.length();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            size = file.length();
        }

        return size;
    }

    private String calculateTitle(MP3File mp3File, File file) {
        String title;
        try {
            title = mp3File.getTitle().getTextContent();
        } catch (Exception e) {
            // System.err.println("Unable to set title because of "+
            // e.getMessage());
            title = file.getName();
        }

        return title;
    }

    private String calculateTrack(MP3File mp3File, File file) {
        String track;
        try {
            track = mp3File.getTrack().getTextContent();
        } catch (Exception e) {
            // System.err.println("Unable to set track because of " +
            // e.getMessage());
            track = "";
        }

        return track;
    }

    private String calculateType(MP3File mp3File, File file) {
        String type;
        try {
            type = mp3File.getFileType().getTextContent();
        } catch (FrameDamagedException e) {
            // System.err.println(e.getMessage());
            type = "";
        }

        return type;
    }

    private int calculateYear(MP3File mp3File, File file) {
        int year;
        try {
            year = getInt(mp3File.getYear().getTextContent());
        } catch (Exception e) {
            // System.err.println("Unable to set year because of " +
            // e.getMessage());

            year = 0;
        }

        return year;
    }

    public MP3 create(MP3 mp3) throws InvalidMp3Exception {
        dao.create(mp3);

        return mp3;
    }

    public MP3 createMp3(File file, Dir parent) throws InvalidMp3Exception {
        MP3 mp3 = new MP3();
        mp3.setParent(parent);
        MP3File mp3File;

        try {
            mp3File = new MP3File(file.getAbsolutePath());

        } catch (Exception e) {
            // System.err.println("Unable to create file " +
            // file.getAbsolutePath() + " - " + e.getMessage());

            throw new InvalidMp3Exception("Unable to create " + file.getAbsolutePath(), e);
        } catch (OutOfMemoryError e) {
            // System.err.println("Unable to create file " +
            // file.getAbsolutePath() + " - " + e.getMessage());

            throw new InvalidMp3Exception("Unable to create " + file.getAbsolutePath() + " " + e.getMessage());
        }

        try {
            mp3.setName(calculateName(mp3File, file));

            mp3.setBitrate(calculateBitrate(mp3File));

            mp3.setBand(calculateBand(mp3File));

            mp3.setComment(calculateComment(mp3File));
            mp3.setArtist(calculateArtist(mp3File));
            mp3.setDate(calculateDate(mp3File));
            mp3.setSize(calculateSize(mp3File, file));
            mp3.setType(calculateType(mp3File, file));

            mp3.setGenre(calculateGenre(mp3File));
            mp3.setLength(calculateLength(mp3File, file));
            mp3.setTitle(calculateTitle(mp3File, file));

            mp3.setTrack(calculateTrack(mp3File, file));

            mp3.setYear(calculateYear(mp3File, file));

            mp3.setAlbum(calculateAlbum(mp3File, file));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mp3;
    }

    public MP3 fromDTO(MP3DTO dto, Dir parent) {
        MP3 mp3 = new MP3();

        mp3.setName(dto.getName());
        mp3.setId(Long.parseLong(dto.getId()));
        mp3.setAlbum(dto.getAlbum());
        mp3.setArtist(dto.getArtist());
        mp3.setBand(dto.getBand());
        mp3.setBitrate(dto.getBitrate());
        mp3.setComment(dto.getComment());
        mp3.setDate(dto.getDate());
        mp3.setGenre(dto.getGenre());
        mp3.setLength(dto.getLength());
        mp3.setSize(dto.getSize());
        mp3.setTitle(dto.getTitle());
        mp3.setTrack(dto.getTrack());
        mp3.setType(dto.getType());
        mp3.setYear(dto.getYear());

        mp3.setParent(parent);

        return mp3;
    }

    public MP3Dao getDao() {
        return dao;
    }

    public DirHelper getDirHelper() {
        return dirHelper;
    }

    private int getInt(String s) {
        if (s == null || s.trim().length() == 0) {
            return 0;
        }

        return Integer.parseInt(s);
    }

    public boolean isMP3(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }

        int i = name.lastIndexOf(".");

        String ext = name.substring(i + 1, name.length());

        if ("MP3".equalsIgnoreCase(ext)) {
            return true;
        }

        return false;

    }

    public MP3 load(int fileId) {
        return dao.load(fileId);
    }

    public List search(MP3SearchCriteria criteria, int maxResults) {
        List files = dao.search(criteria);

        if (files.size() > maxResults) {
            files = files.subList(0, maxResults);
        }

        List<MP3DTO> dtos = new ArrayList<MP3DTO>();

        for (Iterator i = files.iterator(); i.hasNext();) {
            MP3 mp3 = (MP3) i.next();
            dtos.add(toDTO(mp3));
        }
        return dtos;
    }

    public void setDao(MP3Dao dao) {
        this.dao = dao;
    }

    public void setDirHelper(DirHelper dirHelper) {
        this.dirHelper = dirHelper;
    }

    public MP3DTO toDTO(MP3 mp3) {
        return toDTO(mp3, getDirHelper().toDTO(mp3.getParent(), false));
    }

    public MP3DTO toDTO(MP3 mp3, DirDTO parent) {
        MP3DTO dto = new MP3DTO();

        dto.setId(String.valueOf(mp3.getId()));
        dto.setName(mp3.getName());
        dto.setAlbum(mp3.getAlbum());
        dto.setArtist(mp3.getArtist());
        dto.setBand(mp3.getBand());
        dto.setBitrate(mp3.getBitrate());
        dto.setComment(mp3.getComment());
        dto.setDate(mp3.getDate());
        dto.setGenre(mp3.getGenre());
        dto.setLength(mp3.getLength());
        dto.setSize(mp3.getSize());
        dto.setTitle(mp3.getTitle());
        dto.setTrack(mp3.getTrack());
        dto.setType(mp3.getType());
        dto.setYear(mp3.getYear());

        dto.setParent(parent);

        return dto;
    }

    public void update(MP3DTO dto) {
        Dir parent = getDirHelper().load(dto.getParent());

        MP3 mp3 = fromDTO(dto, parent);

        logger.debug("Updating mp3: " + mp3.getId());

        dao.update(mp3);
    }

    public MP3DTO populateFromFields(MP3Info info) {
        Map fields = info.getFields();

        MP3DTO dto = new MP3DTO();

        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();

            if (MP3Info.FIELD_ALBUM.equals(key)) {
                dto.setAlbum((String) fields.get(key));
            } else if (MP3Info.FIELD_ARTIST.equals(key)) {
                dto.setArtist((String) fields.get(key));
            } else {
                // ...
            }

        }

        return dto;
    }

}
