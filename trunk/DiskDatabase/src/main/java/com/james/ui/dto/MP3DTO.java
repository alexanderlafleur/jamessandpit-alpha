package com.james.ui.dto;

public class MP3DTO extends DTO {
    private String album;

    private String artist;

    private String band;

    private int bitrate;

    private String comment;

    private String date;

    private String genre;

    private long length;

    private String name;

    private DirDTO parent;

    private long size;

    private String title;

    private String track;

    private String type;

    private int year;

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

    public long getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public DirDTO getParent() {
        return parent;
    }

    public long getSize() {
        return size;
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

    public void setLength(long length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(DirDTO parent) {
        this.parent = parent;
    }

    public void setSize(long size) {
        this.size = size;
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

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("MP3DTO: " + getId() + " " + getName() + " " + getSize() + " " + getAlbum() + " "
                + getBitrate() + " " + getGenre() + " " + getLength() + " " + getTitle() + " "
                + getTrack() + " " + getYear() + " (" + getArtist() + " " + getBand() + " "
                + getComment() + " " + getDate() + " " + getParent() + ")");

        return sb.toString();
    }
}
