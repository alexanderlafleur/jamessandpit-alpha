// ID3Utils
// $Id: ID3Utils.java,v 1.16 2003/07/20 06:46:17 axelwernicke Exp $
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

package de.axelwernicke.mypod.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.axelwernicke.mypod.MP3Meta;
import de.vdheide.mp3.FrameDamagedException;
import de.vdheide.mp3.MP3File;
import de.vdheide.mp3.TagContent;
import de.vdheide.mp3.TagFormatException;

/**
 * This class provides some helper methods to read and write id3 tags
 * 
 * @author axel wernicke
 */
public class ID3Utils {
    /** Holds an instance of this singleton designed class */
    private static ID3Utils INSTANCE = null;

    /** jdk1.4 logger */
    private static Logger logger = Logger.getLogger("de.axelwernicke.mypod.util");

    /**
     * Creates a new instance of ID3Utils. The constructor is private, cause this class is singleton. Please use getInstance() instead.
     */
    private ID3Utils() {
    }

    /**
     * Gets an instance of the ID3Utils class.
     * 
     * @return instance of ID3Utils class
     */
    public ID3Utils getInstance() {
        return (INSTANCE != null) ? INSTANCE : new ID3Utils();
    }

    /**
     * <BR>- file is opened as de.vdheide.MP3File
     * 
     * @param file
     *            to scan
     * @return mp3 meta data of the file
     */
    public static MP3Meta scanMp3VdHeide(java.io.File file) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "scanMp3VdHeide");

        if (logger.isLoggable(Level.FINER)) {
            logger.finer("now scanning: " + file.getName());
        }

        MP3Meta mp3Meta = null;

        try {
            MP3File mp3File = new MP3File(file.getPath());
            mp3Meta = scanMp3VdHeide(mp3File);
            mp3File = null;
        } catch (de.vdheide.mp3.ID3v2DecompressionException e) {
            logger.warning("ID3v2DecompressionException raised for file: " + file);
        } catch (de.vdheide.mp3.NoMP3FrameException e) {
            logger.warning("NoMP3FrameException raised for file: " + file);
        } catch (Exception e) {
            logger.warning("Exception raised: " + e.getMessage());
            e.printStackTrace();
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "scanMp3VdHeide");

        return mp3Meta;
    }

    /**
     * Scans an mp3 file for id3 tags. <BR>- extract id3 tags <BR>- return created MP3Meta object
     * 
     * @return mp3 meta data of the file
     * @param mp3File
     *            to read meta data from
     */
    public static MP3Meta scanMp3VdHeide(MP3File mp3File) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "scanMp3VdHeide");

        MP3Meta mp3Meta = new MP3Meta();

        String textValue;
        byte[] binValue;

        // copy information from mp3
        try {
            mp3Meta.setAlbum(mp3File.getAlbum().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getAlbum");
        }

        try {
            mp3Meta.setArtist(mp3File.getArtist().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getArtist");
        }

        try {
            mp3Meta.setArtistWebpage(mp3File.getArtistWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getArtistWebpage");
        }

        try {
            mp3Meta.setAudioFileWebpage(mp3File.getAudioFileWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getAudioFileWebpage");
        }

        try {
            mp3Meta.setAudioSourceWebpage(mp3File.getAudioSourceWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getAudioSourceWebpage");
        }

        try {
            mp3Meta.setBpm(mp3File.getBPM().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getBPM");
        }

        try {
            mp3Meta.setBand(mp3File.getBand().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getBand");
        }

        try {
            mp3Meta.setBitrate(mp3File.getBitrate());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getBitrate");
        }

        try {
            // TODO: check binary content conversion...
            binValue = mp3File.getCDIdentifier().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setCdIdentifier(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getCDIdentifier");
        }

        try {
            mp3Meta.setComment(mp3File.getComments().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getComments");
        }

        try {
            // TODO: check binary content conversion...
            binValue = mp3File.getCommercial().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setCommercial(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getCommercial");
        }

        try {
            mp3Meta.setCommercialInformation(mp3File.getCommercialInformation().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getCommercialInformation");
        }

        try {
            mp3Meta.setComposer(mp3File.getComposer().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getComposer");
        }

        try {
            mp3Meta.setConductor(mp3File.getConductor().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getConductor");
        }

        try {
            mp3Meta.setContentGroupset(mp3File.getContentGroup().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getContentGroup");
        }

        try {
            mp3Meta.setCopyright(mp3File.getCopyright());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getCopyright");
        }

        try {
            mp3Meta.setCopyrightText(mp3File.getCopyrightText().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getCopyrightText");
        }

        try {
            mp3Meta.setCopyrightWebpage(mp3File.getCopyrightWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getCopyrightWebpage");
        }

        try {
            mp3Meta.setDate(mp3File.getDate().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getDate");
        }

        try {
            mp3Meta.setEmphasis(mp3File.getEmphasis());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getEmphasis");
        }

        try {
            // TODO handle binary content
            binValue = mp3File.getEncapsulatedObject().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setEncapsulatedObject(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getEncapsulatedObject");
        }

        try {
            mp3Meta.setEncodedBy(mp3File.getEncodedBy().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getEncodedBy");
        }

        try {
            binValue = mp3File.getEncryptionMethodRegistration().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setEncryptionMethodRegistration(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getEncryptionMethodRegistration");
        }

        try {
            binValue = mp3File.getEqualisation().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setEqualisation(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getEqualisation");
        }

        try {
            binValue = mp3File.getEventTimingCodes().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setEventTimingCodes(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getEventTimingCodes");
        }

        try {
            mp3Meta.setFileOwner(mp3File.getFileOwner().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getFileOwner");
        }

        try {
            mp3Meta.setFileType(mp3File.getFileType().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getFileType");
        }

        try {
            mp3Meta.setGenre(MP3Meta.convertWinAmpGenre(mp3File.getGenre().getTextContent()));
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getGenre");
        }

        try {
            binValue = mp3File.getGroupIdentificationRegistration().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setGroupIdentificationRegistration(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getGroupIdentificationRegistration");
        }

        try {
            mp3Meta.setIsrc(mp3File.getISRC().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getISRC");
        }

        try {
            mp3Meta.setInitialKey(mp3File.getInitialKey().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getInitialKey");
        }

        try {
            mp3Meta.setInternetRadioStationName(mp3File.getInternetRadioStationName().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getInternetRadioStationName");
        }

        try {
            mp3Meta.setInternetRadioStationOwner(mp3File.getInternetRadioStationOwner().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getInternetRadioStationOwner");
        }

        try {
            mp3Meta.setInternetRadioStationWebpage(mp3File.getInternetRadioStationWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getInternetRadioStationWebpage");
        }

        try {
            mp3Meta.setLanguage(LanguageUtils.getLanguageName(mp3File.getLanguage().getTextContent()));
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLanguage");
        }

        try {
            mp3Meta.setLayer(mp3File.getLayer());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLayer");
        }

        try {
            mp3Meta.setDuration(getBestDuration(mp3File));
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLength");
        }

        try {
            mp3Meta.setLengthInTag(mp3File.getLengthInTag().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLengthInTag");
        }

        try {
            binValue = mp3File.getLookupTable().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setLookupTable(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLookupTable");
        }

        try {
            mp3Meta.setLyricist(mp3File.getLyricist().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getLyricist");
        }

        try {
            mp3Meta.setMPEGLevel(mp3File.getMPEGLevel());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getMPEGLevel");
        }

        try {
            mp3Meta.setMediaType(mp3File.getMediaType().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getMediaType");
        }

        try {
            mp3Meta.setMode(mp3File.getMode());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getMode");
        }

        try {
            mp3Meta.setOriginal(mp3File.getOriginal());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginal");
        }

        try {
            mp3Meta.setOriginalArtist(mp3File.getOriginalArtist().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginalArtist");
        }

        try {
            mp3Meta.setOriginalFilename(mp3File.getOriginalFilename().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginalFilename");
        }

        try {
            mp3Meta.setOriginalLyricist(mp3File.getOriginalLyricist().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginalLyricist");
        }

        try {
            mp3Meta.setOriginalTitle(mp3File.getOriginalTitle().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginalTitle");
        }

        try {
            mp3Meta.setOriginalYear(mp3File.getOriginalYear().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOriginalYear");
        }

        try {
            binValue = mp3File.getOwnership().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setOwnership(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getOwnership");
        }

        try {
            mp3Meta.setPadding(mp3File.getPadding());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPadding");
        }

        try {
            mp3Meta.setPartOfSet(mp3File.getPartOfSet().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPartOfSet");
        }

        try {
            mp3Meta.setPaymentWebpage(mp3File.getPaymentWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPaymentWebpage");
        }

        try {
            // TODO handle binary content
            binValue = mp3File.getPicture().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setPicture(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPicture");
        }

        try {
            binValue = mp3File.getPlayCounter().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setPlayCounter(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPlayCounter");
        }

        try {
            mp3Meta.setPlaylistDelay(mp3File.getPlaylistDelay().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPlaylistDelay");
        }

        try {
            binValue = mp3File.getPopularimeter().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setPopularimeter(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPopularimeter");
        }

        try {
            binValue = mp3File.getPositionSynchronization().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setPositionSynchronization(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPositionSynchronization");
        }

        try {
            mp3Meta.setPrivat(mp3File.getPrivate());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPrivate");
        }

        try {
            binValue = mp3File.getPrivateData().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setPrivateData(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPrivateData");
        }

        try {
            mp3Meta.setProtection(mp3File.getProtection());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getProtection");
        }

        try {
            mp3Meta.setPublisher(mp3File.getPublisher().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPublisher");
        }

        try {
            mp3Meta.setPublishersWebpage(mp3File.getPublishersWebpage().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getPublishersWebpage");
        }

        try {
            binValue = mp3File.getRecommendedBufferSize().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setRecommendedBufferSize(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getRecommendedBufferSize");
        }

        try {
            mp3Meta.setRecordingDates(mp3File.getRecordingDates().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getRecordingDates");
        }

        try {
            binValue = mp3File.getRelativeVolumenAdjustment().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setRelativeVolumenAdjustment(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getRelativeVolumenAdjustment");
        }

        try {
            mp3Meta.setRemixer(mp3File.getRemixer().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getRemixer");
        }

        try {
            binValue = mp3File.getReverb().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setReverb(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getReverb");
        }

        try {
            mp3Meta.setSamplerate(mp3File.getSamplerate());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getSamplerate");
        }

        try {
            mp3Meta.setSubtitle(mp3File.getSubtitle().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getSubtitle");
        }

        try {
            binValue = mp3File.getSynchronizedLyrics().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setSynchronizedLyrics(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getSynchronizedLyrics");
        }

        try {
            binValue = mp3File.getSynchronizedTempoCodes().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setSynchronizedTempoCodes(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getSynchronizedTempoCodes");
        }

        try {
            mp3Meta.setTermsOfUse(mp3File.getTermsOfUse().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getTermsOfUse");
        }

        try {
            mp3Meta.setTime(mp3File.getTime().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getTime");
        }

        try {
            mp3Meta.setTitle(mp3File.getTitle().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getTitle");
        }

        try {
            binValue = mp3File.getUniqueFileIdentifier().getBinaryContent();
            textValue = (binValue != null ? "vorh. (" + binValue.length + ")" : "");
            mp3Meta.setUniqueFileIdentifier(textValue);
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getUniqueFileIdentifier");
        }

        try {
            mp3Meta.setUnsynchronizedLyrics(mp3File.getUnsynchronizedLyrics().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getUnsynchronizedLyrics");
        }

        try {
            mp3Meta.setUseCRC(mp3File.getUseCRC());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getUseCRC");
        }

        try {
            mp3Meta.setUseCompression(mp3File.getUseCompression());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getUseCompression");
        }

        try {
            mp3Meta.setUsePadding(mp3File.getUsePadding());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getUsePadding");
        }

        try {
            mp3Meta.setUseUnsynchronization(mp3File.getUseUnsynchronization());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getUseUnsynchronization");
        }

        try {
            mp3Meta.setUserDefinedText(mp3File.getUserDefinedText().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getUserDefinedText");
        }

        try {
            mp3Meta.setUserDefinedURL(mp3File.getUserDefinedURL().getTextContent());
        } catch (FrameDamagedException fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in getUserDefinedURL");
        }

        try {
            mp3Meta.setVbr(mp3File.isVBR());
        } catch (Exception fde) {
            logger.info("FrameDamagedException raised for: " + mp3File.getName() + " in isVBR");
        }

        try {
            mp3Meta.setWriteID3(mp3File.getWriteID3());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getWriteID3");
        }

        try {
            mp3Meta.setWriteID3v2(mp3File.getWriteID3v2());
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getWriteID3v2");
        }

        mp3Meta.setFilename(mp3File.getName());
        mp3Meta.setFilePath(mp3File.getPath());
        mp3Meta.setFilesize(mp3File.length());
        mp3Meta.setLastModified(mp3File.lastModified());

        try {
            String track = mp3File.getTrack().getTextContent();
            if (track != null && !track.equals("")) {
                // sometimes we get a tracknumber like 3/15 - check this!
                if (track.indexOf("/") != -1) {
                    track = track.substring(0, track.indexOf("/"));
                }
                mp3Meta.setTrack(new Integer(track).intValue());
            }
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getTrack");
        }

        try {
            if (mp3File.getYear().getTextContent() != null && !mp3File.getYear().getTextContent().equals("")) {
                mp3Meta.setYear(Integer.valueOf(mp3File.getYear().getTextContent()).intValue());
            }
        } catch (Exception fde) {
            logger.info("Exception raised for: " + mp3File.getName() + " in getYear");
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "scanMp3VdHeide");

        return mp3Meta;
    }

    /**
     * Updates the id3 tags ( v1 & v2.3 ) of an mp3 clip
     * 
     * @param origMeta
     *            original meta data of the clip
     * @param metaToSet
     *            meta data to set, values are set if they are not null (strings) or -1 (numbers)
     */
    public static void updateMp3VdHeide(MP3Meta origMeta, MP3Meta metaToSet) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "updateMp3VdHeide");

        MP3File mp3File = null;
        TagContent tag = null;

        try {
            // open mp3 file and read tags - this is neccessary, cause only tags
            // read at least once, are written correctly
            mp3File = new MP3File(origMeta.getFilePath());
            prepareForUpdate(mp3File);

            try { // set new Album
                tag = createTag(origMeta.getAlbum(), metaToSet.getAlbum());
                if (tag != null) {
                    mp3File.setAlbum(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set album tag ");
            }

            try { // set new artist
                tag = createTag(origMeta.getArtist(), metaToSet.getArtist());
                if (tag != null) {
                    mp3File.setArtist(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set artist tag ");
            }

            try { // set new bpm
                tag = createTag(origMeta.getBpm(), metaToSet.getBpm());
                if (tag != null) {
                    mp3File.setBPM(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set bpm tag");
            }

            try { // set band
                tag = createTag(origMeta.getBand(), metaToSet.getBand());
                if (tag != null) {
                    mp3File.setBand(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set band tag ");
            }

            try { // set new cdIdentifier
                tag = createTag((origMeta.getCdIdentifier() != null) ? origMeta.getCdIdentifier().getBytes() : "".getBytes(), (metaToSet.getCdIdentifier() != null) ? metaToSet
                        .getCdIdentifier().getBytes() : "".getBytes());
                if (tag != null) {
                    mp3File.setCDIdentifier(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set CdIdentifier tag ");
            }

            try { // set new comment
                tag = createTag(origMeta.getComment(), metaToSet.getComment());
                if (tag != null) {
                    tag.setType("   "); // TODO set correct type ?!?
                    tag.setDescription("");
                    mp3File.setComments(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set comment tag ");
            }

            try { // set new composer
                tag = createTag(origMeta.getComposer(), metaToSet.getComposer());
                if (tag != null) {
                    mp3File.setComposer(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set composer tag ");
            }

            try { // set new content group set
                tag = createTag(origMeta.getContentGroupset(), metaToSet.getContentGroupset());
                if (tag != null) {
                    mp3File.setContentGroup(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set ContentGroupset tag ");
            }

            try { // set new getCopyright text
                tag = createTag(origMeta.getCopyrightText(), metaToSet.getCopyrightText());
                if (tag != null) {
                    mp3File.setCopyrightText(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set CopyrightText tag ");
            }

            try { // set new getCopyright Webpage
                tag = createTag(origMeta.getCopyrightWebpage(), metaToSet.getCopyrightWebpage());
                if (tag != null) {
                    mp3File.setCopyrightWebpage(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set CopyrightWebpage tag ");
            }

            try { // set new encoded by
                tag = createTag(origMeta.getEncodedBy(), metaToSet.getEncodedBy());
                if (tag != null) {
                    mp3File.setEncodedBy(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set EncodedBy tag ");
            }

            try { // set new genre
                tag = createTag(origMeta.getGenre(), metaToSet.getGenre());
                if (tag != null) {
                    mp3File.setGenre(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set genre tag ");
            }

            try { // set new isrc
                tag = createTag(origMeta.getIsrc(), metaToSet.getIsrc());
                if (tag != null) {
                    mp3File.setISRC(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set isrc tag ");
            }

            try { // set new language
                tag = createTag(LanguageUtils.getLanguageCode(origMeta.getLanguage()), LanguageUtils.getLanguageCode(metaToSet.getLanguage()));
                if (tag != null) {
                    mp3File.setLanguage(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set Language tag ");
            }

            try { // set new lyricist
                tag = createTag(origMeta.getLyricist(), metaToSet.getLyricist());
                if (tag != null) {
                    mp3File.setLyricist(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set lyricist tag ");
            }

            try { // set new original artist
                tag = createTag(origMeta.getOriginalArtist(), metaToSet.getOriginalArtist());
                if (tag != null) {
                    mp3File.setOriginalArtist(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set OriginalArtist tag ");
            }

            try { // set new original lyricist
                tag = createTag(origMeta.getOriginalLyricist(), metaToSet.getOriginalLyricist());
                if (tag != null) {
                    mp3File.setOriginalLyricist(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set OriginalLyricist tag ");
            }

            try { // set new original title
                tag = createTag(origMeta.getOriginalTitle(), metaToSet.getOriginalTitle());
                if (tag != null) {
                    mp3File.setOriginalTitle(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set OriginalTitle tag ");
            }

            try { // set new original year
                // TODO year must have a length of 4
                tag = createTag(origMeta.getOriginalYear(), metaToSet.getOriginalYear());
                if (tag != null) {
                    mp3File.setOriginalYear(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set OriginalYear tag ");
            }

            try { // set new playcounter
                tag = createTag((origMeta.getPlayCounter() != null) ? origMeta.getPlayCounter().getBytes() : "".getBytes(), (metaToSet.getPlayCounter() != null) ? metaToSet
                        .getPlayCounter().getBytes() : "".getBytes());
                if (tag != null) {
                    mp3File.setPlayCounter(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set PlayCounter tag ");
            }

            try { // set new publisher
                tag = createTag(origMeta.getPublisher(), metaToSet.getPublisher());
                if (tag != null) {
                    mp3File.setPublisher(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set Publisher tag ");
            }

            try { // set new publisher webpage
                tag = createTag(origMeta.getPublishersWebpage(), metaToSet.getPublishersWebpage());
                if (tag != null) {
                    mp3File.setPublishersWebpage(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set PublishersWebpage tag ");
            }

            try { // set new title
                tag = createTag(origMeta.getTitle(), metaToSet.getTitle());
                if (tag != null) {
                    mp3File.setTitle(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set title tag ");
            }

            try { // set new track
                tag = createTag(origMeta.getTrack(), metaToSet.getTrack());
                if (tag != null) {
                    mp3File.setTrack(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set track tag ");
            }

            try { // set new year
                tag = createTag(origMeta.getYear(), metaToSet.getYear());
                if (tag != null) {
                    mp3File.setYear(tag);
                }
            } catch (Exception e) {
                logger.warning(e.getClass().getName() + " raised while trying to set year tag ");
            }

            // update file
            // TODO removed checking if something changed - this should help to
            // hold id3v1 and id3v2 in sync
            mp3File.update();
        } catch (Exception e) {
            logger.warning("Exception raised for " + ((mp3File != null) ? mp3File.getName() : "unknown file (file == null)" + " : " + e.getMessage()));
            e.printStackTrace();
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "updateMp3VdHeide");
    }

    /**
     * Create a id3 text content tag from string <br>
     * The tag is created only, if new value is not null and different from the original one. <br>
     * null is returned otherwise
     * 
     * @param origValue
     *            old value
     * @param newValue
     *            new value
     * @return tag containing the new value
     */
    private static TagContent createTag(String origValue, String newValue) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        TagContent tag = null;

        if (newValue != null && !newValue.equals(origValue)) {
            tag = new TagContent();
            tag.setContent(newValue);
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        return tag;
    }

    /**
     * Create a id3 binary content tag from byte array <br>
     * The tag is created only, if new value is not null and different from the original one. <br>
     * null is returned otherwise
     * 
     * @param origValue
     *            old value
     * @param newValue
     *            new value
     * @return tag containing the new value
     */
    private static TagContent createTag(byte[] origValue, byte[] newValue) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        TagContent tag = null;

        if (newValue != null && !java.util.Arrays.equals(newValue, origValue)) {
            tag = new TagContent();
            tag.setContent(newValue);
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        return tag;
    }

    /**
     * Create a id3 binary content tag from int <br>
     * The tag is created only, if new value is not null and different from the original one. <br>
     * null is returned otherwise
     * 
     * @param origValue
     *            old value
     * @param newValue
     *            new value
     * @return tag containing the new value
     */
    private static TagContent createTag(int origValue, int newValue) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        TagContent tag = null;

        if (newValue != -1 && newValue != origValue) {
            tag = new TagContent();
            tag.setContent(String.valueOf(newValue));
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "createTag");

        return tag;
    }

    /**
     * Prepares an mp3 file to be updated. All tags are read once and id3v1 tags are written
     * 
     * @param mp3File
     *            to prepare
     */
    private static void prepareForUpdate(MP3File mp3File) {
        logger.entering("de.axelwernicke.mypod.util.ID3Utils", "prepareForUpdate");

        try {
            // read all tags once, otherwise id3v2 tags are not written
            // correctly
            scanMp3VdHeide(mp3File);

            // unsynchronization makes id3v2 unicode tags unreadable ...
            mp3File.setUseUnsynchronization(false);

            // id3v1 tags are written correctly only, if they are explicitly set
            // ....
            try {
                mp3File.setArtist(mp3File.getArtist());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setAlbum(mp3File.getAlbum());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setGenre(mp3File.getGenre());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setTitle(mp3File.getTitle());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setTrack(mp3File.getTrack());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setYear(mp3File.getYear());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
            try {
                mp3File.setComments(mp3File.getComments());
            } catch (TagFormatException e) {
                ;
            } catch (FrameDamagedException e) {
                ;
            }
        } catch (Exception e) {
            logger.warning("Exception raised: " + e.getMessage());
            e.printStackTrace();
        }

        logger.exiting("de.axelwernicke.mypod.util.ID3Utils", "prepareForUpdate");
    }

    /**
     */
    private static long getBestDuration(MP3File mp3File) {
        // get length from tag
        long lengthInTag = -1;
        try {
            lengthInTag = Long.parseLong(mp3File.getLengthInTag().getTextContent());
        } catch (Exception e) {
            // there can be various uncritical exception, so don't care
            ;
        }

        // return length from tag, if it seems to be valid, calculated length
        // (more sensible and inaccurate) otherwise
        return (lengthInTag > 0) ? (lengthInTag / 1000) : mp3File.getLength();
    }

}
