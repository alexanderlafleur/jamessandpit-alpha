/*
 * PlayPlaylistAppendAction.java
 *
 * Created on 5. Juni 2003, 13:16
 */

package de.axelwernicke.mypod.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.axelwernicke.mypod.Playlist;
import de.axelwernicke.mypod.myPod;
import de.axelwernicke.mypod.gui.GuiUtils;

/**
 * @author axelwe
 */

/**
 * Play Playlist Add Action class
 */
public class PlayPlaylistAppendAction extends AbstractAction {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** jdk1.4 logger */
    private static Logger logger = Logger.getLogger("de.axelwernicke.mypod");

    /**
     * constructs an action object
     * 
     * @param mode
     *            gui object the action is for
     */
    public PlayPlaylistAppendAction(int mode) {
        putValue(Action.NAME, GuiUtils.getStringLocalized("playAppend"));
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/resource/Append16.gif")));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        putValue(Action.SHORT_DESCRIPTION, GuiUtils.getStringLocalized("playAppend"));
    }

    /**
     * executes the PlayerAction
     * 
     * @param evt
     *            event
     */
    public void actionPerformed(ActionEvent evt) {
        logger.entering(this.getClass().getName(), "actionPerformed");

        // lock frontend
        myPod.getFrontend().setLocked(true);

        try {
            // determine playlist
            Playlist playlist = myPod.getSelectedPlaylist();

            // call external player
            myPod.getBackend().playExtern(playlist, true);
        } catch (Exception e) {
            logger.warning("An action catched an exception : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // unlock frontend
            myPod.getFrontend().setLocked(false);
            System.gc();
        }

        logger.exiting(this.getClass().getName(), "actionPerformed");
    }
}
