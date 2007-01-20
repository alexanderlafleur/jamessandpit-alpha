/*
 * Created on Feb 8, 2005
 *
 */
package com.james.fileItems.util;

import com.james.util.ParseException;

import de.vdheide.mp3.FrameDamagedException;
import de.vdheide.mp3.MP3File;

/**
 * @author James
 */
public class MP3ParserImpl {

    private MP3File f;

    public MP3ParserImpl(String filename) {
        try {
            f = new MP3File(filename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new ParseException(e.getMessage());
        }
    }

    public String getAlbum() {
        try {
            return f.getAlbum().getTextContent();
        } catch (FrameDamagedException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String getArtist() {
        try {
            return f.getArtist().getTextContent();
        } catch (FrameDamagedException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String getTitle() {
        if (f == null) {
            return "Error";
        }
        try {
            String title;

            if (f.getOriginalTitle() == null) {
                title = f.getTitle().getTextContent();
            } else {
                title = f.getOriginalTitle().getTextContent();
            }

            return title;

        } catch (FrameDamagedException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public String getTrackNumber() {
        try {
            return f.getTrack().getTextContent();
        } catch (FrameDamagedException e) {
            return "0";
        }
    }
}