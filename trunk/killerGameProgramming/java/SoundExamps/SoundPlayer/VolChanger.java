package SoundExamps.SoundPlayer;

// VolChanger.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* VolChanger is a volume changing thread, which operates on
 the synthesizer used by the MIDI player in FadeMidi.

 It gradually reduces the volume of the player (by accessing
 its synthesizer channel controls).
 */
public class VolChanger extends Thread {
    // the amount of time between changes to the volume, in ms
    private static int PERIOD = 500;
    private int numChanges = 0;
    private FadeMidi player;

    // the number of volume changes to be carried out
    public VolChanger(FadeMidi p) {
        super("VolChanger");
        player = p;
    }

    @Override
    public void run()
    /*
     * Modify the volume, sleep a while, then repeat, until the volume has been changed the required number of times.
     */
    {
        /*
         * calculate stepVolume, the amount to decrease the volume each time that the volume is changed.
         */
        int volume = player.getMaxVolume();
        int stepVolume = volume / numChanges;
        if (stepVolume == 0) {
            stepVolume = 1;
        }
        System.out.println("Max Volume: " + volume + ", step: " + stepVolume);
        int counter = 0;
        System.out.print("Fading");
        while (counter < numChanges) {
            try {
                volume -= stepVolume; // reduce the required volume level
                if (volume >= 0 && player != null) {
                    player.setVolume(volume); // change the volume
                }
                Thread.sleep(PERIOD); // delay a while
            } catch (InterruptedException e) {
            }
            System.out.print(".");
            counter++;
        }
        System.out.println();
    } // end of run()

    public void startChanging(int duration)
    /*
     * FadeMidi calls this method, supplying the duration of its MIDI sequence in ms.
     */
    {
        // calculate how many times the volume should be adjusted
        numChanges = duration / PERIOD;
        // System.out.println("No of changes: " + numChanges);
        start();
    } // end of startChanging()
} // end of VolChanger.java
