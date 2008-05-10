package SoundExamps.McDonalds;

// McDonald.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Download an audio clip inside init(), and play it repeatedly.
 when start() is called. Mercifully, stop() will stop it when
 the page is no longer active in the browser.
 */
import java.applet.AudioClip;
import java.awt.Graphics;

import javax.swing.JApplet;

public class McDonald extends JApplet {
    /**
     * 
     */
    private static final long serialVersionUID = 8986096146024195542L;
    private AudioClip mcdClip;

    @Override
    public void init() {
        mcdClip = getAudioClip(getCodeBase(), "mcdonald.mid");
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("Old McDonald", 25, 25);
    }

    @Override
    public void start()
    /*
     * A looping play (and a call to play()) always starts at the beginning of the clip.
     */
    {
        mcdClip.loop();
    }

    @Override
    public void stop() {
        mcdClip.stop();
    }
} // end of McDonald.java
