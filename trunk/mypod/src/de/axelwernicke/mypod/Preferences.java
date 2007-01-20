// Preferences
// $Id: Preferences.java,v 1.18 2003/08/03 09:45:28 axelwernicke Exp $
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

import java.awt.Rectangle;

/**
 * manages all preferences for myPod
 * 
 * @author axel.wernicke@gmx.de
 */
public class Preferences implements java.lang.Cloneable, java.io.Serializable {
    /** path to the ipod (mountpoint or device) */
    private String iPodPath = "";

    /** path to the executable for the external player */
    private String playerPath = "";

    /** add action enables */
    private boolean addEnabled = false;

    /** parameter to use the add action with the external player */
    private String addParameter = "";

    /** append action enabled */
    private boolean appendEnabled = false;

    /** parameter to use the append action with the external player */
    private String appendParameter = "/ADD";

    /** position of the application window */
    private Rectangle mainFramePosition = new Rectangle(100, 50, 800, 600);

    /** position of the playlist / clips table divider */
    private int dividerMainPosition = 200;

    /** position of the artist spreader divider */
    private int dividerArtistViewPosition = 150;

    /** position of the album spreader divider */
    private int dividerAlbumViewPosition = 150;

    /** position of the genre spreader divider */
    private int dividerGenreViewPosition = 100;

    /** name of the look and feel */
    private String lookAndFeelName = "";

    /** column width optimization in the clips table enabled ? */
    private boolean optimizeColumnWidth = true;

    /* hiding of empty columns in the clips table enabled? */
    private boolean hideEmptyColumns = false;

    /** recently used scan path */
    private String recentScanPath = "";

    /** recently used playlist path */
    private String recentPlaylistPath = "";

    /** recently used external player path */
    private String recentExtPlayerPath = "";

    /** recently used ipod path */
    private String recentIPodPath = "";

