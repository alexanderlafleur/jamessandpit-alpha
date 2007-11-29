package com.james.jamesipod;

import java.util.Enumeration;
import java.util.Vector;

import de.axelwernicke.mypod.MP3Meta;
import de.axelwernicke.mypod.Preferences;
import de.axelwernicke.mypod.ipod.IPod;
import de.axelwernicke.mypod.ipod.ITunesDB;
import de.axelwernicke.mypod.ipod.ITunesDBContentItem;
import de.axelwernicke.mypod.ipod.ITunesDBListHolder;
import de.axelwernicke.mypod.ipod.ITunesDBSongItem;
import de.axelwernicke.mypod.ipod.ITunesDBSonglistHeader;

public class JamesIPODMain {
	public static final void main(String args[]) {
		JamesIPODMain ipod = new JamesIPODMain();

		// ipod.wipeIpod();
		ipod.createPlaylist();
		ipod.listContents();

		ipod.addFile();
	}

	private void addFile() {
		int nextSongIndex = db.getNextAvailableSongIndex(0);
		System.out.println("Next song index: " + nextSongIndex);

		MP3Meta meta = new MP3Meta();
		meta.setName("new song");
		
		db.addClip(nextSongIndex, meta);
		ipod.saveITunesDB(db);
	}

	private IPod ipod;
	private ITunesDB db;

	public JamesIPODMain() {
		ipod = new IPod();

		setup();

		db = ipod.getITunesDB();
	}

	private void createPlaylist() {
		Vector<Integer> fileIdc = new Vector<Integer>();
		db.createPlaylist("test", fileIdc);
	}

	private void setup() {
		Preferences ipodPrefs = new Preferences();
		ipodPrefs.setIPodPath("/media/JAMES'S IPO");
		ipod.setPreferences(ipodPrefs);
	}

	private void wipeIpod() {
		ipod.saveITunesDB(db);
		ipod.setITunesDB(db);
		ipod.saveITunesDB();
		ipod.wipe();
	}

	private void listContents() {
		ITunesDBListHolder songHolder = db.getSonglistHolder();
		ITunesDBListHolder playlistHolder = db.getPlaylistHolder();
		ITunesDBSonglistHeader songHeader = db.getSonglistHeader();

		printNumberOfSongs(songHeader);
		printPlaylists(playlistHolder);
		printSongs(db.getSonglistHeader(), db.getSonglistHolder());
	}

	private void printSongs(ITunesDBSonglistHeader songlistHeader,
			ITunesDBListHolder songHolder) {
		System.out.println("Number of songs: " + songlistHeader.getSongCount());
		System.out.println("Size of songholder: " + songHolder.getRecordSize());
		System.out.println("SpaceUsed: " + db.getRecordSize());

		for (int i = 0; i < songlistHeader.getSongCount(); i++) {
			ITunesDBSongItem song = songlistHeader.getSongItem(i);
			for (ITunesDBContentItem songItem : song.getContent()) {
				System.out.println("Content type: " + songItem.getContentTyp());
			}
		}
	}

	private void printPlaylists(ITunesDBListHolder playlistHolder) {
		System.out.println("Number of playlists: "
				+ playlistHolder.getRecordSize());
		//
		// for (int i = 0, size = playlistHolder.getRecordSize(); i < size; i++)
		// {
		// ITunesDBSongItem song = playlistHolder..getSongItem(i);
		//
		// for (Enumeration e = song.getContent().elements(); e
		// .hasMoreElements();) {
		// ITunesDBContentItem item = (ITunesDBContentItem) e
		// .nextElement();
		//
		// System.out.println(item.getContentAsString());
		// }
		// }
		//
		// System.out.println("SpaceUsed: " + db.getRecordSize());

	}

	private void printNumberOfSongs(ITunesDBSonglistHeader songHeader) {
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

// DataPool dataPool = new DataPool();
// Hashtable iMapper = new Hashtable();
// Vector oids = new Vector();
// oids.add(new Long(0));
// dataPool.
// IPodSyncDialog dialog = new IPodSyncDialog(null, null, false);
//
// ipod.addClips(dataPool, oids, iMapper, dialog);
