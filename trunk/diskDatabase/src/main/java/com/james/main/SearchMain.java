package com.james.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.james.dao.hibernate.MP3SearchCriteria;
import com.james.helper.DirHelper;
import com.james.helper.DiskFileHelper;
import com.james.helper.DiskHelper;
import com.james.helper.MP3Helper;
import com.james.helper.WalletHelper;

public class SearchMain extends BaseMain {

	private static final int MAX_RESULTS = 100;

	public static void main(String args[]) throws IOException {
		SearchMain main = (SearchMain) CONTEXT.getBean("searchMain");

		while (true) {
			List wallets = main.searchForWallets();

			main.printWallets(wallets, false);

			List disks = main.searchForDisks();

			main.printDisks(disks);

			List dirs = main.searchForDirs();

			main.printDirs(dirs);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));

			String name = main.getInput("Enter file name to search for: ", in);
			String type = main.getInput("Enter type to search for: ", in);
			String path = main.getInput("Enter path to search for: ", in);
			Long low = main
					.getLongInput("Enter lower size to search for: ", in);
			Long high = main
					.getLongInput("Enter high size to search for: ", in);
			String album = main.getInput("Enter album to search for: ", in);
			String artist = main.getInput("Enter artist to search for: ", in);

			DiskFileSearchCriteria criteria = new DiskFileSearchCriteria(name,
					type, path, low, high);

			List files = main.searchForFiles(criteria);

			main.printFiles(files);

			MP3SearchCriteria mp3Criteria = new MP3SearchCriteria(name, type,
					low, high, album, artist);

			List mp3s = main.searchForMP3s(mp3Criteria);

			main.printMP3s(mp3s);
		}
	}

	private DirHelper dirHelper;

	private DiskFileHelper diskFileHelper;

	private DiskHelper diskHelper;

	private Log log = LogFactory.getLog(this.getClass());

	private MP3Helper mp3Helper;

	private WalletHelper walletHelper;

	public DirHelper getDirHelper() {
		return dirHelper;
	}

	public DiskFileHelper getDiskFileHelper() {
		return diskFileHelper;
	}

	public DiskHelper getDiskHelper() {
		return diskHelper;
	}

	public Log getLogger() {
		return log;
	}

	public MP3Helper getMp3Helper() {
		return mp3Helper;
	}

	public WalletHelper getWalletHelper() {
		return walletHelper;
	}

	private List searchForDirs() {
		return dirHelper.search("Chemical Brothers - Live At The Social");
	}

	private List searchForDisks() {
		return diskHelper.search("Disk");
	}

	private List searchForFiles(DiskFileSearchCriteria criteria) {
		return diskFileHelper.search(criteria, MAX_RESULTS);
	}

	private List searchForMP3s(MP3SearchCriteria criteria) {
		return mp3Helper.search(criteria, MAX_RESULTS);
	}

	private List searchForWallets() {
		return walletHelper.search("Wallet");
	}

	public void setDirHelper(DirHelper dirHelper) {
		this.dirHelper = dirHelper;
	}

	public void setDiskFileHelper(DiskFileHelper diskFileHelper) {
		this.diskFileHelper = diskFileHelper;
	}

	public void setDiskHelper(DiskHelper diskHelper) {
		this.diskHelper = diskHelper;
	}

	public void setMp3Helper(MP3Helper mp3Helper) {
		this.mp3Helper = mp3Helper;
	}

	public void setWalletHelper(WalletHelper walletHelper) {
		this.walletHelper = walletHelper;
	}
}