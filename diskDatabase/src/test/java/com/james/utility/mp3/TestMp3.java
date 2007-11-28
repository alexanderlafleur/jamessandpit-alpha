package com.james.utility.mp3;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import de.vdheide.mp3.ID3v2DecompressionException;
import de.vdheide.mp3.ID3v2IllegalVersionException;
import de.vdheide.mp3.ID3v2WrongCRCException;
import de.vdheide.mp3.MP3File;
import de.vdheide.mp3.NoMP3FrameException;

public class TestMp3 extends TestCase {
	public void test() throws ID3v2WrongCRCException,
			ID3v2DecompressionException, ID3v2IllegalVersionException,
			IOException, NoMP3FrameException {
		File dir = new File("c:/itunes/Podcasts/JapanesePod101.com/");

		File files[] = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			try {
				new MP3File(files[i].getAbsolutePath());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
