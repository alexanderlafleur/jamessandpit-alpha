// ListViewUtils
// $Id: ClipsTableUtils.java,v 1.5 2003/02/03 19:07:13 axelwernicke Exp $
//
// Copyright (C) 2002 Axel Wernicke <axel.wernicke@gmx.de>
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

package de.axelwernicke.mypod.util;

import de.axelwernicke.mypod.gui.GuiUtils;

/**
 * This class defines some helpers for the list view table. For each mp3 meta data an abstract name and a number are defined. Furthermore a name for each column of the list view
 * table is defined.
 * 
 * @author axel wernicke
 */
public class ClipsTableUtils {
    /**
     * list of static variables that declare which mp3meta information is assigned to which column of the list view table model
     */
    public static final int ALBUM_INDEX = 0;

    public static final int ARTIST_INDEX = 1;

    public static final int ARTISTWEBPAGE_INDEX = 2;

    public static final int AUDIOFILEWEBPAGE_INDEX = 3;

    public static final int AUDIOSOURCEWEBPAGE_INDEX = 4;

    public static final int BAND_INDEX = 5;

    public static final int BITRATE_INDEX = 6;

    public static final int BPM_INDEX = 7;

    public static final int CDIDENTIFIER_INDEX = 8;

    public static final int COMMENT_INDEX = 9;

    public static final int COMMERCIAL_INDEX = 10;

    public static final int COMMERCIALINFO_INDEX = 11;

    public static final int COMPOSER_INDEX = 12;

    public static final int CONDUCTOR_INDEX = 13;

    public static final int CONTENTGROUPSET_INDEX = 14;

    public static final int COPYRIGHT_INDEX = 15;

    public static final int COPYRIGHTTEXT_INDEX = 16;

    public static final int COPYRIGHTWEBPAGE_INDEX = 17;

    public static final int DATE_INDEX = 18;

    public static final int DURATION_INDEX = 19;

    public static final int EMPHASIS_INDEX = 20;

    public static final int ENCAPSULATEDOBJECT_INDEX = 21;

    public static final int ENCODEDBY_INDEX = 22;

    public static final int ENCRYPTIONMETHODREGISTRATION_INDEX = 23;

    public static final int EQUALISATION_INDEX = 24;

    public static final int EVENTTIMINGCODES_INDEX = 25;

    public static final int LASTMODIFIED_INDEX = 26;

    public static final int FILENAME_INDEX = 27;

    public static final int FILEOWNER_INDEX = 28;

    public static final int FILEPATH_INDEX = 29;

    public static final int FILESIZE_INDEX = 30;

    public static final int FILETYPE_INDEX = 31;

    public static final int GENRE_INDEX = 32;

    public static final int GROUPIDENTREGISTRATION_INDEX = 33;

    public static final int ISRC_INDEX = 34;

    public static final int INITIALKEY_INDEX = 35;

    public static final int INTERNETRADIOSTATIONNAME_INDEX = 36;

    public static final int INTERNETRADIOSTATIONOWNER_INDEX = 37;

    public static final int INTERNETRADIOWEBPAGE_INDEX = 38;

    public static final int LANGUAGE_INDEX = 39;

    public static final int LAYER_INDEX = 40;

    public static final int LENGTHINTAG_INDEX = 41;

    public static final int LOOKUPTABLE_INDEX = 42;

    public static final int LYRICIST_INDEX = 43;

    public static final int MPEGLEVEL_INDEX = 44;

    public static final int MEDIATYPE_INDEX = 45;

    public static final int MODE_INDEX = 46;

    public static final int NAME_INDEX = 47;

    public static final int OID_INDEX = 48;

    public static final int ORIGINAL_INDEX = 49;

    public static final int ORIGINALARTIST_INDEX = 50;

    public static final int ORIGINALFILENAME_INDEX = 51;

    public static final int ORIGINALLYRICIST_INDEX = 52;

    public static final int ORIGINALTITLE_INDEX = 53;

    public static final int ORIGINALYEAR_INDEX = 54;

    public static final int OWNERSHIP_INDEX = 55;

    public static final int PADDING_INDEX = 56;

    public static final int PARTOFSET_INDEX = 57;

    public static final int PAYMENTWEBPAGE_INDEX = 58;

    public static final int PICTURE_INDEX = 59;

    public static final int PLAYCOUNTER_INDEX = 60;

    public static final int PLAYLISTDELAY_INDEX = 61;