    // list view preferences
    /**
     * defines the default preferences for each column of a list view model.
     * values are: <BR> - visibility (1 means show this column in list view)
     * <BR> - width (width of column) <BR> - position (position of the visible
     * column)
     */
    private int listViewColumnInfo[][] = { { 1, 200, -1 }, // ALBUM_INDEX = 0;
            { 1, 150, -1 }, // ARTIST_INDEX = 1;
            { 1, 10, -1 }, // ARTISTWEBPAGE_INDEX = 2;
            { 1, 50, -1 }, // AUDIOFILEWEBPAGE_INDEX = 3;
            { 1, 50, -1 }, // AUDIOSOURCEWEBPAGE_INDEX = 4;
            { 1, 50, -1 }, // BAND_INDEX = 5;
            { 1, 50, -1 }, // BITRATE_INDEX = 6;
            { 1, 50, -1 }, // BPM_INDEX = 7;
            { 0, 50, -1 }, // CDIDENTIFIER_INDEX = 8;
            { 1, 50, -1 }, // COMMENT_INDEX = 9;
            { 1, 50, -1 }, // COMMERCIAL_INDEX = 10;
            { 1, 50, -1 }, // COMMERCIALINFO_INDEX = 11;
            { 1, 50, -1 }, // COMPOSER_INDEX = 12;
            { 1, 50, -1 }, // CONDUCTOR_INDEX = 13;
            { 1, 50, -1 }, // CONTENTGROUPSET_INDEX = 14;
            { 1, 50, -1 }, // COPYRIGHT_INDEX = 15;
            { 1, 50, -1 }, // COPYRIGHTTEXT_INDEX = 16;
            { 1, 50, -1 }, // COPYRIGHTWEBPAGE_INDEX = 17;
            { 0, 50, -1 }, // DATE_INDEX = 18;
            { 1, 50, -1 }, // DURATION_INDEX = 19;
            { 1, 50, -1 }, // EMPHASIS_INDEX = 20;
            { 0, 50, -1 }, // ENCAPSULATEDOBJECT_INDEX = 21;
            { 1, 50, -1 }, // ENCODEDBY_INDEX = 22;
            { 0, 50, -1 }, // ENCRYPTIONMETHODREGISTRATION_INDEX = 23;
            { 0, 50, -1 }, // EQUALISATION_INDEX = 24;
            { 0, 50, -1 }, // EVENTTIMINGCODES_INDEX = 25;
            { 1, 50, -1 }, // LASTMODIFIED_INDEX = 26;
            { 1, 250, -1 }, // FILENAME_INDEX = 27;
            { 0, 50, -1 }, // FILEOWNER_INDEX = 28;
            { 1, 250, -1 }, // FILEPATH_INDEX = 29;
            { 1, 50, -1 }, // FILESIZE_INDEX = 30;
            { 1, 50, -1 }, // FILETYPE_INDEX = 31;
            { 1, 50, -1 }, // GENRE_INDEX = 32;
            { 0, 50, -1 }, // GROUPIDENTREGISTRATION_INDEX = 33;
            { 0, 50, -1 }, // ISRC_INDEX = 34;
            { 0, 50, -1 }, // INITIALKEY_INDEX = 35;
            { 1, 50, -1 }, // INTERNETRADIOSTATIONNAME_INDEX = 36;
            { 1, 50, -1 }, // INTERNETRADIOSTATIONOWNER_INDEX = 37;
            { 1, 50, -1 }, // INTERNETRADIOWEBPAGE_INDEX = 38;
            { 1, 50, -1 }, // LANGUAGE_INDEX = 39;
            { 1, 50, -1 }, // LAYER_INDEX = 40;
            { 1, 50, -1 }, // LENGTHINTAG_INDEX = 41;
            { 0, 50, -1 }, // LOOKUPTABLE_INDEX = 42;
            { 1, 50, -1 }, // LYRICIST_INDEX = 43;
            { 1, 50, -1 }, // MPEGLEVEL_INDEX = 44;
            { 1, 50, -1 }, // MEDIATYPE_INDEX = 45;
            { 1, 50, -1 }, // MODE_INDEX = 46;
            { 0, 50, -1 }, // NAME_INDEX = 47; - name is not in use currently
            { 1, 50, -1 }, // OID_INDEX = 48;
            { 1, 50, -1 }, // ORIGINAL_INDEX = 49;
            { 1, 50, -1 }, // ORIGINALARTIST_INDEX = 50;
            { 0, 50, -1 }, // ORIGINALFILENAME_INDEX = 51;
            { 1, 50, -1 }, // ORIGINALLYRICIST_INDEX = 52;
            { 1, 50, -1 }, // ORIGINALTITLE_INDEX = 53;
            { 1, 50, -1 }, // ORIGINALYEAR_INDEX = 54;
            { 1, 50, -1 }, // OWNERSHIP_INDEX = 55;
            { 0, 50, -1 }, // PADDING_INDEX = 56;
            { 1, 50, -1 }, // PARTOFSET_INDEX = 57;
            { 1, 50, -1 }, // PAYMENTWEBPAGE_INDEX = 58;
            { 1, 50, -1 }, // PICTURE_INDEX = 59;
            { 1, 50, -1 }, // PLAYCOUNTER_INDEX = 60;
            { 0, 50, -1 }, // PLAYLISTDELAY_INDEX = 61;
            { 1, 50, -1 }, // PLAYLISTNR_INDEX = 62;
            { 1, 50, -1 }, // POPULARIMETER_INDEX = 63;
            { 0, 50, -1 }, // POSITIONSYNCHRONIZATION_INDEX = 64;
            { 1, 50, -1 }, // PRIVATE_INDEX = 65;
            { 0, 50, -1 }, // PRIVATEDATA_INDEX = 66;
            { 1, 50, -1 }, // PROTECTION_INDEX = 67;
            { 1, 50, -1 }, // PUBLISHER_INDEX = 68;
            { 1, 50, -1 }, // PUBLISHERSWEBPAGE_INDEX = 69;
            { 0, 50, -1 }, // RECOMMENDEDBUFFERSIZE_INDEX = 70;
            { 1, 50, -1 }, // RECORDINGDATES_INDEX = 71;
            { 1, 50, -1 }, // RELATIVEVOLUMEADJUSTMENT_INDEX = 72;
            { 0, 50, -1 }, // REMIXER_INDEX = 73;
            { 0, 50, -1 }, // REVERB_INDEX = 74;
            { 1, 50, -1 }, // SAMPLERATE_INDEX = 75;
            { 1, 50, -1 }, // SUBTITLE_INDEX = 76;
            { 1, 50, -1 }, // SYNCHRONIZEDLYRICS_INDEX = 77;
            { 0, 50, -1 }, // SYNCHRONIZEDTEMPOCODES_INDEX = 78;
            { 1, 50, -1 }, // TERMSOFUSE_INDEX = 79;
            { 0, 50, -1 }, // TIME_INDEX = 80;
            { 1, 150, -1 }, // TITLE_INDEX = 81;
            { 1, 25, -1 }, // TRACK_INDEX = 82;
            { 0, 50, -1 }, // UNIQUEFILEIDENTIFIER_INDEX = 83;
            { 1, 50, -1 }, // UNSYNCHRONIZEDLYRICS_INDEX = 84;
            { 0, 50, -1 }, // USECRC_INDEX = 85;
            { 0, 10, -1 }, // USECOMPRESSION_INDEX = 86;
            { 0, 10, -1 }, // USEPADDING_INDEX = 87;
            { 0, 10, -1 }, // USEUNSYNCHRONIZATION_INDEX = 88;
            { 1, 10, -1 }, // USERDEFINEDTEXT_INDEX = 89;
            { 1, 10, -1 }, // USERDEFINEDURL_INDEX = 90;
            { 0, 10, -1 }, // WRITEID3_INDEX = 91;
            { 0, 10, -1 }, // WRITEID3V2_INDEX = 92;
            { 1, 50, -1 }, // YEAR_INDEX = 93;
    };

