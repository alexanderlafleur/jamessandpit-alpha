package BugRunner;

// MidiInfo.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Hold a single midi sequence, and allow it to be played,
 stopped, paused, resumed, and made to loop.

 Looping is controlled by MidisLoader by calling tryLooping().

 MidisLoader passes a reference to its sequencer to each
 MidiInfo object, so that it can play its sequence.
 */

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiInfo {
    private final static String SOUND_DIR = "Sounds/";

    private String name, filename;

    private Sequence seq = null;

    private Sequencer sequencer; // passed in from MidisLoader

    private boolean isLooping = false;

    public MidiInfo(String nm, String fnm, Sequencer sqr) {
        this.name = nm;
        this.filename = SOUND_DIR + fnm;
        this.sequencer = sqr;
        loadMidi();
    } // end of MidiInfo()

    private void loadMidi()
    // load the Midi sequence
    {
        try {
            this.seq = MidiSystem.getSequence(getClass().getResource(this.filename));
        } catch (InvalidMidiDataException e) {
            System.out.println("Unreadable/unsupported midi file: " + this.filename);
        } catch (IOException e) {
            System.out.println("Could not read: " + this.filename);
        } catch (Exception e) {
            System.out.println("Problem with " + this.filename);
        }
    } // end of loadMidi()

    public void play(boolean toLoop) {
        if ((this.sequencer != null) && (this.seq != null)) {
            try {
                this.sequencer.setSequence(this.seq); // load MIDI sequence into the
                // sequencer
                this.sequencer.setTickPosition(0); // reset to the start
                this.isLooping = toLoop;
                this.sequencer.start(); // play it
            } catch (InvalidMidiDataException e) {
                System.out.println("Corrupted/invalid midi file: " + this.filename);
            }
        }
    } // end of play()

    public void stop()
    /*
     * Stop the sequence. We want this to trigger an 'end-of-track' meta message, so we stop the track by winding it to its end. The meta message will be sent to meta() in
     * MidisLoader, where the sequencer was created.
     */
    {
        if ((this.sequencer != null) && (this.seq != null)) {
            this.isLooping = false;
            if (!this.sequencer.isRunning()) {
                this.sequencer.start();
            }
            this.sequencer.setTickPosition(this.sequencer.getTickLength());
            // move to the end of the sequence to trigger an end-of-track msg
        }
    } // end of stop()

    public void pause()
    // pause the sequence by stopping the sequencer
    {
        if ((this.sequencer != null) && (this.seq != null)) {
            if (this.sequencer.isRunning()) {
                this.sequencer.stop();
            }
        }
    }

    public void resume() {
        if ((this.sequencer != null) && (this.seq != null)) {
            this.sequencer.start();
        }
    }

    public boolean tryLooping()
    /*
     * Loop the music if it's been set to be loopable, and report whether looping has occurred. Called by MidisLoader from meta() when it has received an 'end-of-track' meta
     * message.
     * 
     * In other words, the sequence is not set in 'looping mode' (which is possible with new methods in J2SE 1.5), but instead is made to play repeatedly by the MidisLoader.
     */
    {
        if ((this.sequencer != null) && (this.seq != null)) {
            if (this.sequencer.isRunning()) {
                this.sequencer.stop();
            }
            this.sequencer.setTickPosition(0);
            if (this.isLooping) { // play it again
                this.sequencer.start();
                return true;
            }
        }
        return false;
    } // end of tryLooping()

    // -------------- other access methods -------------------

    public String getName() {
        return this.name;
    }

} // end of MidiInfo class
