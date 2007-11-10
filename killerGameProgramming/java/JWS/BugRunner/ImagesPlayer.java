package JWS.BugRunner;

// ImagesPlayer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* ImagesPLayer is aimed at displaying the sequence of images
 making up a 'n', 's', or 'g' image file, as loaded by
 ImagesLoader.

 The ImagesPlayer constructor is supplied with the
 intended duration for showing the entire sequence
 (seqDuration). This is used to calculate showPeriod,
 the amount of time each image should be shown before
 the next image is displayed.

 The animation period (animPeriod) input argument states
 how often the ImagesPlayer's updateTick() method will be
 called. The intention is that updateTick() will be called periodically
 from the update() method in the top-level animation framework.

 The current animation time is calculated when updateTick()
 is called, and used to calculate imPosition, imPosition
 specifies which image should be returned when getCurrentImage() 
 is called.

 The ImagesPlayer can be set to cycle, stop, resume, or restart
 at a given image position.

 When the sequence finishes, a callback, sequenceEnded(), can
 be invoked in a specified object implementing the 
 ImagesPlayerWatcher interface.

 */

import java.awt.image.BufferedImage;

public class ImagesPlayer {
    private String imName;

    private boolean isRepeating, ticksIgnored;

    private ImagesLoader imsLoader;

    private int animPeriod;

    // period used by animation loop (in ms)
    private long animTotalTime;

    private int showPeriod;

    // period the current image is shown (in ms)
    private double seqDuration;

    // total duration of the entire image sequence (in secs)

    private int numImages;

    private int imPosition; // position of current displayable image

    private ImagesPlayerWatcher watcher = null;

    public ImagesPlayer(String nm, int ap, double d, boolean isr, ImagesLoader il) {
        this.imName = nm;
        this.animPeriod = ap;
        this.seqDuration = d;
        this.isRepeating = isr;
        this.imsLoader = il;

        this.animTotalTime = 0L;

        if (this.seqDuration < 0.5) {
            System.out.println("Warning: minimum sequence duration is 0.5 sec.");
            this.seqDuration = 0.5;
        }

        if (!this.imsLoader.isLoaded(this.imName)) {
            System.out.println(this.imName + " is not known by the ImagesLoader");
            this.numImages = 0;
            this.imPosition = -1;
            this.ticksIgnored = true;
        } else {
            this.numImages = this.imsLoader.numImages(this.imName);
            this.imPosition = 0;
            this.ticksIgnored = false;
            this.showPeriod = (int) (1000 * this.seqDuration / this.numImages);
        }
    } // end of ImagesPlayer()

    public void updateTick()
    /* We assume that this method is called every animPeriod ms */
    {
        if (!this.ticksIgnored) {
            // update total animation time, modulo the animation sequence
            // duration
            this.animTotalTime = (this.animTotalTime + this.animPeriod) % (long) (1000 * this.seqDuration);

            // calculate current displayable image position
            this.imPosition = (int) (this.animTotalTime / this.showPeriod); // in range 0 to
            // num-1
            if ((this.imPosition == this.numImages - 1) && (!this.isRepeating)) { // at end of
                // sequence
                this.ticksIgnored = true; // stop at this image
                if (this.watcher != null) {
                    this.watcher.sequenceEnded(this.imName); // call callback
                }
            }
        }
    } // end of updateTick()

    public BufferedImage getCurrentImage() {
        if (this.numImages != 0) {
            return this.imsLoader.getImage(this.imName, this.imPosition);
        } else {
            return null;
        }
    } // end of getCurrentImage()

    public int getCurrentPosition() {
        return this.imPosition;
    }

    public void setWatcher(ImagesPlayerWatcher w) {
        this.watcher = w;
    }

    public void stop()
    /*
     * updateTick() calls will no longer update the total animation time or imPosition.
     */
    {
        this.ticksIgnored = true;
    }

    public boolean isStopped() {
        return this.ticksIgnored;
    }

    public boolean atSequenceEnd()
    // are we at the last image and not cycling through them?
    {
        return ((this.imPosition == this.numImages - 1) && (!this.isRepeating));
    }

    public void restartAt(int imPosn)
    /*
     * Start showing the images again, starting with image number imPosn. This requires a resetting of the animation time as well.
     */
    {
        if (this.numImages != 0) {
            if ((imPosn < 0) || (imPosn > this.numImages - 1)) {
                System.out.println("Out of range restart, starting at 0");
                imPosn = 0;
            }

            this.imPosition = imPosn;
            // calculate a suitable animation time
            this.animTotalTime = (long) this.imPosition * this.showPeriod;
            this.ticksIgnored = false;
        }
    } // end of restartAt()

    public void resume()
    // start at previous image position
    {
        if (this.numImages != 0) {
            this.ticksIgnored = false;
        }
    }

} // end of ImagesPlayer class
