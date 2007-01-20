// MP3Meta
// $Id: MP3Meta.java,v 1.16 2003/07/20 06:46:16 axelwernicke Exp $
//
// Copyright (C) 2002-2003 Axel Wernicke <axel.wernicke@gmx.de>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package de.axelwernicke.mypod;

import java.util.logging.Logger;

import de.axelwernicke.mypod.gui.GuiUtils;

/**
 * holds all information about a single mp3 file
 * 
 * @author axel wernicke
 */
public class MP3Meta implements java.io.Serializable {
    /** jdk1.4 logger */
    private static Logger logger = Logger.getLogger("de.axelwernicke.mypod");

    /** size of media file in bytes */
    private long filesize = -1;

    /** clip has id3 v1 tags */
    private boolean id3v1 = false;

    /** clip has id3 v2 tags */
    private boolean id3v2 = false;

    /** clip is encoded with variable bitrate */
    private boolean vbr = false;

    /** album the clip is contained */
    private String album = "";

    /** artist of the clip */
    private String artist = "";

    /** artists webpage */
    private String artistWebpage = "";

    /** audio file webpage */
    private String audioFileWebpage = "";

    /** audio source webpage */
    private String audioSourceWebpage = "";

    /** bits per minute */
    private String bpm = "";

    /** band */
    private String band = "";

    /** bitrate the file was digitized with */
    private int bitrate = -1;

    /** cd identifier */
    private String cdIdentifier = "";

    /** comment */
    private String comment = "";

    /** commercial */
    private String commercial = "";

    /** commercial information */
    private String commercialInformation = "";

    /** composer */
    private String composer = "";

    /** conductor */
    private String conductor = "";

    /** content groupset */
    private String contentGroupset = "";

    /** clip is copyright protected */
    private boolean copyright = false;

    /** copyright text */
    private String copyrightText = "";

    /** copyright webpage */
    private String copyrightWebpage = "";

    /** date */
    // TODO what date ??
    private String date = "";

    /** clip duration in seconds */
    private long duration = -1;

    /** 0-NONE, 1-5015MS, 2-ILLEGAL, 3-CCITT */
    private int emphasis = -1;

    /** encapsulated object */
    private String encapsulatedObject = "";

    /** encoded by */
    private String encodedBy = "";

    /** encryption method registration */
    private String encryptionMethodRegistration = "";

    /** equalisation */
    private String equalisation = "";

    /** event timing codes */
    private String eventTimingCodes = "";

    /** media file owner */
    private String fileOwner = "";

    /** media file path */
    private String filePath = "";

    /** media file type */
    private String fileType = "";

    /** media file name */
    private String filename = "";

    /** clips genre */
    private String genre = "";

    /** group identification registration */
    private String groupIdentificationRegistration = "";

    /** ISRC */
    private String isrc = "";

    /** initial key */
    private String initialKey = "";

    /** internet radio station name */
    private String internetRadioStationName = "";

    /** internet radio station owner */
    private String internetRadioStationOwner = "";

    /** internet radio station webpage */
    private String internetRadioStationWebpage = "";

    /** language */
    private String language = "";

    /** last modified date */
    private long lastModified = -1;

    /** mpeg layer */
    private int layer = -1;

    /** length in tag */
    private String lengthInTag = "";

    /** lookup table */
    private String lookupTable = "";

    /** lyricist */
    private String lyricist = "";

    /** mpeg level */
    private int mPEGLevel = -1;

    /** media type */
    private String mediaType = "";

    /** mpeg mode 0-Stereo, 1-Joint Stereo, 2-Dual Channel, 3-Mono */
    private int mode = -1;

    /** clip name */
    private String name = "";

    /** clip is original */
    private boolean original = false;

    /** original artist */
    private String originalArtist = "";

    /** original file name */
    private String originalFilename = "";

    /** original lyricist */
    private String originalLyricist = "";

    /** original title */
    private String originalTitle = "";

    /** original year */
    private String originalYear = "";

    /** ownership */
    private String ownership = "";

    /** mp3 has padding */
    private boolean padding = false;

    /** part of set */
    private String partOfSet = "";

    /** payment webpage */
    private String paymentWebpage = "";

    /** picture */
    private String picture = "";

    /**
     * playcounter TODO change playcounter from string to int
     */
    private String playCounter = "";

    /** playlist delay */
    private String playlistDelay = "";

    /** popularimeter */
    private String popularimeter = "";

    /** position synchronization */
    private String positionSynchronization = "";

    /** clip is private */
    private boolean privat = false;

    /** private data */
    private String privateData = "";

    /** clip is protected */
    private boolean protection = false;

    /** publisher */
    private String publisher = "";

    /** publisher webpage */
    private String publishersWebpage = "";

    /** recommended buffer size */
    private String recommendedBufferSize = "";

    /** recording date */
    private String recordingDates = "";

    /** relative volumen adjustment */
    private String relativeVolumenAdjustment = "";

    /** remixer */
    private String remixer = "";

    /** reverb */
    private String reverb = "";

    /** mp3 samplerate */
    private int samplerate = -1;

    /** subtitle */
    private String subtitle = "";

    /** synchronized lyrics */
    private String synchronizedLyrics = "";

    /** synchronized tempo codes */
    private String synchronizedTempoCodes = "";

    /** terms of use */
    private String termsOfUse = "";

    /** time */
    // TODO what time ??
    private String time = "";

    /** clips title */
    private String title = "";

    /** track number */
    private int track = 0;

    /** uniqe file indentifier */
    private String uniqueFileIdentifier = "";

    /** unsynchronized lyrics */
    private String unsynchronizedLyrics = "";

    /** mp3 has checksum */
    private boolean useCRC = false;

    /** mp3 uses compression */
    private boolean useCompression = false;

    /** mp3 uses padding */
    private boolean usePadding = false;

    /** mp3 uses unsynchronization */
    private boolean useUnsynchronization = false;

    /** user defined text */
    private String userDefinedText = "";

    /** user defined url */
    private String userDefinedURL = "";

    /** mp3 uses id3 v1 */
    private boolean writeID3 = false;

    /** mp3 uses id3 v2 */
    private boolean writeID3v2 = false;

    /** year */
    private int year = 0;

    /**
     * Creates a new instance of MP3Meta
     */
    public MP3Meta() {
    }

    /**
     * Gets the filesize formatted.
     * 
     * @return filesize formatted
     */
    public String getFilesizeFormatted() {
        return GuiUtils.formatFilesize(filesize);
    }

