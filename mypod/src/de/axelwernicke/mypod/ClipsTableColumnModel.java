// ListViewColumnModel
// $Id: ClipsTableColumnModel.java,v 1.15 2003/07/20 06:46:16 axelwernicke Exp $
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

import java.awt.Component;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import de.axelwernicke.mypod.gui.GuiUtils;
import de.axelwernicke.mypod.util.ClipsTableUtils;

/**
 * Column model for myPods list view. This class extends the DefaultTableColumnModel class. Functionality to make sorting of columns persistent, as well as cell renderers are
 * added.
 * 
 * @author axel wernicke
 */
public class ClipsTableColumnModel extends javax.swing.table.DefaultTableColumnModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** jdk1.4 logger */
    private static Logger logger = Logger.getLogger("de.axelwernicke.mypod");

    /** emphasis strings */
    private static final String[] emphasisText = { " none", " 5015MS", "", " CCIT", "" };

    /** mode strings */
    private static final String[] modeText = { " Stereo", " Joint Stereo", " Dual Channel", " Mono" };

    /** mpeg level strings */
    private static final String[] levelText = { "", " MPEG 1", " MPEG 2" };

    /** mpeg layer strings */
    private static final String[] layerText = { "", " I", " II", " III" };

    /** date formatter */
    private static DateFormat df = DateFormat.getDateTimeInstance();

    private static Date date = new Date(0L);

    /** data pool object */
    private static DataPool dataPool = myPod.getDataPool();

    /**
     * Creates a new instance of ClipsTableColumnModel
     */
    public ClipsTableColumnModel() {
        super();
    }

    /**
     * Adds a column to a column model. The column is added if, and only if, it is enabled in the preferences object. Width of the column is set from the preferences too.
     * 
     * @param column
     *            to add to the column model
     */
    @Override
    public void addColumn(javax.swing.table.TableColumn column) {
        logger.entering("ClipsTableColumnModel", "addColumn()");

        // check if the column is enabled in the preferences
        int colIndex = column.getModelIndex();
        if (myPod.getBackend().getPreferences().getListViewColumnInfo()[colIndex][0] > 0) {
            // set column width from preferences
            column.setPreferredWidth(myPod.getBackend().getPreferences().getListViewColumnInfo()[colIndex][1]);

            // set cell renderer for this column
            column.setCellRenderer(determineCellRenderer(colIndex));

            // delegate the adding of the column to the default implementation
            super.addColumn(column);
        }

        logger.exiting("ClipsTableColumnModel", "addColumn()");
    }

    /**
     * Stores Column Preferences.
     */
    public void storeColumnPreferences() {
        // get the current list view info from preferences
        int colInfo[][] = myPod.getBackend().getPreferences().getListViewColumnInfo();

        // store column sort to preferences
        int columnCount = getColumnCount();
        for (int col = 0; col < columnCount; col++) {
            // debugging
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("stored column move for model column:" + getColumn(col).getModelIndex() + " from " + colInfo[getColumn(col).getModelIndex()][2] + " to " + col);
            }

            // store column order and width
            colInfo[getColumn(col).getModelIndex()][2] = col;
            colInfo[getColumn(col).getModelIndex()][1] = getColumn(col).getWidth();
        }
    }

    /**
     * Moves all columns to the position determined from the preferences.
     */
    public void restoreColumnPreferences() {
        logger.entering("ClipsTableColumnModel", "restoreColumnPreferences()");

        // get column info preferences
        int colInfo[][] = myPod.getBackend().getPreferences().getListViewColumnInfo();

        // restore column order
        int moveCnt = 0;
        boolean moved = false;
        int columnCount = getColumnCount();
        do {
            moved = false;

            for (int col = 0; col < columnCount - 1; col++) {
                int colDest = colInfo[getColumn(col).getModelIndex()][2];

                // check if there is something to move
                if (col != colDest && colDest != -1) {
                    moved = true;
                    moveCnt++;
                    if (col < colDest) {
                        // logger.finer("moving column: " + col + " fromDest " +
                        // (col+1));
                        moveColumn(col, col + 1);
                    } else {
                        // logger.finer("automove from: " + col + " fromDest " +
                        // (col-1));
                        moveColumn(col, col - 1);
                    }
                } // something to move
            }
        } while (moved && moveCnt < 10000);

        logger.fine(moveCnt + " moves needed");

        // restore column width
        for (int col = 0; col < columnCount; col++) {
            // set column width from preferences
            getColumn(col).setMinWidth(0);
            getColumn(col).setPreferredWidth(colInfo[getColumn(col).getModelIndex()][1]);
        }

        logger.exiting("ClipsTableColumnModel", "restoreColumnPreferences()");

    } // restoreColumnPreferences

    /**
     * Optimizes the width of a column by its content.
     * 
     * @param minWidth
     *            minimal column width
     * @param hideEmptyColumns
     *            hide empty columns
     * @param contentWidth
     *            array containing the width of the cells
     * @param columnIndex
     *            index of the column to optimize
     */
    public void optimizeColumnWidth(int[] contentWidth, int columnIndex, int minWidth, boolean hideEmptyColumns) {
        logger.entering("ClipsTableColumnModel", "optimizeColumnWidth()");

        // check arguments
        if (contentWidth.length <= 0 || columnIndex < 0) {
            return;
        }

        // sort width values
        java.util.Arrays.sort(contentWidth);

        // count how many items have a width of 0
        int nullCount = 0;
        while (nullCount < contentWidth.length && contentWidth[nullCount] == 0) {
            nullCount++;
        }

        // calculate optimized column width, based on cell size
        int skipItems = (contentWidth.length - nullCount) / 20;
        int width = contentWidth[contentWidth.length - skipItems - 1];
        width = (width > 0) ? width + 10 : ((hideEmptyColumns) ? 0 : 20);

        // check minimum - maximum
        width = ((0 < width) && (width < minWidth)) ? minWidth : width;
        width = (width > 400) ? 400 : width;

        // set column width
        getColumn(columnIndex).setPreferredWidth(width);

        logger.exiting("ClipsTableColumnModel", "optimizeColumnWidth()");
    }

    /**
     * Set cell renderers for each column
     * 
     * @param column
     *            to set a renderer
     * @return column renderer for the column
     */
    private TableCellRenderer determineCellRenderer(int column) {
        switch (column) {
        case ClipsTableUtils.BITRATE_INDEX:
            return new BitrateRenderer();
            // case ClipsTableUtils.CDIDENTIFIER_INDEX: return new
            // CDIdentifierRenderer();
        case ClipsTableUtils.LASTMODIFIED_INDEX:
        case ClipsTableUtils.OID_INDEX:
            return new DateRenderer();
        case ClipsTableUtils.DURATION_INDEX:
            return new DurationRenderer();
        case ClipsTableUtils.EMPHASIS_INDEX:
            return new EmphasisRenderer();
        case ClipsTableUtils.FILESIZE_INDEX:
            return new FilesizeRenderer();
            // case ClipsTableUtils.PLAYCOUNTER_INDEX: return new JTypeRenderer();
        case ClipsTableUtils.LAYER_INDEX:
            return new LayerRenderer();
        case ClipsTableUtils.MPEGLEVEL_INDEX:
            return new LevelRenderer();
        case ClipsTableUtils.MODE_INDEX:
            return new ModeRenderer();
        case ClipsTableUtils.PLAYLISTNR_INDEX:
            return new PlaylistIndexNumberRenderer();
        case ClipsTableUtils.SAMPLERATE_INDEX:
            return new SamplerateRenderer();
        case ClipsTableUtils.ARTISTWEBPAGE_INDEX:
        case ClipsTableUtils.AUDIOFILEWEBPAGE_INDEX:
        case ClipsTableUtils.AUDIOSOURCEWEBPAGE_INDEX:
        case ClipsTableUtils.COPYRIGHTWEBPAGE_INDEX:
        case ClipsTableUtils.INTERNETRADIOWEBPAGE_INDEX:
        case ClipsTableUtils.PUBLISHERSWEBPAGE_INDEX:
        case ClipsTableUtils.USERDEFINEDURL_INDEX:
            return new URLRenderer();

        default:
            return null; // if not set, a default renderer is used
        }
    } // set all cell renderer

    /**
     * Playlist clip index cell renderer
     */
    public static class PlaylistIndexNumberRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString() + ". ");

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(SwingConstants.TRAILING);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // PlaylistIndexNumberRenderer

    /**
     * date cell renderer
     */
    public static class DateRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            date.setTime(((Long) value).longValue());
            setText(" " + df.format(date));

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // DateRenderer

    /**
     * duration cell renderer
     */
    public static class DurationRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(GuiUtils.formatTime(((Long) value).longValue()) + " ");

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            setHorizontalAlignment(SwingConstants.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // Duration enderer

    /**
     * filesize cell renderer
     */
    public static class FilesizeRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(GuiUtils.formatFilesizeKB(((Long) value).longValue()) + " ");

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(SwingConstants.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // Filesize Renderer

    /**
     * emphasis cell renderer
     */
    public static class EmphasisRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                setText(emphasisText[((Integer) value).intValue()]);
            } catch (Exception e) {
                setText("");
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        @Override
        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // emphasis Renderer

    /**
     * mpeg mode cell renderer
     */
    public static class ModeRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                setText(modeText[((Integer) value).intValue()]);
            } catch (Exception e) {
                setText("");
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        @Override
        public void validate() {
            ;
        }

        @Override
        public void revalidate() {
            ;
        }

        @Override
        public void repaint(Rectangle r) {
            ;
        }

        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // mpeg mode Renderer */

    /** mpeg level cell renderer */
    public static class LevelRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         * @return
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                setText(levelText[((Integer) value).intValue()]);
            } catch (Exception e) {
                setText("");
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // mpeg mode Renderer

    /** mpeg mode cell renderer */
    public static class LayerRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            try {
                setText(layerText[((Integer) value).intValue()]);
            } catch (Exception e) {
                setText("");
            }

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // mpeg mode Renderer

    /** mpeg mode cell renderer */
    public static class SamplerateRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString() + " Hz ");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(JLabel.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // mpeg samplerate Renderer

    /** mpeg mode cell renderer */
    public static class BitrateRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // get vbr info
            boolean vbr = false;
            try {
                vbr = dataPool.getMeta((Long) ((ClipsTableModel) table.getModel()).getValueAt(row, ClipsTableUtils.OID_INDEX)).isVbr();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // set text
            setText(value.toString() + (vbr ? "[v] kbit " : " kbit "));

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(JLabel.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // mpeg biterate Renderer

    /** JType cell renderer */
    public static class JTypeRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("[" + value.getClass().getName() + "] " + value.toString());

            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(JLabel.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // JTypeRenderer Renderer

    /** Url cell renderer */
    public static class URLRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // TODO: may be one could make the links "usable" somehow...
            setText((value != null) ? value.toString() : "");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(JLabel.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // URL Renderer

    /** CD Identifier cell renderer */
    public static class CDIdentifierRenderer extends JLabel implements TableCellRenderer {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        /**
         * Here all the work is done.
         * 
         * @param table
         *            to render a cell for
         * @param value
         *            to render
         * @param isSelected
         *            flag
         * @param hasFocus
         *            flag
         * @param row
         *            of the cell to render
         * @param column
         *            of the cell to render
         * @return rendered component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // TODO: enhance
            setText((value != null) ? value.toString() : "");
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setHorizontalAlignment(JLabel.RIGHT);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);

            return this;
        }

        // overwrite a couple of JLabel classes with no-ops to enhance
        // performance
        public void validate() {
            ;
        }

        public void revalidate() {
            ;
        }

        public void repaint(Rectangle r) {
            ;
        }

        public void repaint(long tm, int x, int y, int width, int height) {
            ;
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
            ;
        }

        public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            ;
        }
    } // CD Identifier cell renderer

}