    /**
     * Clones preferences object
     * 
     * @return clone
     */
    public Object clone() {
        // create new object
        Preferences copy = new Preferences();

        // copy content
        copy.iPodPath = this.iPodPath;
        copy.playerPath = this.playerPath;
        copy.addEnabled = this.addEnabled;
        copy.addParameter = this.addParameter;
        copy.appendEnabled = this.appendEnabled;
        copy.appendParameter = this.appendParameter;
        copy.listViewColumnInfo = this.listViewColumnInfo;
        copy.mainFramePosition = this.mainFramePosition;
        copy.dividerMainPosition = this.dividerMainPosition;
        copy.dividerArtistViewPosition = this.dividerArtistViewPosition;
        copy.dividerAlbumViewPosition = this.dividerAlbumViewPosition;
        copy.dividerGenreViewPosition = this.dividerGenreViewPosition;
        copy.lookAndFeelName = this.lookAndFeelName;
        copy.recentExtPlayerPath = this.recentExtPlayerPath;
        copy.recentIPodPath = this.recentIPodPath;
        copy.recentPlaylistPath = this.recentPlaylistPath;
        copy.recentScanPath = this.recentScanPath;
        copy.hideEmptyColumns = this.hideEmptyColumns;
        copy.optimizeColumnWidth = this.optimizeColumnWidth;

        return copy;
    }

    /**
     * Getter for property mainFramePosition.
     * 
     * @return Value of property mainFramePosition.
     */
    public Rectangle getMainFramePosition() {
        return mainFramePosition;
    }

    /**
     * Setter for property mainFramePosition.
     * 
     * @param mainFramePosition
     *            New value of property mainFramePosition.
     */
    public void setMainFramePosition(Rectangle mainFramePosition) {
        this.mainFramePosition = mainFramePosition;
    }

    /**
     * Getter for property iPodPath.
     * 
     * @return Value of property iPodPath.
     */
    public java.lang.String getIPodPath() {
        return iPodPath;
    }

    /**
     * Setter for property iPodPath.
     * 
     * @param iPodPath
     *            New value of property iPodPath.
     */
    public void setIPodPath(java.lang.String iPodPath) {
        this.iPodPath = iPodPath;
    }

