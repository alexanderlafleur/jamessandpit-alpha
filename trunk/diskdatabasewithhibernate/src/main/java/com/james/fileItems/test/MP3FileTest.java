/*
 * Created on Feb 8, 2005
 *
 */
package com.james.fileItems.test;

import java.io.IOException;

import junit.framework.TestCase;
import de.ueberdosis.mp3info.ID3Reader;

/**
 * @author James
 */
public class MP3FileTest extends TestCase {
    public void atestMP3() throws IOException {
        String mp3Filename = "E:\\Cypress Hill\\Live at the Fillmore\\01_Hand on the Pump.mp3";
        new ID3Reader(mp3Filename);
    }

}