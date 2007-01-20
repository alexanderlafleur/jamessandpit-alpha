package com.james.jamesipod;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import de.axelwernicke.mypod.DataPool;
import de.axelwernicke.mypod.Preferences;
import de.axelwernicke.mypod.gui.IPodSyncDialog;
import de.axelwernicke.mypod.ipod.IPod;
import de.axelwernicke.mypod.ipod.ITunesDB;
import de.axelwernicke.mypod.ipod.ITunesDBContentItem;
import de.axelwernicke.mypod.ipod.ITunesDBListHolder;
import de.axelwernicke.mypod.ipod.ITunesDBSongItem;
import de.axelwernicke.mypod.ipod.ITunesDBSonglistHeader;

public class JamesIPOD {

	public static final void main(String args[]) {
		JamesIPOD ipod = new JamesIPOD();

		ipod.listContents();
	}

	private IPod ipod;

	private ITunesDB db;

	public JamesIPOD() {
		ipod = new IPod();

		Preferences ipodPrefs = new Preferences();
		ipodPrefs.setIPodPath("/media/ipod");
		ipod.setPreferences(ipodPrefs);

		db = ipod.getITunesDB();

		Vector fileIdc = new Vector();

		db.createPlaylist("test", fileIdc);

		// DataPool dataPool = new DataPool();
		// Hashtable iMapper = new Hashtable();
		// Vector oids = new Vector();
		// oids.add(new Long(0));
		// dataPool.
		// IPodSyncDialog dialog = new IPodSyncDialog(null, null, false);
		//
		// ipod.addClips(dataPool, oids, iMapper, dialog);

		ipod.saveITunesDB(db);

		ipod.setITunesDB(db);
		ipod.saveITunesDB();
		
		// ipod.wipe();
	}

	private void listContents() {

		ITunesDBListHolder songHolder = db.getSonglistHolder();
		ITunesDBListHolder playlistHolder = db.getPlaylistHolder();
		ITunesDBSonglistHeader songHeader = db.getSonglistHeader();

		System.out.println("Number of songs: " + songHeader.getSongCount());

		for (int i = 0, size = songHeader.getSongCount(); i < size; i++) {
			ITunesDBSongItem song = songHeader.getSongItem(i);

			for (Enumeration e = song.getContent().elements(); e
					.hasMoreElements();) {
				ITunesDBContentItem item = (ITunesDBContentItem) e
						.nextElement();

				System.out.println(item.getContentAsString());
			}
		}

		System.out.println("SpaceUsed: " + db.getRecordSize());
	}
}
