package FPShooter3D;

// AmmoManager.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* AmmoManager starts by loading a predetermined set of explosion images
 into exploIms[].

 It then creates NUMBEAMS LaserShot objects, and passes a reference
 to the array to each one. This means that the images are only loaded
 once, rather than NUMBEAMS times.

 AmmoManager also handles a fireBeam() call from KeyBehavior by firing 
 a beam if one is available, otherwise doing nothing.
 */
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.image.TextureLoader;

public class AmmoManager {
    private final static int NUMBEAMS = 20;
    private LaserShot[] shots; // for the beams

    public AmmoManager(TransformGroup steerTG, BranchGroup sceneBG, Vector3d targetVec) {
        // load the explosion images
        ImageComponent2D[] exploIms = loadImages("explo/explo", 6);
        shots = new LaserShot[NUMBEAMS];
        for (int i = 0; i < NUMBEAMS; i++) {
            shots[i] = new LaserShot(steerTG, exploIms, targetVec);
            // a laser shot represents a single beam and explosion
            sceneBG.addChild(shots[i].getTG());
        }
    } // end of AmmoManager()

    public void fireBeam()
    // fire an available beam (called from KeyBehavior)
    {
        for (int i = 0; i < NUMBEAMS; i++) {
            if (shots[i].requestFiring()) {
                // System.out.println("Fired shot " + i);
                return;
            }
        }
        // System.out.println("No free shots");
    } // end of fireBeam()

    private ImageComponent2D[] loadImages(String fNms, int numIms)
    /*
     * Load the explosion images: they all start with fNms, and there are numIms of them.
     */
    {
        String filename;
        TextureLoader loader;
        ImageComponent2D[] ims = new ImageComponent2D[numIms];
        System.out.println("Loading " + numIms + " textures from " + fNms);
        for (int i = 0; i < numIms; i++) {
            filename = new String(fNms + i + ".gif");
            loader = new TextureLoader(filename, null);
            ims[i] = loader.getImage();
            if (ims[i] == null) {
                System.out.println("Load failed for texture in : " + filename);
            }
        }
        return ims;
    } // end of loadImages()
} // end of AmmoManager class