    /**
     * Gets the duration formatted
     * 
     * @return formatted string
     */
    public String getDurationFormatted() {
        return GuiUtils.formatTime(duration);
    }

    /**
     * Getter for property album.
     * 
     * @return Value of property album.
     */
    public java.lang.String getAlbum() {
        return album;
    }

    /**
     * Setter for property album.
     * 
     * @param album
     *            New value of property album.
     */
    public void setAlbum(java.lang.String album) {
        if (album != null) {
            album.trim();
        }

        this.album = album;
    }

    /**
     * Getter for property artist.
     * 
     * @return Value of property artist.
     */
    public java.lang.String getArtist() {
        return artist;
    }

    /**
     * Setter for property artist.
     * 
     * @param artist
     *            New value of property artist.
     */
    public void setArtist(java.lang.String artist) {
        if (artist != null) {
            artist.trim();
        }

        this.artist = artist;
    }

    /**
     * Getter for property artistWebpage.
     * 
     * @return Value of property artistWebpage.
     */
    public java.lang.String getArtistWebpage() {
        return artistWebpage;
    }

    /**
     * Setter for property artistWebpage.
     * 
     * @param artistWebpage
     *            New value of property artistWebpage.
     */
    public void setArtistWebpage(java.lang.String artistWebpage) {
        this.artistWebpage = artistWebpage;
    }

    /**
     * Getter for property audioFileWebpage.
     * 
     * @return Value of property audioFileWebpage.
     */
    public java.lang.String getAudioFileWebpage() {
        return audioFileWebpage;
    }

    /**
     * Setter for property audioFileWebpage.
     * 
     * @param audioFileWebpage
     *            New value of property audioFileWebpage.
     */
    public void setAudioFileWebpage(java.lang.String audioFileWebpage) {
        this.audioFileWebpage = audioFileWebpage;
    }

    /**
     * Getter for property audioSourceWebpage.
     * 
     * @return Value of property audioSourceWebpage.
     */
    public java.lang.String getAudioSourceWebpage() {
        return audioSourceWebpage;
    }

    /**
     * Setter for property audioSourceWebpage.
     * 
     * @param audioSourceWebpage
     *            New value of property audioSourceWebpage.
     */
    public void setAudioSourceWebpage(java.lang.String audioSourceWebpage) {
        this.audioSourceWebpage = audioSourceWebpage;
    }

    /**
     * Getter for property band.
     * 
     * @return Value of property band.
     */
    public java.lang.String getBand() {
        return band;
    }

    /**
     * Setter for property band.
     * 
     * @param band
     *            New value of property band.
     */
    public void setBand(java.lang.String band) {
        this.band = band;
    }

    /**
     * Getter for property bitrate.
     * 
     * @return Value of property bitrate.
     */
    public int getBitrate() {
        return bitrate;
    }

    /**
     * Setter for property bitrate.
     * 
     * @param bitrate
     *            New value of property bitrate.
     */
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Getter for property bpm.
     * 
     * @return Value of property bpm.
     */
    public java.lang.String getBpm() {
        return bpm;
    }

    /**
     * Setter for property bpm.
     * 
     * @param bpm
     *            New value of property bpm.
     */
    public void setBpm(java.lang.String bpm) {
        this.bpm = bpm;
    }

    /**
     * Getter for property cdIdentifier.
     * 
     * @return Value of property cdIdentifier.
     */
    public java.lang.String getCdIdentifier() {
        return cdIdentifier;
    }

    /**
     * Setter for property cdIdentifier.
     * 
     * @param cdIdentifier
     *            New value of property cdIdentifier.
     */
    public void setCdIdentifier(java.lang.String cdIdentifier) {
        this.cdIdentifier = cdIdentifier;
    }

    /**
     * Getter for property comment.
     * 
     * @return Value of property comment.
     */
    public java.lang.String getComment() {
        return comment;
    }

    /**
     * Setter for property comment.
     * 
     * @param comment
     *            New value of property comment.
     */
    public void setComment(java.lang.String comment) {
        if (comment != null) {
            comment.trim();
        }
        {
            this.comment = comment;
        }
    }

    /**
     * Getter for property commercial.
     * 
     * @return Value of property commercial.
     */
    public java.lang.String getCommercial() {
        return commercial;
    }

    /**
     * Setter for property commercial.
     * 
     * @param commercial
     *            New value of property commercial.
     */
    public void setCommercial(java.lang.String commercial) {
        this.commercial = commercial;
    }

    /**
     * Getter for property commercialInformation.
     * 
     * @return Value of property commercialInformation.
     */
    public java.lang.String getCommercialInformation() {
        return commercialInformation;
    }

    /**
     * Setter for property commercialInformation.
     * 
     * @param commercialInformation
     *            New value of property commercialInformation.
     */
    public void setCommercialInformation(java.lang.String commercialInformation) {
        this.commercialInformation = commercialInformation;
    }

    /**
     * Getter for property composer.
     * 
     * @return Value of property composer.
     */
    public java.lang.String getComposer() {
        return composer;
    }

    /**
     * Setter for property composer.
     * 
     * @param composer
     *            New value of property composer.
     */
    public void setComposer(java.lang.String composer) {
        this.composer = composer;
    }

    /**
     * Getter for property conductor.
     * 
     * @return Value of property conductor.
     */
    public java.lang.String getConductor() {
        return conductor;
    }

    /**
     * Setter for property conductor.
     * 
     * @param conductor
     *            New value of property conductor.
     */
    public void setConductor(java.lang.String conductor) {
        this.conductor = conductor;
    }

    /**
     * Getter for property contentGroupset.
     * 
     * @return Value of property contentGroupset.
     */
    public java.lang.String getContentGroupset() {
        return contentGroupset;
    }

    /**
     * Setter for property contentGroupset.
     * 
     * @param contentGroupset
     *            New value of property contentGroupset.
     */
    public void setContentGroupset(java.lang.String contentGroupset) {
        this.contentGroupset = contentGroupset;
    }

    /**
     * Getter for property copyright.
     * 
     * @return Value of property copyright.
     */
    public boolean isCopyright() {
        return copyright;
    }

    /**
     * Setter for property copyright.
     * 
     * @param copyright
     *            New value of property copyright.
     */
    public void setCopyright(boolean copyright) {
        this.copyright = copyright;
    }

