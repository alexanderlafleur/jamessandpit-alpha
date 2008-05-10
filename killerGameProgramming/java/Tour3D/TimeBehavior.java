package Tour3D;

// TimeBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Update the alien sprite every timeDelay ms by calling
 its update() method.
 This class is the Java 3D version of a Timer.
 */
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;

public class TimeBehavior extends Behavior {
    private AlienSprite alien;
    private WakeupCondition timeOut;

    public TimeBehavior(int timeDelay, AlienSprite as) {
        alien = as;
        timeOut = new WakeupOnElapsedTime(timeDelay);
    }

    @Override
    public void initialize() {
        wakeupOn(timeOut);
    }

    @Override
    public void processStimulus(Enumeration criteria) { // ignore criteria
        alien.update();
        wakeupOn(timeOut);
    } // end of processStimulus()
} // end of TimeBehavior class