    public static final int PLAYLISTNR_INDEX = 62;

    public static final int POPULARIMETER_INDEX = 63;

    public static final int POSITIONSYNCHRONIZATION_INDEX = 64;

    public static final int PRIVATE_INDEX = 65;

    public static final int PRIVATEDATA_INDEX = 66;

    public static final int PROTECTION_INDEX = 67;

    public static final int PUBLISHER_INDEX = 68;

    public static final int PUBLISHERSWEBPAGE_INDEX = 69;

    public static final int RECOMMENDEDBUFFERSIZE_INDEX = 70;

    public static final int RECORDINGDATES_INDEX = 71;

    public static final int RELATIVEVOLUMEADJUSTMENT_INDEX = 72;

    public static final int REMIXER_INDEX = 73;

    public static final int REVERB_INDEX = 74;

    public static final int SAMPLERATE_INDEX = 75;

    public static final int SUBTITLE_INDEX = 76;

    public static final int SYNCHRONIZEDLYRICS_INDEX = 77;

    public static final int SYNCHRONIZEDTEMPOCODES_INDEX = 78;

    public static final int TERMSOFUSE_INDEX = 79;

    public static final int TIME_INDEX = 80;

    public static final int TITLE_INDEX = 81;

    public static final int TRACK_INDEX = 82;

    public static final int UNIQUEFILEIDENTIFIER_INDEX = 83;

    public static final int UNSYNCHRONIZEDLYRICS_INDEX = 84;

    public static final int USECRC_INDEX = 85;

    public static final int USECOMPRESSION_INDEX = 86;

    public static final int USEPADDING_INDEX = 87;

    public static final int USEUNSYNCHRONIZATION_INDEX = 88;

    public static final int USERDEFINEDTEXT_INDEX = 89;

    public static final int USERDEFINEDURL_INDEX = 90;

    public static final int WRITEID3_INDEX = 91;

    public static final int WRITEID3V2_INDEX = 92;

    public static final int YEAR_INDEX = 93;

