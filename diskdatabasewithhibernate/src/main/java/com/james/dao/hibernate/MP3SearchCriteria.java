package com.james.dao.hibernate;

import com.james.fileItems.Dir;
import com.james.fileItems.util.MP3ParserImpl;

public class MP3SearchCriteria {
    private String album;

    private String artist;

    private String band;

    private int bitrate;

    private String comment;

    private String date;

    private String genre;

    private long id;

    private long length;

    private String name;

    private Dir parent;

    private MP3ParserImpl parserImpl;

    private Long sizeHigh;

    private Long sizeLow;

    private String title;

    private String track;

    private String type;

    private int year;

    public MP3SearchCriteria(String name, String type, Long sizeLow, Long sizeHigh, String album, String artist) {
        this.name = name;
        this.type = type;
        this.sizeLow = sizeLow;
        this.album = album;
        this.artist = artist;
        this.sizeHigh = sizeHigh;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getBand() {
        return band;
    }

    public int getBitrate() {
        return bitrate;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getGenre() {
        return genre;
    }

    public long getId() {
        return id;
    }

    public long getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public Dir getParent() {
        return parent;
    }

    public MP3ParserImpl getParserImpl() {
        return parserImpl;
    }

    public Long getSizeHigh() {
        return sizeHigh;
    }

    public Long getSizeLow() {
        return sizeLow;
    }

    public String getTitle() {
        return title;
    }

    public String getTrack() {
        return track;
    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Dir parent) {
        this.parent = parent;
    }

    public void setParserImpl(MP3ParserImpl parserImpl) {
        this.parserImpl = parserImpl;
    }

    public void setSizeHigh(Long sizeHigh) {
        this.sizeHigh = sizeHigh;
    }

    public void setSizeLow(Long sizeLow) {
        this.sizeLow = sizeLow;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("MP3 Criteria: " + getId() + " " + getType() + " " + getName() + " " + getSizeLow() + " " + getSizeHigh() + "" + getAlbum() + " " + getArtist() + " ("
                + getParent() + ")");

        return sb.toString();
    }

}
