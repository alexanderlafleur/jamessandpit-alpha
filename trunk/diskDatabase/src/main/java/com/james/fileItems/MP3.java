package com.james.fileItems;

import com.james.fileItems.util.MP3ParserImpl;
import com.james.helper.StringUtils;

public class MP3 {
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
		this.comment = StringUtils.trim(comment);

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

		sb.append("MP3: " + getId() + " " + getName() + " " + getSize() + ""
				+ getAlbum() + " " + getArtist() + " (" + getParent() + ")");

		return sb.toString();
	}

}