    /**
     * Getter for property copyrightText.
     * 
     * @return Value of property copyrightText.
     */
    public java.lang.String getCopyrightText() {
        return copyrightText;
    }

    /**
     * Setter for property copyrightText.
     * 
     * @param copyrightText
     *            New value of property copyrightText.
     */
    public void setCopyrightText(java.lang.String copyrightText) {
        this.copyrightText = copyrightText;
    }

    /**
     * Getter for property copyrightWebpage.
     * 
     * @return Value of property copyrightWebpage.
     */
    public java.lang.String getCopyrightWebpage() {
        return copyrightWebpage;
    }

    /**
     * Setter for property copyrightWebpage.
     * 
     * @param copyrightWebpage
     *            New value of property copyrightWebpage.
     */
    public void setCopyrightWebpage(java.lang.String copyrightWebpage) {
        this.copyrightWebpage = copyrightWebpage;
    }

    /**
     * Getter for property date.
     * 
     * @return Value of property date.
     */
    public java.lang.String getDate() {
        return date;
    }

    /**
     * Setter for property date.
     * 
     * @param date
     *            New value of property date.
     */
    public void setDate(java.lang.String date) {
        this.date = date;
    }

    /**
     * Getter for property duration.
     * 
     * @return Value of property duration.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Setter for property duration.
     * 
     * @param duration
     *            New value of property duration.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Getter for property emphasis.
     * 
     * @return Value of property emphasis.
     */
    public int getEmphasis() {
        return emphasis;
    }

    /**
     * Setter for property emphasis.
     * 
     * @param emphasis
     *            New value of property emphasis.
     */
    public void setEmphasis(int emphasis) {
        this.emphasis = emphasis;
    }

    /**
     * Getter for property encapsulatedObject.
     * 
     * @return Value of property encapsulatedObject.
     */
    public java.lang.String getEncapsulatedObject() {
        return encapsulatedObject;
    }

    /**
     * Setter for property encapsulatedObject.
     * 
     * @param encapsulatedObject
     *            New value of property encapsulatedObject.
     */
    public void setEncapsulatedObject(java.lang.String encapsulatedObject) {
        this.encapsulatedObject = encapsulatedObject;
    }

    /**
     * Getter for property encodedBy.
     * 
     * @return Value of property encodedBy.
     */
    public java.lang.String getEncodedBy() {
        return encodedBy;
    }

    /**
     * Setter for property encodedBy.
     * 
     * @param encodedBy
     *            New value of property encodedBy.
     */
    public void setEncodedBy(java.lang.String encodedBy) {
        this.encodedBy = encodedBy;
    }

    /**
     * Getter for property encryptionMethodRegistration.
     * 
     * @return Value of property encryptionMethodRegistration.
     */
    public java.lang.String getEncryptionMethodRegistration() {
        return encryptionMethodRegistration;
    }

    /**
     * Setter for property encryptionMethodRegistration.
     * 
     * @param encryptionMethodRegistration
     *            New value of property encryptionMethodRegistration.
     */
    public void setEncryptionMethodRegistration(java.lang.String encryptionMethodRegistration) {
        this.encryptionMethodRegistration = encryptionMethodRegistration;
    }

    /**
     * Getter for property equalisation.
     * 
     * @return Value of property equalisation.
     */
    public java.lang.String getEqualisation() {
        return equalisation;
    }

    /**
     * Setter for property equalisation.
     * 
     * @param equalisation
     *            New value of property equalisation.
     */
    public void setEqualisation(java.lang.String equalisation) {
        this.equalisation = equalisation;
    }

    /**
     * Getter for property eventTimingCodes.
     * 
     * @return Value of property eventTimingCodes.
     */
    public java.lang.String getEventTimingCodes() {
        return eventTimingCodes;
    }

    /**
     * Setter for property eventTimingCodes.
     * 
     * @param eventTimingCodes
     *            New value of property eventTimingCodes.
     */
    public void setEventTimingCodes(java.lang.String eventTimingCodes) {
        this.eventTimingCodes = eventTimingCodes;
    }

    /**
     * Getter for property filename.
     * 
     * @return Value of property filename.
     */
    public java.lang.String getFilename() {
        return filename;
    }

    /**
     * Setter for property filename.
     * 
     * @param filename
     *            New value of property filename.
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }

    /**
     * Getter for property fileOwner.
     * 
     * @return Value of property fileOwner.
     */
    public java.lang.String getFileOwner() {
        return fileOwner;
    }

    /**
     * Setter for property fileOwner.
     * 
     * @param fileOwner
     *            New value of property fileOwner.
     */
    public void setFileOwner(java.lang.String fileOwner) {
        this.fileOwner = fileOwner;
    }

    /**
     * Getter for property filePath.
     * 
     * @return Value of property filePath.
     */
    public java.lang.String getFilePath() {
        return filePath;
    }

    /**
     * Setter for property filePath.
     * 
     * @param filePath
     *            New value of property filePath.
     */
    public void setFilePath(java.lang.String filePath) {
        this.filePath = filePath;
    }

    /**
     * Getter for property filesize.
     * 
     * @return Value of property filesize.
     */
    public long getFilesize() {
        return filesize;
    }

    /**
     * Setter for property filesize.
     * 
     * @param filesize
     *            New value of property filesize.
     */
    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    /**
     * Getter for property fileType.
     * 
     * @return Value of property fileType.
     */
    public java.lang.String getFileType() {
        return fileType;
    }

    /**
     * Setter for property fileType.
     * 
     * @param fileType
     *            New value of property fileType.
     */
    public void setFileType(java.lang.String fileType) {
        this.fileType = fileType;
    }

    /**
     * Getter for property genre.
     * 
     * @return Value of property genre.
     */
    public java.lang.String getGenre() {
        return genre;
    }

    /**
     * Setter for property genre.
     * 
     * @param genre
     *            New value of property genre.
     */
    public void setGenre(java.lang.String genre) {
        if (genre != null) {
            genre.trim();
        }

        this.genre = genre;
    }

    /**
     * Getter for property groupIdentificationRegistration.
     * 
     * @return Value of property groupIdentificationRegistration.
     */
    public java.lang.String getGroupIdentificationRegistration() {
        return groupIdentificationRegistration;
    }

    /**
     * Setter for property groupIdentificationRegistration.
     * 
     * @param groupIdentificationRegistration
     *            New value of property groupIdentificationRegistration.
     */
    public void setGroupIdentificationRegistration(java.lang.String groupIdentificationRegistration) {
        this.groupIdentificationRegistration = groupIdentificationRegistration;
    }

