package FPShooter3D;

// AnimBeam.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

public class AnimBeam extends Thread {
    private LaserShot shot;

    public AnimBeam(LaserShot ls) {
        this.shot = ls;
    } // end of AnimBeam()

    @Override
    public void run() {
        this.shot.moveBeam();
    }

} // end of AnimBeam class