    /**
     * Getter for property playerPath.
     * 
     * @return Value of property playerPath.
     */
    public java.lang.String getPlayerPath() {
        return playerPath;
    }

    /**
     * Setter for property playerPath.
     * 
     * @param playerPath
     *            New value of property playerPath.
     */
    public void setPlayerPath(java.lang.String playerPath) {
        this.playerPath = playerPath;
    }

    /**
     * Getter for property addEnabled.
     * 
     * @return Value of property addEnabled.
     */
    public boolean isAddEnabled() {
        return addEnabled;
    }

    /**
     * Setter for property addEnabled.
     * 
     * @param addEnabled
     *            New value of property addEnabled.
     */
    public void setAddEnabled(boolean addEnabled) {
        this.addEnabled = addEnabled;
    }

    /**
     * Getter for property addParameter.
     * 
     * @return Value of property addParameter.
     */
    public java.lang.String getAddParameter() {
        return addParameter;
    }

    /**
     * Setter for property addParameter.
     * 
     * @param addParameter
     *            New value of property addParameter.
     */
    public void setAddParameter(java.lang.String addParameter) {
        this.addParameter = addParameter;
    }

    /**
     * Getter for property appendEnabled.
     * 
     * @return Value of property appendEnabled.
     */
    public boolean isAppendEnabled() {
        return appendEnabled;
    }

    /**
     * Setter for property appendEnabled.
     * 
     * @param appendEnabled
     *            New value of property appendEnabled.
     */
    public void setAppendEnabled(boolean appendEnabled) {
        this.appendEnabled = appendEnabled;
    }

    /**
     * Getter for property appendParameter.
     * 
     * @return Value of property appendParameter.
     */
    public java.lang.String getAppendParameter() {
        return appendParameter;
    }

    /**
     * Setter for property appendParameter.
     * 
     * @param appendParameter
     *            New value of property appendParameter.
     */
    public void setAppendParameter(java.lang.String appendParameter) {
        this.appendParameter = appendParameter;
    }

    /**
     * Getter for property listViewColumnInfo.
     * 
     * @return Value of property listViewColumnInfo.
     */
    public int[][] getListViewColumnInfo() {
        return this.listViewColumnInfo;
    }

    /**
     * Setter for property listViewColumnInfo.
     * 
     * @param listViewColumnInfo
     *            New value of property listViewColumnInfo.
     */
    public void setListViewColumnInfo(int[][] listViewColumnInfo) {
        this.listViewColumnInfo = listViewColumnInfo;
    }

    /**
     * Getter for property recentScanPath.
     * 
     * @return Value of property recentScanPath.
     */
    public java.lang.String getRecentScanPath() {
        return recentScanPath;
    }

    /**
     * Setter for property recentScanPath.
     * 
     * @param recentScanPath
     *            New value of property recentScanPath.
     */
    public void setRecentScanPath(java.lang.String recentScanPath) {
        this.recentScanPath = recentScanPath;
    }

    /**
     * Getter for property recentPlaylistPath.
     * 
     * @return Value of property recentPlaylistPath.
     */
    public java.lang.String getRecentPlaylistPath() {
        return recentPlaylistPath;
    }

    /**
     * Setter for property recentPlaylistPath.
     * 
     * @param recentPlaylistPath
     *            New value of property recentPlaylistPath.
     */
    public void setRecentPlaylistPath(java.lang.String recentPlaylistPath) {
        this.recentPlaylistPath = recentPlaylistPath;
    }

    /**
     * Getter for property recentExtPlayerPath.
     * 
     * @return Value of property recentExtPlayerPath.
     */
    public java.lang.String getRecentExtPlayerPath() {
        return recentExtPlayerPath;
    }

    /**
     * Setter for property recentExtPlayerPath.
     * 
     * @param recentExtPlayerPath
     *            New value of property recentExtPlayerPath.
     */
    public void setRecentExtPlayerPath(java.lang.String recentExtPlayerPath) {
        this.recentExtPlayerPath = recentExtPlayerPath;
    }