    /**
     * Getter for property id3v1.
     * 
     * @return Value of property id3v1.
     */
    public boolean isId3v1() {
        return id3v1;
    }

    /**
     * Setter for property id3v1.
     * 
     * @param id3v1
     *            New value of property id3v1.
     */
    public void setId3v1(boolean id3v1) {
        this.id3v1 = id3v1;
    }

    /**
     * Getter for property id3v2.
     * 
     * @return Value of property id3v2.
     */
    public boolean isId3v2() {
        return id3v2;
    }

    /**
     * Setter for property id3v2.
     * 
     * @param id3v2
     *            New value of property id3v2.
     */
    public void setId3v2(boolean id3v2) {
        this.id3v2 = id3v2;
    }

    /**
     * Getter for property initialKey.
     * 
     * @return Value of property initialKey.
     */
    public java.lang.String getInitialKey() {
        return initialKey;
    }

    /**
     * Setter for property initialKey.
     * 
     * @param initialKey
     *            New value of property initialKey.
     */
    public void setInitialKey(java.lang.String initialKey) {
        this.initialKey = initialKey;
    }

    /**
     * Getter for property internetRadioStationName.
     * 
     * @return Value of property internetRadioStationName.
     */
    public java.lang.String getInternetRadioStationName() {
        return internetRadioStationName;
    }

    /**
     * Setter for property internetRadioStationName.
     * 
     * @param internetRadioStationName
     *            New value of property internetRadioStationName.
     */
    public void setInternetRadioStationName(java.lang.String internetRadioStationName) {
        this.internetRadioStationName = internetRadioStationName;
    }

    /**
     * Getter for property internetRadioStationOwner.
     * 
     * @return Value of property internetRadioStationOwner.
     */
    public java.lang.String getInternetRadioStationOwner() {
        return internetRadioStationOwner;
    }

    /**
     * Setter for property internetRadioStationOwner.
     * 
     * @param internetRadioStationOwner
     *            New value of property internetRadioStationOwner.
     */
    public void setInternetRadioStationOwner(java.lang.String internetRadioStationOwner) {
        this.internetRadioStationOwner = internetRadioStationOwner;
    }

    /**
     * Getter for property internetRadioStationWebpage.
     * 
     * @return Value of property internetRadioStationWebpage.
     */
    public java.lang.String getInternetRadioStationWebpage() {
        return internetRadioStationWebpage;
    }

    /**
     * Setter for property internetRadioStationWebpage.
     * 
     * @param internetRadioStationWebpage
     *            New value of property internetRadioStationWebpage.
     */
    public void setInternetRadioStationWebpage(java.lang.String internetRadioStationWebpage) {
        this.internetRadioStationWebpage = internetRadioStationWebpage;
    }

    /**
     * Getter for property isrc.
     * 
     * @return Value of property isrc.
     */
    public java.lang.String getIsrc() {
        return isrc;
    }

    /**
     * Setter for property isrc.
     * 
     * @param isrc
     *            New value of property isrc.
     */
    public void setIsrc(java.lang.String isrc) {
        this.isrc = isrc;
    }

    /**
     * Getter for property language.
     * 
     * @return Value of property language.
     */
    public java.lang.String getLanguage() {
        return language;
    }

    /**
     * Setter for property language.
     * 
     * @param language
     *            New value of property language.
     */
    public void setLanguage(java.lang.String language) {
        this.language = language;
    }

    /**
     * Getter for property lastModified.
     * 
     * @return Value of property lastModified.
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Setter for property lastModified.
     * 
     * @param lastModified
     *            New value of property lastModified.
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Getter for property layer.
     * 
     * @return Value of property layer.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Setter for property layer.
     * 
     * @param layer
     *            New value of property layer.
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

    /**
     * Getter for property lengthInTag.
     * 
     * @return Value of property lengthInTag.
     */
    public java.lang.String getLengthInTag() {
        return lengthInTag;
    }

    /**
     * Setter for property lengthInTag.
     * 
     * @param lengthInTag
     *            New value of property lengthInTag.
     */
    public void setLengthInTag(java.lang.String lengthInTag) {
        this.lengthInTag = lengthInTag;
    }

    /**
     * Getter for property lookupTable.
     * 
     * @return Value of property lookupTable.
     */
    public java.lang.String getLookupTable() {
        return lookupTable;
    }

    /**
     * Setter for property lookupTable.
     * 
     * @param lookupTable
     *            New value of property lookupTable.
     */
    public void setLookupTable(java.lang.String lookupTable) {
        this.lookupTable = lookupTable;
    }

    /**
     * Getter for property lyricist.
     * 
     * @return Value of property lyricist.
     */
    public java.lang.String getLyricist() {
        return lyricist;
    }

    /**
     * Setter for property lyricist.
     * 
     * @param lyricist
     *            New value of property lyricist.
     */
    public void setLyricist(java.lang.String lyricist) {
        this.lyricist = lyricist;
    }

    /**
     * Getter for property mediaType.
     * 
     * @return Value of property mediaType.
     */
    public java.lang.String getMediaType() {
        return mediaType;
    }

