package SoundExamps.McDonalds;

// OldMcDonald.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Download and play an audio clip using Applet's 
 play()
 */
import java.applet.Applet;
import java.awt.Graphics;

public class OldMcDonald extends Applet {
    /**
     * 
     */
    private static final long serialVersionUID = -3996497424041366595L;

    @Override
    public void init() {
        play(getCodeBase(), "McDonald.mid");
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("Older McDonald", 25, 25);
    }
} // end of OldMcDonald.java
