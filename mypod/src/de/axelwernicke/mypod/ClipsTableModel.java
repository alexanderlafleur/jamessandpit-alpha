// ListViewTableModel
// $Id: ClipsTableModel.java,v 1.11 2003/07/20 06:46:16 axelwernicke Exp $
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

import java.text.Collator;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;

import de.axelwernicke.mypod.util.ClipsTableUtils;

/**
 * Model for a list view table. The model is mostly a wrapper around a playlist. Additionally all the stuff to sort a table by columns is in here...
 * 
 * @author axel wernicke
 */
public class ClipsTableModel extends javax.swing.table.AbstractTableModel implements javax.swing.event.TableModelListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** jdk1.4 logger */
    private static Logger logger = Logger.getLogger("de.axelwernicke.mypod");

    /** the playlist currently assigned to this list view model */
    private Playlist playlist;

    /** index the table is sorted by */
    private int sortIdx[];

    /** counts the compares */
    int compares;

    /** holds the sorting column as Integer */
    private Vector sortingColumns = new Vector(3);

    /** sorting order */
    private boolean ascending = true;

    /** Locale sensitive string comparator */
    Collator collator = Collator.getInstance();

    /** Default Constructor */
    public ClipsTableModel() {
        this(null, null);
    }

    /**
     * Creates a new instance of ListViewModel and initializes with playlist.
     * 
     * @param _playlist
     *            Playlist to initialize table with.
     */
    public ClipsTableModel(Playlist _playlist) {
        this(_playlist, null);
    }

    /**
     * Creates a new instance of ListViewModel and initializes with playlist and sorting.
     * 
     * @param _sortingColumns
     *            columns to define sorting.
     * @param _playlist
     *            Playlist to initialize playlist from.
     */
    public ClipsTableModel(Playlist _playlist, Vector _sortingColumns) {
        // set new playlist
        this.playlist = (_playlist != null) ? _playlist : new Playlist();

        // set sorting
        this.sortingColumns = (_sortingColumns != null) ? _sortingColumns : new Vector(3);

        // reallocate index for column sorting
        reallocateIndexes();

        // kick sorting
        sort();
    }

    /**
     * Sets a playlist to the model.
     * 
     * @param value
     *            Playlist to set.
     */
    public void setPlaylist(Playlist value) {
        // set new playlist
        playlist = value;

        // reallocate index for column sorting
        resetSortedBy();
        reallocateIndexes();

        this.fireTableDataChanged();
    }

    /**
     * Gets the playlist behind the model.
     * 
     * @return Playlist behind the model.
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Gets the column count of the table.
     * 
     * @return column count.
     */
    public int getColumnCount() {
        return ClipsTableUtils.columnNames.length;
    }

    /**
     * gets the column name and adds a mark, if the table is sorted by this row
     * 
     * @param column
     *            index of the column
     * @return name of the column
     */
    @Override
    public String getColumnName(int column) {
        String name = ClipsTableUtils.columnNames[column];

        if ((sortingColumns.size() > 0) && (column == ((Integer) this.sortingColumns.get(0)).intValue())) {
            name = "¹" + name;
        }
        if ((sortingColumns.size() > 1) && (column == ((Integer) this.sortingColumns.get(1)).intValue())) {
            name = "²" + name;
        }
        if ((sortingColumns.size() > 2) && (column == ((Integer) this.sortingColumns.get(2)).intValue())) {
            name = "³" + name;
        }

        return name;
    }

    /**
     * gets the class of a column
     * 
     * @param column
     *            index of the column
     * @return class of the column
     */
    @Override
    public Class getColumnClass(int column) {
        switch (column) {
        // Integer:
        case ClipsTableUtils.BITRATE_INDEX:
        case ClipsTableUtils.EMPHASIS_INDEX:
        case ClipsTableUtils.LAYER_INDEX:
        case ClipsTableUtils.MPEGLEVEL_INDEX:
        case ClipsTableUtils.MODE_INDEX:
        case ClipsTableUtils.PLAYLISTNR_INDEX:
        case ClipsTableUtils.SAMPLERATE_INDEX:
        case ClipsTableUtils.TRACK_INDEX:
        case ClipsTableUtils.YEAR_INDEX:

            return java.lang.Integer.class;

            // Long:
        case ClipsTableUtils.OID_INDEX:
        case ClipsTableUtils.LASTMODIFIED_INDEX:
        case ClipsTableUtils.DURATION_INDEX:
        case ClipsTableUtils.FILESIZE_INDEX:

            return java.lang.Long.class;

            // Boolean:
        case ClipsTableUtils.COPYRIGHT_INDEX:
        case ClipsTableUtils.ORIGINAL_INDEX:
        case ClipsTableUtils.PADDING_INDEX:
        case ClipsTableUtils.PRIVATE_INDEX:
        case ClipsTableUtils.PROTECTION_INDEX:
        case ClipsTableUtils.USECRC_INDEX:
        case ClipsTableUtils.USECOMPRESSION_INDEX:
        case ClipsTableUtils.USEPADDING_INDEX:
        case ClipsTableUtils.USEUNSYNCHRONIZATION_INDEX:
        case ClipsTableUtils.WRITEID3_INDEX:
        case ClipsTableUtils.WRITEID3V2_INDEX:

            return java.lang.Boolean.class;

            // anything else:
        default:

            return java.lang.String.class;
        }
    }

    /**
     * gets the total count of rows in the model
     * 
     * @return total count of rows
     */
    public int getRowCount() {
        if (playlist == null) {
            return 0;
        }

        return playlist.getTotalClips();
    }

    /**
     * gets a value of the model
     * 
     * @param row
     *            specifies the row
     * @param col
     *            specifies the column
     * @return the object from the position (row, col)
     */
    public Object getValueAt(int row, int col) {
        return getUnsortedValueAt(sortIdx[row], col);
    }

    /**
     * gets a value of the model
     * 
     * @param row
     *            specifies the row
     * @param col
     *            specifies the column
     * @return the object from the position (row, col)
     */
    private Object getUnsortedValueAt(int row, int col) {
        MP3Meta mp3Meta = null;
        try {
            mp3Meta = myPod.getDataPool().getMeta(playlist.getClipAt(row));

            switch (col) {
            case ClipsTableUtils.ALBUM_INDEX:
                return mp3Meta.getAlbum();
            case ClipsTableUtils.ARTIST_INDEX:
                return mp3Meta.getArtist();
            case ClipsTableUtils.ARTISTWEBPAGE_INDEX:
                return mp3Meta.getArtistWebpage();
            case ClipsTableUtils.AUDIOFILEWEBPAGE_INDEX:
                return mp3Meta.getAudioFileWebpage();
            case ClipsTableUtils.AUDIOSOURCEWEBPAGE_INDEX:
                return mp3Meta.getAudioSourceWebpage();
            case ClipsTableUtils.BPM_INDEX:
                return mp3Meta.getBpm();
            case ClipsTableUtils.BAND_INDEX:
                return mp3Meta.getBand();
            case ClipsTableUtils.BITRATE_INDEX:
                return new Integer(mp3Meta.getBitrate());
            case ClipsTableUtils.CDIDENTIFIER_INDEX:
                return mp3Meta.getCdIdentifier();
            case ClipsTableUtils.COMMENT_INDEX:
                return mp3Meta.getComment();
            case ClipsTableUtils.COMMERCIAL_INDEX:
                return mp3Meta.getCommercial();
            case ClipsTableUtils.COMMERCIALINFO_INDEX:
                return mp3Meta.getCommercialInformation();
            case ClipsTableUtils.COMPOSER_INDEX:
                return mp3Meta.getComposer();
            case ClipsTableUtils.CONDUCTOR_INDEX:
                return mp3Meta.getConductor();
            case ClipsTableUtils.CONTENTGROUPSET_INDEX:
                return mp3Meta.getContentGroupset();
            case ClipsTableUtils.COPYRIGHT_INDEX:
                return new Boolean(mp3Meta.isCopyright());
            case ClipsTableUtils.COPYRIGHTTEXT_INDEX:
                return mp3Meta.getCopyrightText();
            case ClipsTableUtils.COPYRIGHTWEBPAGE_INDEX:
                return mp3Meta.getCopyrightWebpage();
            case ClipsTableUtils.DATE_INDEX:
                return mp3Meta.getDate();
            case ClipsTableUtils.DURATION_INDEX:
                return new Long(mp3Meta.getDuration());
            case ClipsTableUtils.EMPHASIS_INDEX:
                return new Integer(mp3Meta.getEmphasis());
            case ClipsTableUtils.ENCAPSULATEDOBJECT_INDEX:
                return mp3Meta.getEncapsulatedObject();
            case ClipsTableUtils.ENCODEDBY_INDEX:
                return mp3Meta.getEncodedBy();
            case ClipsTableUtils.ENCRYPTIONMETHODREGISTRATION_INDEX:
                return mp3Meta.getEncryptionMethodRegistration();
            case ClipsTableUtils.EQUALISATION_INDEX:
                return mp3Meta.getEqualisation();
            case ClipsTableUtils.EVENTTIMINGCODES_INDEX:
                return mp3Meta.getEventTimingCodes();
            case ClipsTableUtils.LASTMODIFIED_INDEX:
                return new Long(mp3Meta.getLastModified());
            case ClipsTableUtils.FILENAME_INDEX:
                return mp3Meta.getFilename();
            case ClipsTableUtils.FILEPATH_INDEX:
                return mp3Meta.getFilePath();
            case ClipsTableUtils.FILEOWNER_INDEX:
                return mp3Meta.getFileOwner();
            case ClipsTableUtils.FILETYPE_INDEX:
                return mp3Meta.getFileType();
            case ClipsTableUtils.FILESIZE_INDEX:
                return new Long(mp3Meta.getFilesize());
            case ClipsTableUtils.GENRE_INDEX:
                return mp3Meta.getGenre();
            case ClipsTableUtils.GROUPIDENTREGISTRATION_INDEX:
                return mp3Meta.getGroupIdentificationRegistration();
            case ClipsTableUtils.ISRC_INDEX:
                return mp3Meta.getIsrc();
            case ClipsTableUtils.INITIALKEY_INDEX:
                return mp3Meta.getInitialKey();
            case ClipsTableUtils.INTERNETRADIOSTATIONNAME_INDEX:
                return mp3Meta.getInternetRadioStationName();
            case ClipsTableUtils.INTERNETRADIOSTATIONOWNER_INDEX:
                return mp3Meta.getInternetRadioStationOwner();
            case ClipsTableUtils.INTERNETRADIOWEBPAGE_INDEX:
                return mp3Meta.getInternetRadioStationWebpage();
            case ClipsTableUtils.LANGUAGE_INDEX:
                return mp3Meta.getLanguage();
            case ClipsTableUtils.LAYER_INDEX:
                return new Integer(mp3Meta.getLayer());
            case ClipsTableUtils.LENGTHINTAG_INDEX:
                return mp3Meta.getLengthInTag();
            case ClipsTableUtils.LOOKUPTABLE_INDEX:
                return mp3Meta.getLookupTable();
            case ClipsTableUtils.LYRICIST_INDEX:
                return mp3Meta.getLyricist();
            case ClipsTableUtils.MPEGLEVEL_INDEX:
                return new Integer(mp3Meta.getMPEGLevel());
            case ClipsTableUtils.MEDIATYPE_INDEX:
                return mp3Meta.getMediaType();
            case ClipsTableUtils.MODE_INDEX:
                return new Integer(mp3Meta.getMode());
            case ClipsTableUtils.NAME_INDEX:
                return mp3Meta.getName();
            case ClipsTableUtils.OID_INDEX:
                return myPod.getDataPool().getOid(mp3Meta.getFilePath());
            case ClipsTableUtils.ORIGINAL_INDEX:
                return new Boolean(mp3Meta.isOriginal());
            case ClipsTableUtils.ORIGINALARTIST_INDEX:
                return mp3Meta.getOriginalArtist();
            case ClipsTableUtils.ORIGINALFILENAME_INDEX:
                return mp3Meta.getOriginalFilename();
            case ClipsTableUtils.ORIGINALLYRICIST_INDEX:
                return mp3Meta.getOriginalLyricist();
            case ClipsTableUtils.ORIGINALTITLE_INDEX:
                return mp3Meta.getOriginalTitle();
            case ClipsTableUtils.ORIGINALYEAR_INDEX:
                return mp3Meta.getOriginalYear();
            case ClipsTableUtils.OWNERSHIP_INDEX:
                return mp3Meta.getOwnership();
            case ClipsTableUtils.PADDING_INDEX:
                return new Boolean(mp3Meta.isPadding());
            case ClipsTableUtils.PARTOFSET_INDEX:
                return mp3Meta.getPartOfSet();
            case ClipsTableUtils.PAYMENTWEBPAGE_INDEX:
                return mp3Meta.getPaymentWebpage();
            case ClipsTableUtils.PICTURE_INDEX:
                return mp3Meta.getPicture();
            case ClipsTableUtils.PLAYCOUNTER_INDEX:
                return mp3Meta.getPlayCounter();
            case ClipsTableUtils.PLAYLISTNR_INDEX:
                return new Integer(row + 1);
            case ClipsTableUtils.PLAYLISTDELAY_INDEX:
                return mp3Meta.getPlaylistDelay();
            case ClipsTableUtils.POPULARIMETER_INDEX:
                return mp3Meta.getPopularimeter();
            case ClipsTableUtils.POSITIONSYNCHRONIZATION_INDEX:
                return mp3Meta.getPositionSynchronization();
            case ClipsTableUtils.PRIVATE_INDEX:
                return new Boolean(mp3Meta.isPrivat());
            case ClipsTableUtils.PRIVATEDATA_INDEX:
                return mp3Meta.getPrivateData();
            case ClipsTableUtils.PROTECTION_INDEX:
                return new Boolean(mp3Meta.isProtection());
            case ClipsTableUtils.PUBLISHER_INDEX:
                return mp3Meta.getPublisher();
            case ClipsTableUtils.PUBLISHERSWEBPAGE_INDEX:
                return mp3Meta.getPublishersWebpage();
            case ClipsTableUtils.RECOMMENDEDBUFFERSIZE_INDEX:
                return mp3Meta.getRecommendedBufferSize();
            case ClipsTableUtils.RECORDINGDATES_INDEX:
                return mp3Meta.getRecordingDates();
            case ClipsTableUtils.RELATIVEVOLUMEADJUSTMENT_INDEX:
                return mp3Meta.getRelativeVolumenAdjustment();
            case ClipsTableUtils.REMIXER_INDEX:
                return mp3Meta.getRemixer();
            case ClipsTableUtils.REVERB_INDEX:
                return mp3Meta.getReverb();
            case ClipsTableUtils.SAMPLERATE_INDEX:
                return new Integer(mp3Meta.getSamplerate());
            case ClipsTableUtils.SUBTITLE_INDEX:
                return mp3Meta.getSubtitle();
            case ClipsTableUtils.SYNCHRONIZEDLYRICS_INDEX:
                return mp3Meta.getSynchronizedLyrics();
            case ClipsTableUtils.SYNCHRONIZEDTEMPOCODES_INDEX:
                return mp3Meta.getSynchronizedTempoCodes();
            case ClipsTableUtils.TERMSOFUSE_INDEX:
                return mp3Meta.getTermsOfUse();
            case ClipsTableUtils.TIME_INDEX:
                return mp3Meta.getTime();
            case ClipsTableUtils.TITLE_INDEX:
                return mp3Meta.getTitle();
            case ClipsTableUtils.TRACK_INDEX:
                return new Integer(mp3Meta.getTrack());
            case ClipsTableUtils.UNIQUEFILEIDENTIFIER_INDEX:
                return mp3Meta.getUniqueFileIdentifier();
            case ClipsTableUtils.UNSYNCHRONIZEDLYRICS_INDEX:
                return mp3Meta.getUnsynchronizedLyrics();
            case ClipsTableUtils.USECRC_INDEX:
                return new Boolean(mp3Meta.isUseCRC());
            case ClipsTableUtils.USECOMPRESSION_INDEX:
                return new Boolean(mp3Meta.isUseCompression());
            case ClipsTableUtils.USEPADDING_INDEX:
                return new Boolean(mp3Meta.isUsePadding());
            case ClipsTableUtils.USEUNSYNCHRONIZATION_INDEX:
                return new Boolean(mp3Meta.isUseUnsynchronization());
            case ClipsTableUtils.USERDEFINEDTEXT_INDEX:
                return mp3Meta.getUserDefinedText();
            case ClipsTableUtils.USERDEFINEDURL_INDEX:
                return mp3Meta.getUserDefinedURL();
            case ClipsTableUtils.WRITEID3_INDEX:
                return new Boolean(mp3Meta.isWriteID3());
            case ClipsTableUtils.WRITEID3V2_INDEX:
                return new Boolean(mp3Meta.isWriteID3v2());
            case ClipsTableUtils.YEAR_INDEX:
                return new Integer(mp3Meta.getYear());

            default:
                return "";
            }
        } catch (Exception ex) {
            logger.warning("Exception raised: " + ex.getMessage());
            logger.warning("mp3Meta was: " + mp3Meta + " row: " + row + " col: " + col);
            ex.printStackTrace();
            return null;
        }
    }

    /** Resets the sorting. */
    void resetSortedBy() {
        sortingColumns.removeAllElements();
    }

    /**
     * Checks if the data in the model has the same count of rows as the sorting index array.
     * 
     * @return true, if sorter index an playlist have the sampe size
     */
    public boolean isValid() {
        return sortIdx.length == getRowCount();
    }

    /**
     * validates list view table model. check playlist.size() == sortIdx.length check playlist.totalClipsTime()
     */
    public void validate() {
        logger.entering("ListViewTableModel", "validate");

        if (!isValid()) {
            logger.info("table index not valid");

            // reallocate sort index
            reallocateIndexes();

            // restore sorting
            sort();
        }

        logger.exiting("ListViewTableModel", "validate");
    }

    /**
     * Inform everbody listening that the model changed.
     * 
     * @param tableModelEvent
     *            event to post
     */
    public void tableChanged(TableModelEvent tableModelEvent) {
        logger.entering("ListViewTableModel", "tableChanged");

        // revalidate the model if neccessary
        validate();

        // inform anybody
        fireTableChanged(tableModelEvent);

        logger.exiting("ListViewTableModel", "tableChanged");
    }

    // ----------------- sorting stuff -------------------------------

    /**
     * Reallocates sorting index.
     */
    public void reallocateIndexes() {
        logger.entering("ListViewTableModel", "reallocateIndexes");

        int rowCount = getRowCount();

        // Set up a new array of indexes with the right number of elements
        sortIdx = new int[rowCount];

        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            sortIdx[row] = row;
        }

        logger.exiting("ListViewTableModel", "reallocateIndexes");

    } // reallocate indexes

    /**
     * Compares two elements of a column.
     * 
     * @param row1
     *            to compare
     * @param row2
     *            to compare
     * @param column
     *            to compare
     * @return result to the comparison.
     */
    public int compareRowsByColumn(int row1, int row2, int column) {
        // get object type for the column
        Class type = getColumnClass(column);

        // get the objects to compare
        Object o1 = getUnsortedValueAt(row1, column);
        Object o2 = getUnsortedValueAt(row2, column);

        // If both values are null return 0
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } // Define null less than everything.
        else if (o2 == null) {
            return 1;
        }

        // We copy all returned values from the getValue call in case
        // an optimised model is reusing one object to return many values.
        // The Number subclasses in the JDK are immutable and so will not be
        // used in
        // this way but other subclasses of Number might want to do this to save
        // space and avoid unnecessary heap allocation.
        if (type.getSuperclass() == java.lang.Number.class) {
            double d1 = ((Number) o1).doubleValue();
            double d2 = ((Number) o2).doubleValue();

            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == Date.class) {
            long n1 = ((Date) o1).getTime();
            long n2 = ((Date) o2).getTime();

            if (n1 < n2) {
                return -1;
            } else if (n1 > n2) {
                return 1;
            } else {
                return 0;
            }
        } else if (type == String.class) {
            return collator.compare((String) o1, (String) o2);
        } else if (type == Boolean.class) {
            boolean b1 = ((Boolean) o1).booleanValue();
            boolean b2 = ((Boolean) o2).booleanValue();

            if (b1 == b2) {
                return 0;
            } else if (b1) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return collator.compare(o1.toString(), o2.toString());
        }
    }

    /**
     * Compares two values of a column to sort by
     * 
     * @param row1
     *            value1
     * @param row2
     *            value2
     * @return result
     */
    public int compare(int row1, int row2) {
        // logger.entering("ListViewTableModel", "compare");

        int result;
        compares++;

        // do it for all columns to compare
        int sortingColumnSize = sortingColumns.size();
        for (int level = 0; level < sortingColumnSize; level++) {
            result = compareRowsByColumn(row1, row2, ((Integer) sortingColumns.elementAt(level)).intValue());

            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        // logger.exiting("ListViewTableModel", "compare");
        return 0;
    }

    /**
     * Sort the model. <br- check the integrity of the model <br- kick (shuttle-)sorting <br- fire table changed event
     */
    public void sort() {
        // check integrity
        validate();

        // reset statistics
        long start = System.currentTimeMillis();
        compares = 0;

        // do sorting
        shuttlesort(sortIdx.clone(), sortIdx, 0, sortIdx.length);

        // log statistics
        double duration = System.currentTimeMillis() - start;
        logger.info("Compares: " + compares + " time: " + duration / 1000.0 + "s, equals to " + compares / (duration / 1000.0) + " compares per second.");

        // inform all about changes
        tableChanged(new TableModelEvent(this));

    } // sort

    // /** n2sorting algorithm - not in use
    // */
    // public void n2sort()
    // {
    // for(int i = 0; i < getRowCount(); i++)
    // {
    // for(int j = i+1; j < getRowCount(); j++)
    // {
    // if (compare(sortIdx[i], sortIdx[j]) == -1)
    // {
    // swap(i, j);
    // }
    // }
    // }
    // } // n2sort
    //	

    /**
     * This is a home-grown implementation which we have not had time to research - it may perform poorly in some circumstances. It requires twice the space of an in-place
     * algorithm and makes NlogN assigments shuttling the values between the two arrays. The number of compares appears to vary between N-1 and NlogN depending on the initial order
     * but the main reason for using it here is that, unlike qsort, it is stable.
     * 
     * @param from
     *            source array
     * @param to
     *            destination array
     * @param low
     *            index
     * @param high
     *            index
     */
    public void shuttlesort(int from[], int to[], int low, int high) {
        // there is nothing to do, break recursion
        if (high - low < 2) {
            return;
        }

        // split
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        // This is an optional short-cut; at each recursive call,
        // check to see if the elements in this subset are already
        // ordered. If so, no further comparisons are needed; the
        // sub-array can just be copied. The array must be copied rather
        // than assigned otherwise sister calls in the recursion might
        // get out of sinc. When the number of elements is three they
        // are partitioned so that the first set, [low, mid), has one
        // element and and the second, [mid, high), has two. We skip the
        // optimisation when the number of elements is three or less as
        // the first compare in the normal merge will produce the same
        // sequence of steps. This optimisation seems to be worthwhile
        // for partially ordered lists but some analysis is needed to
        // find out how the performance drops to Nlog(N) as the initial
        // order diminishes - it may drop very quickly.
        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge.
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    } // shuttlesort

    /**
     * Swaps two elements.
     * 
     * @param i
     *            element to swap.
     * @param j
     *            element to swap.
     */
    public void swap(int i, int j) {
        int tmp = sortIdx[i];
        sortIdx[i] = sortIdx[j];
        sortIdx[j] = tmp;
    }

    /**
     * Sorts a a table model by column ascending.
     * 
     * @param column
     *            to sort by
     */
    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    /**
     * Sorts a a table model by column.
     * 
     * @param column
     *            to sort by
     * @param ascending
     *            sorting oder
     */
    public void sortByColumn(int column, boolean ascending) {
        // set order
        this.ascending = ascending;

        // FIFO indices, but only if column changed
        if (((sortingColumns.size() > 0) && (column != ((Integer) sortingColumns.get(0)).intValue())) || (sortingColumns.size() == 0)) {
            sortingColumns.insertElementAt(new Integer(column), 0);
        }

        // we want max 3 sorted columns
        if (sortingColumns.size() > 3) {
            sortingColumns.removeElementAt(3);
        }

        // make sure that first and third sorting column are not the same
        if (sortingColumns.size() > 2 && ((Integer) sortingColumns.get(0)).intValue() == ((Integer) sortingColumns.get(2)).intValue()) {
            sortingColumns.removeElementAt(2);
        }

        // just do it
        sort();

    } // sort by column

    /**
     * Getter for property sortingColumns.
     * 
     * @return Value of property sortingColumns.
     */
    public Vector getSortingColumns() {
        return sortingColumns;
    }

    /**
     * Setter for property sortingColumns.
     * 
     * @param _sortingColumns
     *            vector containing the columns to sort the table by.
     */
    public void setSortingColumns(Vector _sortingColumns) {
        this.sortingColumns = _sortingColumns;

        // kick sorting
        sort();
    }

} // list view table model