    /**
     * Setter for property mediaType.
     * 
     * @param mediaType
     *            New value of property mediaType.
     */
    public void setMediaType(java.lang.String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Getter for property mode.
     * 
     * @return Value of property mode.
     */
    public int getMode() {
        return mode;
    }

    /**
     * Setter for property mode.
     * 
     * @param mode
     *            New value of property mode.
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Getter for property mPEGLevel.
     * 
     * @return Value of property mPEGLevel.
     */
    public int getMPEGLevel() {
        return mPEGLevel;
    }

    /**
     * Setter for property mPEGLevel.
     * 
     * @param mPEGLevel
     *            New value of property mPEGLevel.
     */
    public void setMPEGLevel(int mPEGLevel) {
        this.mPEGLevel = mPEGLevel;
    }

    /**
     * Getter for property name. Since name is equal to filename, name is not in
     * use anymore.
     * 
     * @return Value of property name.
     */
    public java.lang.String getName() {
        logger.warning("meta data name is not in use anymore. Do not try to read!");
        return name;
    }

    /**
     * Setter for property name.
     * 
     * @param name
     *            New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Getter for property original.
     * 
     * @return Value of property original.
     */
    public boolean isOriginal() {
        return original;
    }

    /**
     * Setter for property original.
     * 
     * @param original
     *            New value of property original.
     */
    public void setOriginal(boolean original) {
        this.original = original;
    }

    /**
     * Getter for property originalFilename.
     * 
     * @return Value of property originalFilename.
     */
    public java.lang.String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * Setter for property originalFilename.
     * 
     * @param originalFilename
     *            New value of property originalFilename.
     */
    public void setOriginalFilename(java.lang.String originalFilename) {
        this.originalFilename = originalFilename;
    }

    /**
     * Getter for property originalLyricist.
     * 
     * @return Value of property originalLyricist.
     */
    public java.lang.String getOriginalLyricist() {
        return originalLyricist;
    }

    /**
     * Setter for property originalLyricist.
     * 
     * @param originalLyricist
     *            New value of property originalLyricist.
     */
    public void setOriginalLyricist(java.lang.String originalLyricist) {
        this.originalLyricist = originalLyricist;
    }

    /**
     * Getter for property originalTitle.
     * 
     * @return Value of property originalTitle.
     */
    public java.lang.String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Setter for property originalTitle.
     * 
     * @param originalTitle
     *            New value of property originalTitle.
     */
    public void setOriginalTitle(java.lang.String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * Getter for property originalYear.
     * 
     * @return Value of property originalYear.
     */
    public java.lang.String getOriginalYear() {
        return originalYear;
    }

    /**
     * Setter for property originalYear.
     * 
     * @param originalYear
     *            New value of property originalYear.
     */
    public void setOriginalYear(java.lang.String originalYear) {
        this.originalYear = originalYear;
    }

    /**
     * Getter for property ownership.
     * 
     * @return Value of property ownership.
     */
    public java.lang.String getOwnership() {
        return ownership;
    }

    /**
     * Setter for property ownership.
     * 
     * @param ownership
     *            New value of property ownership.
     */
    public void setOwnership(java.lang.String ownership) {
        this.ownership = ownership;
    }

    /**
     * Getter for property padding.
     * 
     * @return Value of property padding.
     */
    public boolean isPadding() {
        return padding;
    }

    /**
     * Setter for property padding.
     * 
     * @param padding
     *            New value of property padding.
     */
    public void setPadding(boolean padding) {
        this.padding = padding;
    }

    /**
     * Getter for property partOfSet.
     * 
     * @return Value of property partOfSet.
     */
    public java.lang.String getPartOfSet() {
        return partOfSet;
    }

    /**
     * Setter for property partOfSet.
     * 
     * @param partOfSet
     *            New value of property partOfSet.
     */
    public void setPartOfSet(java.lang.String partOfSet) {
        this.partOfSet = partOfSet;
    }

    /**
     * Getter for property paymentWebpage.
     * 
     * @return Value of property paymentWebpage.
     */
    public java.lang.String getPaymentWebpage() {
        return paymentWebpage;
    }

    /**
     * Setter for property paymentWebpage.
     * 
     * @param paymentWebpage
     *            New value of property paymentWebpage.
     */
    public void setPaymentWebpage(java.lang.String paymentWebpage) {
        this.paymentWebpage = paymentWebpage;
    }

    /**
     * Getter for property picture.
     * 
     * @return Value of property picture.
     */
    public java.lang.String getPicture() {
        return picture;
    }

    /**
     * Setter for property picture.
     * 
     * @param picture
     *            New value of property picture.
     */
    public void setPicture(java.lang.String picture) {
        this.picture = picture;
    }

    /**
     * Getter for property playCounter.
     * 
     * @return Value of property playCounter.
     */
    public java.lang.String getPlayCounter() {
        return playCounter;
    }

    /**
     * Setter for property playCounter.
     * 
     * @param playCounter
     *            New value of property playCounter.
     */
    public void setPlayCounter(java.lang.String playCounter) {
        this.playCounter = playCounter;
    }

    /**
     * Getter for property playlistDelay.
     * 
     * @return Value of property playlistDelay.
     */
    public java.lang.String getPlaylistDelay() {
        return playlistDelay;
    }

    /**
     * Setter for property playlistDelay.
     * 
     * @param playlistDelay
     *            New value of property playlistDelay.
     */
    public void setPlaylistDelay(java.lang.String playlistDelay) {
        this.playlistDelay = playlistDelay;
    }

    /**
     * Getter for property popularimeter.
     * 
     * @return Value of property popularimeter.
     */
    public java.lang.String getPopularimeter() {
        return popularimeter;
    }

    /**
     * Setter for property popularimeter.
     * 
     * @param popularimeter
     *            New value of property popularimeter.
     */
    public void setPopularimeter(java.lang.String popularimeter) {
        this.popularimeter = popularimeter;
    }

    /**
     * Getter for property positionSynchronization.
     * 
     * @return Value of property positionSynchronization.
     */
    public java.lang.String getPositionSynchronization() {
        return positionSynchronization;
    }

    /**
     * Setter for property positionSynchronization.
     * 
     * @param positionSynchronization
     *            New value of property positionSynchronization.
     */
    public void setPositionSynchronization(java.lang.String positionSynchronization) {
        this.positionSynchronization = positionSynchronization;
    }

    /**
     * Getter for property privat.
     * 
     * @return Value of property privat.
     */
    public boolean isPrivat() {
        return privat;
    }

    /**
     * Setter for property privat.
     * 
     * @param privat
     *            New value of property privat.
     */
    public void setPrivat(boolean privat) {
        this.privat = privat;
    }

    /**
     * Getter for property privateData.
     * 
     * @return Value of property privateData.
     */
    public java.lang.String getPrivateData() {
        return privateData;
    }

    /**
     * Setter for property privateData.
     * 
     * @param privateData
     *            New value of property privateData.
     */
    public void setPrivateData(java.lang.String privateData) {
        this.privateData = privateData;
    }

    /**
     * Getter for property protection.
     * 
     * @return Value of property protection.
     */
    public boolean isProtection() {
        return protection;
    }

    /**
     * Setter for property protection.
     * 
     * @param protection
     *            New value of property protection.
     */
    public void setProtection(boolean protection) {
        this.protection = protection;
    }

    /**
     * Getter for property publisher.
     * 
     * @return Value of property publisher.
     */
    public java.lang.String getPublisher() {
        return publisher;
    }

    /**
     * Setter for property publisher.
     * 
     * @param publisher
     *            New value of property publisher.
     */
    public void setPublisher(java.lang.String publisher) {
        this.publisher = publisher;
    }

    /**
     * Getter for property publishersWebpage.
     * 
     * @return Value of property publishersWebpage.
     */
    public java.lang.String getPublishersWebpage() {
        return publishersWebpage;
    }

    /**
     * Setter for property publishersWebpage.
     * 
     * @param publishersWebpage
     *            New value of property publishersWebpage.
     */
    public void setPublishersWebpage(java.lang.String publishersWebpage) {
        this.publishersWebpage = publishersWebpage;
    }

    /**
     * Getter for property recommendedBufferSize.
     * 
     * @return Value of property recommendedBufferSize.
     */
    public java.lang.String getRecommendedBufferSize() {
        return recommendedBufferSize;
    }

    /**
     * Setter for property recommendedBufferSize.
     * 
     * @param recommendedBufferSize
     *            New value of property recommendedBufferSize.
     */
    public void setRecommendedBufferSize(java.lang.String recommendedBufferSize) {
        this.recommendedBufferSize = recommendedBufferSize;
    }

    /**
     * Getter for property recordingDates.
     * 
     * @return Value of property recordingDates.
     */
    public java.lang.String getRecordingDates() {
        return recordingDates;
    }

    /**
     * Setter for property recordingDates.
     * 
     * @param recordingDates
     *            New value of property recordingDates.
     */
    public void setRecordingDates(java.lang.String recordingDates) {
        this.recordingDates = recordingDates;
    }

    /**
     * Getter for property relativeVolumenAdjustment.
     * 
     * @return Value of property relativeVolumenAdjustment.
     */
    public java.lang.String getRelativeVolumenAdjustment() {
        return relativeVolumenAdjustment;
    }

    /**
     * Setter for property relativeVolumenAdjustment.
     * 
     * @param relativeVolumenAdjustment
     *            New value of property relativeVolumenAdjustment.
     */
    public void setRelativeVolumenAdjustment(java.lang.String relativeVolumenAdjustment) {
        this.relativeVolumenAdjustment = relativeVolumenAdjustment;
    }

    /**
     * Getter for property remixer.
     * 
     * @return Value of property remixer.
     */
    public java.lang.String getRemixer() {
        return remixer;
    }

    /**
     * Setter for property remixer.
     * 
     * @param remixer
     *            New value of property remixer.
     */
    public void setRemixer(java.lang.String remixer) {
        this.remixer = remixer;
    }

    /**
     * Getter for property reverb.
     * 
     * @return Value of property reverb.
     */
    public java.lang.String getReverb() {
        return reverb;
    }

    /**
     * Setter for property reverb.
     * 
     * @param reverb
     *            New value of property reverb.
     */
    public void setReverb(java.lang.String reverb) {
        this.reverb = reverb;
    }

    /**
     * Getter for property samplerate.
     * 
     * @return Value of property samplerate.
     */
    public int getSamplerate() {
        return samplerate;
    }

    /**
     * Setter for property samplerate.
     * 
     * @param samplerate
     *            New value of property samplerate.
     */
    public void setSamplerate(int samplerate) {
        this.samplerate = samplerate;
    }

    /**
     * Getter for property subtitle.
     * 
     * @return Value of property subtitle.
     */
    public java.lang.String getSubtitle() {
        return subtitle;
    }

    /**
     * Setter for property subtitle.
     * 
     * @param subtitle
     *            New value of property subtitle.
     */
    public void setSubtitle(java.lang.String subtitle) {
        this.subtitle = subtitle;
    }

    /**
     * Getter for property synchronizedLyrics.
     * 
     * @return Value of property synchronizedLyrics.
     */
    public java.lang.String getSynchronizedLyrics() {
        return synchronizedLyrics;
    }

    /**
     * Setter for property synchronizedLyrics.
     * 
     * @param synchronizedLyrics
     *            New value of property synchronizedLyrics.
     */
    public void setSynchronizedLyrics(java.lang.String synchronizedLyrics) {
        this.synchronizedLyrics = synchronizedLyrics;
    }

    /**
     * Getter for property synchronizedTempoCodes.
     * 
     * @return Value of property synchronizedTempoCodes.
     */
    public java.lang.String getSynchronizedTempoCodes() {
        return synchronizedTempoCodes;
    }

    /**
     * Setter for property synchronizedTempoCodes.
     * 
     * @param synchronizedTempoCodes
     *            New value of property synchronizedTempoCodes.
     */
    public void setSynchronizedTempoCodes(java.lang.String synchronizedTempoCodes) {
        this.synchronizedTempoCodes = synchronizedTempoCodes;
    }

    /**
     * Getter for property termsOfUse.
     * 
     * @return Value of property termsOfUse.
     */
    public java.lang.String getTermsOfUse() {
        return termsOfUse;
    }

    /**
     * Setter for property termsOfUse.
     * 
     * @param termsOfUse
     *            New value of property termsOfUse.
     */
    public void setTermsOfUse(java.lang.String termsOfUse) {
        this.termsOfUse = termsOfUse;
    }

    /**
     * Getter for property time.
     * 
     * @return Value of property time.
     */
    public java.lang.String getTime() {
        return time;
    }

    /**
     * Setter for property time.
     * 
     * @param time
     *            New value of property time.
     */
    public void setTime(java.lang.String time) {
        this.time = time;
    }

    /**
     * Getter for property title.
     * 
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Setter for property title.
     * 
     * @param title
     *            New value of property title.
     */
    public void setTitle(java.lang.String title) {
        if (title != null) {
            title.trim();
        }

        this.title = title;
    }

    /**
     * Getter for property track.
     * 
     * @return Value of property track.
     */
    public int getTrack() {
        return track;
    }

    /**
     * Setter for property track.
     * 
     * @param track
     *            New value of property track.
     */
    public void setTrack(int track) {
        this.track = track;
    }

    /**
     * Getter for property uniqueFileIdentifier.
     * 
     * @return Value of property uniqueFileIdentifier.
     */
    public java.lang.String getUniqueFileIdentifier() {
        return uniqueFileIdentifier;
    }

    /**
     * Setter for property uniqueFileIdentifier.
     * 
     * @param uniqueFileIdentifier
     *            New value of property uniqueFileIdentifier.
     */
    public void setUniqueFileIdentifier(java.lang.String uniqueFileIdentifier) {
        this.uniqueFileIdentifier = uniqueFileIdentifier;
    }

    /**
     * Getter for property unsynchronizedLyrics.
     * 
     * @return Value of property unsynchronizedLyrics.
     */
    public java.lang.String getUnsynchronizedLyrics() {
        return unsynchronizedLyrics;
    }

    /**
     * Setter for property unsynchronizedLyrics.
     * 
     * @param unsynchronizedLyrics
     *            New value of property unsynchronizedLyrics.
     */
    public void setUnsynchronizedLyrics(java.lang.String unsynchronizedLyrics) {
        this.unsynchronizedLyrics = unsynchronizedLyrics;
    }

    /**
     * Getter for property useCompression.
     * 
     * @return Value of property useCompression.
     */
    public boolean isUseCompression() {
        return useCompression;
    }

    /**
     * Setter for property useCompression.
     * 
     * @param useCompression
     *            New value of property useCompression.
     */
    public void setUseCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    /**
     * Getter for property useCRC.
     * 
     * @return Value of property useCRC.
     */
    public boolean isUseCRC() {
        return useCRC;
    }

    /**
     * Setter for property useCRC.
     * 
     * @param useCRC
     *            New value of property useCRC.
     */
    public void setUseCRC(boolean useCRC) {
        this.useCRC = useCRC;
    }

    /**
     * Getter for property usePadding.
     * 
     * @return Value of property usePadding.
     */
    public boolean isUsePadding() {
        return usePadding;
    }

    /**
     * Setter for property usePadding.
     * 
     * @param usePadding
     *            New value of property usePadding.
     */
    public void setUsePadding(boolean usePadding) {
        this.usePadding = usePadding;
    }

    /**
     * Getter for property userDefinedText.
     * 
     * @return Value of property userDefinedText.
     */
    public java.lang.String getUserDefinedText() {
        return userDefinedText;
    }

    /**
     * Setter for property userDefinedText.
     * 
     * @param userDefinedText
     *            New value of property userDefinedText.
     */
    public void setUserDefinedText(java.lang.String userDefinedText) {
        this.userDefinedText = userDefinedText;
    }

    /**
     * Getter for property userDefinedURL.
     * 
     * @return Value of property userDefinedURL.
     */
    public java.lang.String getUserDefinedURL() {
        return userDefinedURL;
    }

    /**
     * Setter for property userDefinedURL.
     * 
     * @param userDefinedURL
     *            New value of property userDefinedURL.
     */
    public void setUserDefinedURL(java.lang.String userDefinedURL) {
        this.userDefinedURL = userDefinedURL;
    }

    /**
     * Getter for property useUnsynchronization.
     * 
     * @return Value of property useUnsynchronization.
     */
    public boolean isUseUnsynchronization() {
        return useUnsynchronization;
    }

    /**
     * Setter for property useUnsynchronization.
     * 
     * @param useUnsynchronization
     *            New value of property useUnsynchronization.
     */
    public void setUseUnsynchronization(boolean useUnsynchronization) {
        this.useUnsynchronization = useUnsynchronization;
    }

    /**
     * Getter for property vbr.
     * 
     * @return Value of property vbr.
     */
    public boolean isVbr() {
        return vbr;
    }

    /**
     * Setter for property vbr.
     * 
     * @param vbr
     *            New value of property vbr.
     */
    public void setVbr(boolean vbr) {
        this.vbr = vbr;
    }

    /**
     * Getter for property writeID3.
     * 
     * @return Value of property writeID3.
     */
    public boolean isWriteID3() {
        return writeID3;
    }

    /**
     * Setter for property writeID3.
     * 
     * @param writeID3
     *            New value of property writeID3.
     */
    public void setWriteID3(boolean writeID3) {
        this.writeID3 = writeID3;
    }

    /**
     * Getter for property writeID3v2.
     * 
     * @return Value of property writeID3v2.
     */
    public boolean isWriteID3v2() {
        return writeID3v2;
    }

    /**
     * Setter for property writeID3v2.
     * 
     * @param writeID3v2
     *            New value of property writeID3v2.
     */
    public void setWriteID3v2(boolean writeID3v2) {
        this.writeID3v2 = writeID3v2;
    }

    /**
     * Getter for property year.
     * 
     * @return Value of property year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Setter for property year.
     * 
     * @param year
     *            New value of property year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Getter for property originalArtist.
     * 
     * @return Value of property originalArtist.
     */
    public java.lang.String getOriginalArtist() {
        return originalArtist;
    }

    /**
     * Setter for property originalArtist.
     * 
     * @param originalArtist
     *            New value of property originalArtist.
     */
    public void setOriginalArtist(java.lang.String originalArtist) {
        this.originalArtist = originalArtist;
    }

    /**
     * Converts a genre code from winAmp or CDEx to text.
     * 
     * @param genre
     *            code like "(27)"
     * @return genre as text e.g. "Rock"
     */
    public static String convertWinAmpGenre(String genre) {
        if (genre == null) {
            genre = "";
        }

        genre = genre.trim();
        int genreCode;

        // check for paranthesis
        if (genre.startsWith("(") && genre.endsWith(")")) {
            try {
                genreCode = Integer.parseInt(genre.substring(1, genre.length() - 1));

                switch (genreCode) {
                // The following genres are defined in ID3v1
                case 0:
                    genre = "Blues";
                    break;
                case 1:
                    genre = "Classic Rock";
                    break;
                case 2:
                    genre = "Country";
                    break;
                case 3:
                    genre = "Dance";
                    break;
                case 4:
                    genre = "Disco";
                    break;
                case 5:
                    genre = "Funk";
                    break;
                case 6:
                    genre = "Grunge";
                    break;
                case 7:
                    genre = "Hip-Hop";
                    break;
                case 8:
                    genre = "Jazz";
                    break;
                case 9:
                    genre = "Metal";
                    break;
                case 10:
                    genre = "New Age";
                    break;
                case 11:
                    genre = "Oldies";
                    break;
                case 12:
                    genre = "Other";
                    break;
                case 13:
                    genre = "Pop";
                    break;
                case 14:
                    genre = "R&B";
                    break;
                case 15:
                    genre = "Rap";
                    break;
                case 16:
                    genre = "Reggae";
                    break;
                case 17:
                    genre = "Rock";
                    break;
                case 18:
                    genre = "Techno";
                    break;
                case 19:
                    genre = "Industrial";
                    break;
                case 20:
                    genre = "Alternative";
                    break;
                case 21:
                    genre = "Ska";
                    break;
                case 22:
                    genre = "Death Metal";
                    break;
                case 23:
                    genre = "Pranks";
                    break;
                case 24:
                    genre = "Soundtrack";
                    break;
                case 25:
                    genre = "Euro-Techno";
                    break;
                case 26:
                    genre = "Ambient";
                    break;
                case 27:
                    genre = "Trip-Hop";
                    break;
                case 28:
                    genre = "Vocal";
                    break;
                case 29:
                    genre = "Jazz+Funk";
                    break;
                case 30:
                    genre = "Fusion";
                    break;
                case 31:
                    genre = "Trance";
                    break;
                case 32:
                    genre = "Classical";
                    break;
                case 33:
                    genre = "Instrumental";
                    break;
                case 34:
                    genre = "Acid";
                    break;
                case 35:
                    genre = "House";
                    break;
                case 36:
                    genre = "Game";
                    break;
                case 37:
                    genre = "Sound Clip";
                    break;
                case 38:
                    genre = "Gospel";
                    break;
                case 39:
                    genre = "Noise";
                    break;
                case 40:
                    genre = "AlternRock";
                    break;
                case 41:
                    genre = "Bass";
                    break;
                case 42:
                    genre = "Soul";
                    break;
                case 43:
                    genre = "Punk";
                    break;
                case 44:
                    genre = "Space";
                    break;
                case 45:
                    genre = "Meditative";
                    break;
                case 46:
                    genre = "Instrumental Pop";
                    break;
                case 47:
                    genre = "Instrumental Rock";
                    break;
                case 48:
                    genre = "Ethnic";
                    break;
                case 49:
                    genre = "Gothic";
                    break;
                case 50:
                    genre = "Darkwave";
                    break;
                case 51:
                    genre = "Techno-Industrial";
                    break;
                case 52:
                    genre = "Electronic";
                    break;
                case 53:
                    genre = "Pop-Folk";
                    break;
                case 54:
                    genre = "Eurodance";
                    break;
                case 55:
                    genre = "Dream";
                    break;
                case 56:
                    genre = "Southern Rock";
                    break;
                case 57:
                    genre = "Comedy";
                    break;
                case 58:
                    genre = "Cult";
                    break;
                case 59:
                    genre = "Gangsta";
                    break;
                case 60:
                    genre = "Top 40";
                    break;
                case 61:
                    genre = "Christian Rap";
                    break;
                case 62:
                    genre = "Pop/Funk";
                    break;
                case 63:
                    genre = "Jungle";
                    break;
                case 64:
                    genre = "Native American";
                    break;
                case 65:
                    genre = "Cabaret";
                    break;
                case 66:
                    genre = "New Wave";
                    break;
                case 67:
                    genre = "Psychadelic";
                    break;
                case 68:
                    genre = "Rave";
                    break;
                case 69:
                    genre = "Showtunes";
                    break;
                case 70:
                    genre = "Trailer";
                    break;
                case 71:
                    genre = "Lo-Fi";
                    break;
                case 72:
                    genre = "Tribal";
                    break;
                case 73:
                    genre = "Acid Punk";
                    break;
                case 74:
                    genre = "Acid Jazz";
                    break;
                case 75:
                    genre = "Polka";
                    break;
                case 76:
                    genre = "Retro";
                    break;
                case 77:
                    genre = "Musical";
                    break;
                case 78:
                    genre = "Rock & Roll";
                    break;
                case 79:
                    genre = "Hard Rock";
                    break;

                // The following genres are Winamp extensions
                case 80:
                    genre = "Folk";
                    break;
                case 81:
                    genre = "Folk-Rock";
                    break;
                case 82:
                    genre = "National Folk";
                    break;
                case 83:
                    genre = "Swing";
                    break;
                case 84:
                    genre = "Fast Fusion";
                    break;
                case 85:
                    genre = "Bebob";
                    break;
                case 86:
                    genre = "Latin";
                    break;
                case 87:
                    genre = "Revival";
                    break;
                case 88:
                    genre = "Celtic";
                    break;
                case 89:
                    genre = "Bluegrass";
                    break;
                case 90:
                    genre = "Avantgarde";
                    break;
                case 91:
                    genre = "Gothic Rock";
                    break;
                case 92:
                    genre = "Progressive Rock";
                    break;
                case 93:
                    genre = "Psychedelic Rock";
                    break;
                case 94:
                    genre = "Symphonic Rock";
                    break;
                case 95:
                    genre = "Slow Rock";
                    break;
                case 96:
                    genre = "Big Band";
                    break;
                case 97:
                    genre = "Chorus";
                    break;
                case 98:
                    genre = "Easy Listening";
                    break;
                case 99:
                    genre = "Acoustic";
                    break;
                case 100:
                    genre = "Humour";
                    break;
                case 101:
                    genre = "Speech";
                    break;
                case 102:
                    genre = "Chanson";
                    break;
                case 103:
                    genre = "Opera";
                    break;
                case 104:
                    genre = "Chamber Music";
                    break;
                case 105:
                    genre = "Sonata";
                    break;
                case 106:
                    genre = "Symphony";
                    break;
                case 107:
                    genre = "Booty Bass";
                    break;
                case 108:
                    genre = "Primus";
                    break;
                case 109:
                    genre = "Porn Groove";
                    break;
                case 110:
                    genre = "Satire";
                    break;
                case 111:
                    genre = "Slow Jam";
                    break;
                case 112:
                    genre = "Club";
                    break;
                case 113:
                    genre = "Tango";
                    break;
                case 114:
                    genre = "Samba";
                    break;
                case 115:
                    genre = "Folklore";
                    break;
                case 116:
                    genre = "Ballad";
                    break;
                case 117:
                    genre = "Power Ballad";
                    break;
                case 118:
                    genre = "Rhythmic Soul";
                    break;
                case 119:
                    genre = "Freestyle";
                    break;
                case 120:
                    genre = "Duet";
                    break;
                case 121:
                    genre = "Punk Rock";
                    break;
                case 122:
                    genre = "Drum Solo";
                    break;
                case 123:
                    genre = "A capella";
                    break;
                case 124:
                    genre = "Euro-House";
                    break;
                case 125:
                    genre = "Dance Hall";
                    break;
                default:
                    genre = "";
                }
            } catch (NumberFormatException nfe) {
                logger.warning("Exception raised:" + nfe.getMessage());
            }
        }

        return genre;
    }
}
