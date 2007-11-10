package NetFourByFour;

// OverlayCanvas.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
 The 3D Canvas includes a status line,
 displayed in red, at the top left corner.

 Current status information is obtained from
 the NetFourByFour object each time postSwap() is called.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;

import javax.media.j3d.Canvas3D;

public class OverlayCanvas extends Canvas3D {
    /**
     * 
     */
    private static final long serialVersionUID = 7813724622736122104L;

    private final static int XPOS = 5;

    private final static int YPOS = 15;

    private final static Font MSGFONT = new Font("SansSerif", Font.BOLD, 12);

    private NetFourByFour fbf;

    private String status;

    public OverlayCanvas(GraphicsConfiguration config, NetFourByFour fbf) {
        super(config);
        this.fbf = fbf;
    }

    @Override
    public void postSwap()
    /*
     * Called by the rendering loop after completing all rendering to the canvas.
     */
    {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setColor(Color.red);
        g.setFont(MSGFONT);

        if ((this.status = this.fbf.getStatus()) != null) {
            g.drawString(this.status, XPOS, YPOS);
        }

        // this call is made to compensate for the javaw repaint bug, ...
        Toolkit.getDefaultToolkit().sync();
    } // end of postSwap()

    @Override
    public void repaint()
    // Overriding repaint() makes the worst flickering disappear
    {
        Graphics2D g = (Graphics2D) getGraphics();
        paint(g);
    }

    @Override
    public void paint(Graphics g)
    // paint() is overridden to compensate for the javaw repaint bug
    {
        super.paint(g);
        Toolkit.getDefaultToolkit().sync();
    }

} // end of OverlayCanvas class