    /** array containing the names of each column in the list view */
    public static final String columnNames[] = { GuiUtils.getStringLocalized("resource/language", "album"), GuiUtils.getStringLocalized("resource/language", "artist"),
            GuiUtils.getStringLocalized("resource/language", "artistwebpage"), GuiUtils.getStringLocalized("resource/language", "audioFileWebpage"),
            GuiUtils.getStringLocalized("resource/language", "audioSourceWebpage"), GuiUtils.getStringLocalized("resource/language", "band"),
            GuiUtils.getStringLocalized("resource/language", "bitrate"), GuiUtils.getStringLocalized("resource/language", "bpm"),
            GuiUtils.getStringLocalized("resource/language", "cdIdentifier"), GuiUtils.getStringLocalized("resource/language", "comment"),
            GuiUtils.getStringLocalized("resource/language", "commercial"), GuiUtils.getStringLocalized("resource/language", "commercialInfo"),
            GuiUtils.getStringLocalized("resource/language", "composer"), GuiUtils.getStringLocalized("resource/language", "conductor"),
            GuiUtils.getStringLocalized("resource/language", "contentGroupSet"), GuiUtils.getStringLocalized("resource/language", "copyright"),
            GuiUtils.getStringLocalized("resource/language", "copyrightText"), GuiUtils.getStringLocalized("resource/language", "copyrightWebpage"),
            GuiUtils.getStringLocalized("resource/language", "date"), GuiUtils.getStringLocalized("resource/language", "duration"),
            GuiUtils.getStringLocalized("resource/language", "emphasis"), GuiUtils.getStringLocalized("resource/language", "encapsulatedObject"),
            GuiUtils.getStringLocalized("resource/language", "encodedBy"), GuiUtils.getStringLocalized("resource/language", "encryptionMethodRegistration"),
            GuiUtils.getStringLocalized("resource/language", "equalisation"), GuiUtils.getStringLocalized("resource/language", "eventTimingCodes"),
            GuiUtils.getStringLocalized("resource/language", "lastModified"), GuiUtils.getStringLocalized("resource/language", "filename"),
            GuiUtils.getStringLocalized("resource/language", "fileOwner"), GuiUtils.getStringLocalized("resource/language", "filepath"),
            GuiUtils.getStringLocalized("resource/language", "filesize"), GuiUtils.getStringLocalized("resource/language", "fileType"),
            GuiUtils.getStringLocalized("resource/language", "genre"), GuiUtils.getStringLocalized("resource/language", "groupIdentRegistration"),
            GuiUtils.getStringLocalized("resource/language", "isrc"), GuiUtils.getStringLocalized("resource/language", "initialKey"),
            GuiUtils.getStringLocalized("resource/language", "internetRadioStationName"), GuiUtils.getStringLocalized("resource/language", "internetRadioStationOwner"),
            GuiUtils.getStringLocalized("resource/language", "internetRadioWebpage"), GuiUtils.getStringLocalized("resource/language", "language"),
            GuiUtils.getStringLocalized("resource/language", "layer"), GuiUtils.getStringLocalized("resource/language", "lengthInTag"),
            GuiUtils.getStringLocalized("resource/language", "lookupTable"), GuiUtils.getStringLocalized("resource/language", "lyricist"),
            GuiUtils.getStringLocalized("resource/language", "mpegLevel"), GuiUtils.getStringLocalized("resource/language", "mediaType"),
            GuiUtils.getStringLocalized("resource/language", "mode"), GuiUtils.getStringLocalized("resource/language", "name"),
            GuiUtils.getStringLocalized("resource/language", "oid"), GuiUtils.getStringLocalized("resource/language", "original"),
            GuiUtils.getStringLocalized("resource/language", "originalArtist"), GuiUtils.getStringLocalized("resource/language", "originalFileName"),
            GuiUtils.getStringLocalized("resource/language", "originalLyricist"), GuiUtils.getStringLocalized("resource/language", "originalTitle"),
            GuiUtils.getStringLocalized("resource/language", "originalYear"), GuiUtils.getStringLocalized("resource/language", "ownership"),
            GuiUtils.getStringLocalized("resource/language", "padding"), GuiUtils.getStringLocalized("resource/language", "partOffset"),
            GuiUtils.getStringLocalized("resource/language", "paymentWebpage"), GuiUtils.getStringLocalized("resource/language", "picture"),
            GuiUtils.getStringLocalized("resource/language", "playcounter"), GuiUtils.getStringLocalized("resource/language", "playlistDelay"),
            GuiUtils.getStringLocalized("resource/language", "playlistnumber"), GuiUtils.getStringLocalized("resource/language", "popularimeter"),
            GuiUtils.getStringLocalized("resource/language", "positionsynchronization"), GuiUtils.getStringLocalized("resource/language", "private"),
            GuiUtils.getStringLocalized("resource/language", "privateData"), GuiUtils.getStringLocalized("resource/language", "protection"),
            GuiUtils.getStringLocalized("resource/language", "publisher"), GuiUtils.getStringLocalized("resource/language", "publisherWebpage"),
            GuiUtils.getStringLocalized("resource/language", "recommendedBufferSize"), GuiUtils.getStringLocalized("resource/language", "recordingDate"),
            GuiUtils.getStringLocalized("resource/language", "relativeVolume"), GuiUtils.getStringLocalized("resource/language", "remixer"),
            GuiUtils.getStringLocalized("resource/language", "reverb"), GuiUtils.getStringLocalized("resource/language", "sampleRate"),
            GuiUtils.getStringLocalized("resource/language", "subtitle"), GuiUtils.getStringLocalized("resource/language", "synchronizedLyrics"),
            GuiUtils.getStringLocalized("resource/language", "synchronizedTempoCodes"), GuiUtils.getStringLocalized("resource/language", "termsOfUse"),
            GuiUtils.getStringLocalized("resource/language", "time"), GuiUtils.getStringLocalized("resource/language", "title"),
            GuiUtils.getStringLocalized("resource/language", "track"), GuiUtils.getStringLocalized("resource/language", "uniqueFileIdentifier"),
            GuiUtils.getStringLocalized("resource/language", "unsynchronizedLyrics"), GuiUtils.getStringLocalized("resource/language", "useCRC"),
            GuiUtils.getStringLocalized("resource/language", "useCompression"), GuiUtils.getStringLocalized("resource/language", "usePadding"),
            GuiUtils.getStringLocalized("resource/language", "useSynchronization"), GuiUtils.getStringLocalized("resource/language", "userdefinedText"),
            GuiUtils.getStringLocalized("resource/language", "userdefinedURL"), GuiUtils.getStringLocalized("resource/language", "writeID3"),
            GuiUtils.getStringLocalized("resource/language", "writeID3v2"), GuiUtils.getStringLocalized("resource/language", "year"), };
}
