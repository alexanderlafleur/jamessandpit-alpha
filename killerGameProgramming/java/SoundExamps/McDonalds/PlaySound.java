package SoundExamps.McDonalds;

// PlaySound.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/*
 Play a sound file using the java.applet APIs
 */

import java.applet.Applet;
import java.applet.AudioClip;

public class PlaySound {
    public PlaySound(String fnm) {
        try {
            AudioClip clip = Applet.newAudioClip(getClass().getResource(fnm));
            clip.play();
        } catch (Exception e) {
            System.out.println("Problem with " + fnm);
        }
    } // end of PlaySound()

    // ------------------------

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java PlaySound <sound file>");
            System.exit(0);
        }
        new PlaySound(args[0]);
    } // end of main()

} // end of PlaySound class