    /**
     * Getter for property recentIPodPath.
     * 
     * @return Value of property recentIPodPath.
     */
    public java.lang.String getRecentIPodPath() {
        return recentIPodPath;
    }

    /**
     * Setter for property recentIPodPath.
     * 
     * @param recentIPodPath
     *            New value of property recentIPodPath.
     */
    public void setRecentIPodPath(java.lang.String recentIPodPath) {
        this.recentIPodPath = recentIPodPath;
    }

    /**
     * Getter for property lookAndFeelName.
     * 
     * @return Value of property lookAndFeelName.
     */
    public java.lang.String getLookAndFeelName() {
        return lookAndFeelName;
    }

    /**
     * Setter for property lookAndFeelName.
     * 
     * @param lookAndFeelName
     *            New value of property lookAndFeelName.
     */
    public void setLookAndFeelName(java.lang.String lookAndFeelName) {
        this.lookAndFeelName = lookAndFeelName;
    }

    /**
     * Getter for property dividerMainPosition.
     * 
     * @return Value of property dividerMainPosition.
     */
    public int getDividerMainPosition() {
        return dividerMainPosition;
    }

    /**
     * Setter for property dividerMainPosition.
     * 
     * @param dividerMainPosition
     *            New value of property dividerMainPosition.
     */
    public void setDividerMainPosition(int dividerMainPosition) {
        this.dividerMainPosition = dividerMainPosition;
    }

    /**
     * Getter for property dividerArtistViewPosition.
     * 
     * @return Value of property dividerArtistViewPosition.
     */
    public int getDividerArtistViewPosition() {
        return dividerArtistViewPosition;
    }

    /**
     * Setter for property dividerArtistViewPosition.
     * 
     * @param dividerArtistViewPosition
     *            New value of property dividerArtistViewPosition.
     */
    public void setDividerArtistViewPosition(int dividerArtistViewPosition) {
        this.dividerArtistViewPosition = dividerArtistViewPosition;
    }

    /**
     * Getter for property dividerAlbumViewPosition.
     * 
     * @return Value of property dividerAlbumViewPosition.
     */
    public int getDividerAlbumViewPosition() {
        return dividerAlbumViewPosition;
    }

    /**
     * Setter for property dividerAlbumViewPosition.
     * 
     * @param dividerAlbumViewPosition
     *            New value of property dividerAlbumViewPosition.
     */
    public void setDividerAlbumViewPosition(int dividerAlbumViewPosition) {
        this.dividerAlbumViewPosition = dividerAlbumViewPosition;
    }

    /**
     * Getter for property dividerGenreViewPosition.
     * 
     * @return Value of property dividerGenreViewPosition.
     */
    public int getDividerGenreViewPosition() {
        return dividerGenreViewPosition;
    }

    /**
     * Setter for property dividerGenreViewPosition.
     * 
     * @param dividerGenreViewPosition
     *            New value of property dividerGenreViewPosition.
     */
    public void setDividerGenreViewPosition(int dividerGenreViewPosition) {
        this.dividerGenreViewPosition = dividerGenreViewPosition;
    }

    /**
     * Getter for property optimizeColumnWidth.
     * 
     * @return Value of property optimizeColumnWidth.
     */
    public boolean isOptimizeColumnWidth() {
        return optimizeColumnWidth;
    }

    /**
     * Setter for property optimizeColumnWidth.
     * 
     * @param optimizeColumnWidth
     *            New value of property optimizeColumnWidth.
     */
    public void setOptimizeColumnWidth(boolean optimizeColumnWidth) {
        this.optimizeColumnWidth = optimizeColumnWidth;
    }

    /**
     * Getter for property hideEmptyColumns.
     * 
     * @return Value of property hideEmptyColumns.
     */
    public boolean isHideEmptyColumns() {
        return hideEmptyColumns;
    }

    /**
     * Setter for property hideEmptyColumns.
     * 
     * @param hideEmptyColumns
     *            New value of property hideEmptyColumns.
     */
    public void setHideEmptyColumns(boolean hideEmptyColumns) {
        this.hideEmptyColumns = hideEmptyColumns;
    }

}
