// AutoPlaylist
// $Id: AutoPlaylist.java,v 1.14 2003/07/05 14:39:51 axelwernicke Exp $
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

import java.util.Collection;
import java.util.Vector;

/**
 * A Paylist that contains music for specific artists or genres.
 * 
 * @author axel wernicke
 */
public class AutoPlaylist extends Playlist implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** list of artists in the autoplaylist */
    private Vector artistFilter;

    /** artist filter flag */
    private boolean artistFilterEnabled;

    /** list of genres in the autoplaylist */
    private Vector genreFilter;

    /** genre filter flag */
    private boolean genreFilterEnabled;

    /** list of years in the autoplaylist */
    private Vector yearFilter;

    /** year filter flag */
    private boolean yearFilterEnabled;

    /** list of albums in the autoplaylist */
    private Vector albumFilter;

    /** album filter flag */
    private boolean albumFilterEnabled;

    /** Default constructor for an autoplaylist */
    public AutoPlaylist() {
        this("");
    }

    /**
     * Creates a new instance of AutoPlaylist.
     * 
     * @param _name
     *            name for the new playlist
     */
    public AutoPlaylist(String _name) {
        super(_name);
        artistFilterEnabled = false;
        albumFilterEnabled = false;
        genreFilterEnabled = false;
        yearFilterEnabled = false;
        artistFilter = new Vector();
        albumFilter = new Vector();
        genreFilter = new Vector();
        yearFilter = new Vector();
    }

    /**
     * Sets an artist filter.
     * 
     * @param value
     *            list of artists to show in an autoplaylist
     */
    public void setArtistFilter(Vector value) {
        artistFilter = value;
    }

    /**
     * gets the artistfilter for the autoplaylist.
     * 
     * @return the artist filter for the autoplaylist
     */
    public Vector getArtistFilter() {
        return artistFilter;
    }

    /**
     * Sets a year filter
     * 
     * @param value
     *            list of artists to show in an autoplaylist
     */
    public void setYearFilter(Vector value) {
        yearFilter = value;
    }

    /**
     * gets the year filter for the autoplaylist.
     * 
     * @return the year filter for the autoplaylist
     */
    public Vector getYearFilter() {
        return yearFilter;
    }

    /**
     * Sets a genre filter
     * 
     * @param value
     *            list of genres to show in an autoplaylist
     */
    public void setGenreFilter(Vector value) {
        genreFilter = value;
    }

    /**
     * gets the artistfilter for the autoplaylist.
     * 
     * @return the artistfilter for the autoplaylist
     */
    public Vector getGenreFilter() {
        return genreFilter;
    }

    /**
     * Enables artist filter.
     * 
     * @param value
     *            true to enable artist filter
     */
    public void setArtistFilterEnabled(boolean value) {
        artistFilterEnabled = value;
    }

    /**
     * enables genre filter
     * 
     * @param value
     *            true to eaname genre filter
     */
    public void setGenreFilterEnabled(boolean value) {
        genreFilterEnabled = value;
    }

    /**
     * enables year filter
     * 
     * @param value
     *            true to enable year filter
     */
    public void setYearFilterEnabled(boolean value) {
        yearFilterEnabled = value;
    }

    /**
     * Checks if artist filter is enabled
     * 
     * @return Checks if artist filter is enabled
     */
    public boolean isArtistFilterEnabled() {
        return artistFilterEnabled;
    }

    /**
     * Checks if genre filter is enabled
     * 
     * @return Checks if genre filter is enabled
     */
    public boolean isGenreFilterEnabled() {
        return genreFilterEnabled;
    }

    /**
     * Checks if year filter is enabled
     * 
     * @return Checks if year filter is enabled
     */
    public boolean isYearFilterEnabled() {
        return yearFilterEnabled;
    }

    /**
     * Updates the playlists content
     * 
     * @param oids
     *            to update the playlist against
     * @param dataPool
     *            to get data from
     */
    protected void update(Collection oids, de.axelwernicke.mypod.DataPool dataPool) {
        MP3Meta clipMeta;
        Long clipOid;

        // find clips in the playlist that not apply to the filter anymore
        // work on a copy to avoid concurrent updates exception
        Vector tmp = this.getAllClips();
        java.util.Iterator iter = tmp.iterator();
        while (iter.hasNext()) {
            clipOid = (Long) iter.next();
            clipMeta = dataPool.getMeta(clipOid);

            if (clipMeta != null
                    && !((!artistFilterEnabled || artistFilter.contains(clipMeta.getArtist())) && (!albumFilterEnabled || albumFilter.contains(clipMeta.getAlbum()))
                            && (!genreFilterEnabled || genreFilter.contains(clipMeta.getGenre())) && (!yearFilterEnabled || yearFilter.contains(new Integer(clipMeta.getYear()))))) {
                this.remove(clipOid);
            }
        }

        // check if we got a vector to use this playlist as a filter for,
        // use all known clips if oids not set...
        iter = (oids != null) ? oids.iterator() : dataPool.getAllOid().iterator();

        // find all clips that apply to the filter and add them, if they aren't
        // in the list yet.
        // get all mp3 files filtered
        while (iter.hasNext()) {
            clipOid = (Long) iter.next();
            clipMeta = dataPool.getMeta(clipOid);

            if (clipMeta != null && !this.containsClip(clipOid) && (!artistFilterEnabled || artistFilter.contains(clipMeta.getArtist()))
                    && (!albumFilterEnabled || albumFilter.contains(clipMeta.getAlbum())) && (!genreFilterEnabled || genreFilter.contains(clipMeta.getGenre()))
                    && (!yearFilterEnabled || yearFilter.contains(new Integer(clipMeta.getYear())))) {
                // add clip
                this.addClip(clipOid);
            }
        }

        // update playlists total time and filesize
        this.validateTotalTime();
        this.validateTotalFilesize();
    }

    /**
     * Getter for property albumFilter.
     * 
     * @return Value of property albumFilter.
     */
    public Vector getAlbumFilter() {
        return albumFilter;
    }

    /**
     * Setter for property albumFilter.
     * 
     * @param albumFilter
     *            New value of property albumFilter.
     */
    public void setAlbumFilter(Vector albumFilter) {
        this.albumFilter = albumFilter;
    }

    /**
     * Getter for property albumFilterEnabled.
     * 
     * @return Value of property albumFilterEnabled.
     */
    public boolean isAlbumFilterEnabled() {
        return albumFilterEnabled;
    }

    /**
     * Setter for property albumFilterEnabled.
     * 
     * @param albumFilterEnabled
     *            New value of property albumFilterEnabled.
     */
    public void setAlbumFilterEnabled(boolean albumFilterEnabled) {
        this.albumFilterEnabled = albumFilterEnabled;
    }
}
