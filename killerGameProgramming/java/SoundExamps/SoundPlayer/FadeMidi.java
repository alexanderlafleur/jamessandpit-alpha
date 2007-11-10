package SoundExamps.SoundPlayer;

// FadeMidi.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Play a Midi sequence, allowing its volume to be adjusted
 while it is being played. In this example, the volume is
 gradually reduced to 0 during the course of the playing.

 The volume is manipulated by calls to the channel controls
 of the synthesizer, in particular to control 7 (VOLUME_CONTROLLER), 
 to control the volume setting of a channel.

 The volume manipulation is done by a separate VolChanger thread,
 set up in main(). A call to FadeMidi's startVolChanger() starts
 the volume changing.
 
 FadeMidi (and PanMidi) are extended versions of PlayMidi.

 Change 6th August 2004
 - added synthesizer.open() to initSequencer()

 Change 16th September 2004
 - got sequencer by explicitly requesting it in obtainSequencer()

 */

import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class FadeMidi implements MetaEventListener {
    // midi meta-event constant used to signal the end of a track
    private static final int END_OF_TRACK = 47;

    private static final int VOLUME_CONTROLLER = 7;

    private final static String SOUND_DIR = "Sounds/";

    private Sequencer sequencer;

    private Synthesizer synthesizer;

    private Sequence seq;

    private String filename;

    private DecimalFormat df;

    // holds the synthesizer's channels
    private MidiChannel[] channels;

    public FadeMidi(String fnm) {
        this.df = new DecimalFormat("0.#"); // 1 dp

        this.filename = SOUND_DIR + fnm;
        initSequencer();
        loadMidi(this.filename);
        play();

        /*
         * No need for sleeping to keep the object alive, since the VolChanger thread refers to it.
         */
    } // end of FadeMidi()

    private void initSequencer()
    /*
     * Set up the MIDI sequencer, the sequencer's meta-event listener, and its synthesizer.
     */
    {
        try {
            this.sequencer = obtainSequencer();

            if (this.sequencer == null) {
                System.out.println("Cannot get a sequencer");
                System.exit(0);
            }

            this.sequencer.open();
            this.sequencer.addMetaEventListener(this);

            // maybe the sequencer is not the same as the synthesizer
            // so link sequencer --> synth (this is required in J2SE 1.5)
            if (!(this.sequencer instanceof Synthesizer)) {
                System.out.println("Linking the MIDI sequencer and synthesizer");
                this.synthesizer = MidiSystem.getSynthesizer();
                this.synthesizer.open(); // new
                Receiver synthReceiver = this.synthesizer.getReceiver();
                Transmitter seqTransmitter = this.sequencer.getTransmitter();
                seqTransmitter.setReceiver(synthReceiver);
            } else {
                this.synthesizer = (Synthesizer) this.sequencer;
            }
        } catch (MidiUnavailableException e) {
            System.out.println("No sequencer available");
            System.exit(0);
        }
    } // end of initSequencer()

    private Sequencer obtainSequencer()
    /*
     * This method handles a bug in J2SE 1.5.0 which retrieves the sequencer with getSequencer() but does not allow its volume to be changed.
     */
    {
        // return MidiSystem.getSequencer();
        // okay in J2SE 1.4.2, but not in J2SE 1.5.0

        MidiDevice.Info[] mdi = MidiSystem.getMidiDeviceInfo();
        int seqPosn = -1;
        for (int i = 0; i < mdi.length; i++) {
            System.out.println(mdi[i].getName());
            // if (mdi[i].getName().contains("Sequencer")) {
            if (mdi[i].getName().indexOf("Sequencer") != -1) {
                seqPosn = i; // found the Sequencer
                System.out.println("  Found Sequencer");
            }
        }

        try {
            if (seqPosn != -1) {
                return (Sequencer) MidiSystem.getMidiDevice(mdi[seqPosn]);
            } else {
                return null;
            }
        } catch (MidiUnavailableException e) {
            return null;
        }
    } // end of obtainSequencer()

    private void loadMidi(String fnm) {
        this.seq = null;
        try {
            this.seq = MidiSystem.getSequence(getClass().getResource(fnm));
            double duration = ((double) this.seq.getMicrosecondLength()) / 1000000;
            System.out.println("Duration: " + this.df.format(duration) + " secs");
        } catch (InvalidMidiDataException e) {
            System.out.println("Unreadable/unsupported midi file: " + fnm);
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Could not read: " + fnm);
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Problem with " + fnm);
            System.exit(0);
        }
    } // end of loadMidi()

    private void play() {
        if ((this.sequencer != null) && (this.seq != null)) {
            try {
                this.sequencer.setSequence(this.seq); // load MIDI into sequencer
                this.sequencer.start(); // play it
                this.channels = this.synthesizer.getChannels();
                // showChannelVolumes();
            } catch (InvalidMidiDataException e) {
                System.out.println("Corrupted/invalid midi file: " + this.filename);
                System.exit(0);
            }
        }
    } // end of play()

    public void meta(MetaMessage event)
    /*
     * Meta-events trigger this method. The end-of-track meta-event signals that the sequence has finished
     */
    {
        if (event.getType() == END_OF_TRACK) {
            System.out.println("Exiting");
            close();
            System.exit(0); // necessary in J2SE 1.4.2 and earlier
        }
    } // end of meta()

    private void close()
    // Close down the sequencer and synthesizer
    {
        if (this.sequencer != null) {
            if (this.sequencer.isRunning()) {
                this.sequencer.stop();
            }

            this.sequencer.removeMetaEventListener(this);
            this.sequencer.close();

            if (this.synthesizer != null) {
                this.synthesizer.close();
            }
        }
    } // end of close()

    // ------------------- Volume Changer methods ------------------

    public void startVolChanger(VolChanger vc)
    /*
     * Start the volume changing thread running, and supply the duration of the MIDI sequence in ms. The duration is needed to estimate how often to change the volume.
     */
    {
        vc.startChanging((int) (this.seq.getMicrosecondLength() / 1000));
    }

    public int getMaxVolume()
    // return the max level for all the volume controllers
    {
        int maxVol = 0;
        int channelVol;
        for (MidiChannel element : this.channels) {
            channelVol = element.getController(VOLUME_CONTROLLER);
            if (maxVol < channelVol) {
                maxVol = channelVol;
            }
        }
        return maxVol;
    } // end of getVolume()

    public void setVolume(int vol)
    // set all the controller's volume levels to vol
    {
        for (MidiChannel element : this.channels) {
            element.controlChange(VOLUME_CONTROLLER, vol);
        }

        // showChannelVolumes();
    }

    // ----------------------------------------------

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FadeMidi <midi file>");
            System.exit(0);
        }
        // set up the player and the volume changer
        FadeMidi player = new FadeMidi(args[0]);
        VolChanger vc = new VolChanger(player);

        player.startVolChanger(vc); // start volume manipulation
    } // end of main()

} // end of FadeMidi class
