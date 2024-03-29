package SoundExamps.SynthSound;

// SeqSynth.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Construct a sequence of MidiEvents containing NOTE_ON/NOTE_OFF
 messages for playing notes, and PROGRAM_CHANGE, CONTROL_CHANGE
 messages for changing the instruments. The speed of playing
 can be specified.

 Notes can be expressed as integers or using note names
 (e.g. F4#). See http://www.phys.unsw.edu.au/~jw/notes.html
 for a chart linking the two.

 This example shows how MIDI sequences can be generated at 
 run time from within  code. It also shows how sequences
 can be constructed using simple musical notation.

 Change 6th August 2004
 - added synthesizer.open() to createSequencer()
 */
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Patch;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

public class SeqSynth implements MetaEventListener {
    // channel controller name for changing an instrument bank
    private static final int BANK_CONTROLLER = 0;
    // A B C D E F G
    private static final int C4_KEY = 60;
    // channel used by the sequence
    private static final int CHANNEL = 0;
    /*
     * The note offsets use the "C" major scale, which has the order "C D E F G A B", but the offsets are stored in the order "A B C D E F G" to
     * simplify their lookup.
     */
    private static final int[] cOffsets = { 9, 11, 0, 2, 4, 5, 7 };
    // midi meta-event constant used to signal the end of a track
    private static final int END_OF_TRACK = 47;
    // C4 is the "C" in the 4th octave on a piano
    private static final int OCTAVE = 12; // note size of an octave
    private static final int VOLUME = 90; // fixed volume for notes

    public static void main(String[] args) {
        new SeqSynth();
        System.exit(0);
    }

    // build a sequence with a single track of MidiEvent messages
    private Sequence sequence;
    private Sequencer sequencer;
    private Synthesizer synthesizer;
    private int tickPos = 0; // used to time-stamp the MidiEvent messages
    private Track track;

    public SeqSynth() {
        createSequencer();
        // listInstruments();
        createTrack(4); // 4 is the PPQ resolution
        // makeScale(21); // the key is "A0"
        makeSong();
        startSequencer(60); // tempo: 60 beats/min
        // wait for the sound sequence to finish playing
        try {
            Thread.sleep(600000); // 10 mins in ms
        } catch (InterruptedException e) {
            System.out.println("Sleep Interrupted");
        }
        System.exit(0);
    } // end of SeqSynth()

    private void add(int note, int period)
    /*
     * Adding a note is really the addition of two MidiEvent messages to the track: a NOTE_ON to start the note playing, and a NOTE_OFF to stop it.
     * The playing length is determined by the time interval (period) separating the time-stamps in the two messages.
     */
    {
        setMessage(ShortMessage.NOTE_ON, note, tickPos);
        tickPos += period;
        setMessage(ShortMessage.NOTE_OFF, note, tickPos);
    }

    // ----------------- examples of sequence creation ----------------
    private void add(String noteStr) {
        add(noteStr, 1);
    }

    // ------------ add methods ----------------------
    private void add(String noteStr, int period)
    // convert the note string to a numerical note, then add it
    {
        int note = getKey(noteStr);
        add(note, period);
    }

    // private void add(int note) {
    // add(note, 1);
    // }
    private void addRest(int period)
    // this will leave a period of no notes (i.e. silence) in the track
    {
        tickPos += period;
    }

    private void bankChange(int bank)
    /*
     * Change the bank setting by adding a CONTROL_CHANGE message in the track.
     */
    {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.CONTROL_CHANGE, CHANNEL, BANK_CONTROLLER, bank);
            MidiEvent event = new MidiEvent(message, tickPos);
            track.add(event);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    } // end of bankChange()

    private void changeInstrument(int bank, int program)
    /*
     * An instrument is defined by its 'bank' and 'program' integers. If the supplied bank and program match an actual instrument, then update the
     * synthesizer's instrument settings.
     */
    {
        Instrument[] instrument = synthesizer.getAvailableInstruments();
        for (Instrument element : instrument) {
            Patch p = element.getPatch();
            if (bank == p.getBank() && program == p.getProgram()) {
                // System.out.println("(" + instrument[i].getName() +
                // " <" + p.getBank() + "," + p.getProgram() + ">) ");
                programChange(program);
                bankChange(bank);
                return;
            }
        }
        System.out.println("No instrument of type <" + bank + "," + program + ">");
    } // end of changeInstrument()

    // --------------------------------------------------
    private void createSequencer()
    // create sequencer _and_ access its synthesizer
    {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            // maybe the sequencer is not the same as the synthesizer
            // so make a link between them (e.g. required in J2SE 1.5)
            if (!(sequencer instanceof Synthesizer)) {
                System.out.println("Linking the MIDI sequencer and synthesizer");
                synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open(); // new
                Receiver synthReceiver = synthesizer.getReceiver();
                Transmitter seqTransmitter = sequencer.getTransmitter();
                seqTransmitter.setReceiver(synthReceiver);
            } else {
                synthesizer = (Synthesizer) sequencer;
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    } // end of createSequencer()

    private void createTrack(int resolution)
    /*
     * The sequence uses PPQ (pulses per quarter (ticks per beat) The actual number of pulses is defined using resolution. The higher the resolution,
     * the faster (greater tempo) to the music. An empty track is added to the sequence.
     */
    {
        try {
            sequence = new Sequence(Sequence.PPQ, resolution);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        track = sequence.createTrack();
    } // end of createTrack()

    private int getKey(String noteStr)
    /* Convert a note string (e.g. "C4", "B5#" into a key. */
    {
        char[] letters = noteStr.toCharArray();
        if (letters.length < 2) {
            System.out.println("Incorrect note syntax; using C4");
            return C4_KEY;
        }
        // look at note letter in letters[0]
        int c_offset = 0;
        if (letters[0] >= 'A' && letters[0] <= 'G') {
            c_offset = cOffsets[letters[0] - 'A'];
        } else {
            System.out.println("Incorrect letter: " + letters[0] + ", using C");
        }
        // look at octave number in letters[1]
        int range = C4_KEY;
        if (letters[1] >= '0' && letters[1] <= '9') {
            range = OCTAVE * (letters[1] - '0' + 1); // plus 1 for midi
        } else {
            System.out.println("Incorrect number: " + letters[1] + ", using 4");
        }
        // look at optional sharp in letters[2]
        int sharp = 0;
        if (letters.length > 2 && letters[2] == '#') {
            sharp = 1; // a sharp is 1 note higher
        }
        // (represented by the black keys on a piano)
        int key = range + c_offset + sharp;
        // System.out.println("note: " + noteStr + "; key: " + key);
        return key;
    } // end of getKey()

    private void makeSong()
    /*
     * The first few notes of "As Time Goes By" from the movie 'Casablanca'. Heinz M. Kabutz created the original MIDI note sequence. See
     * http://www.javaspecialists.co.za/archive/Issue076.html
     */
    {
        changeInstrument(0, 33); // set bank and program; bass
        addRest(7);
        add("F4");
        add("F4#");
        add("F4");
        add("D4#");
        add("C4#");
        add("D4#", 3);
        add("F4");
        add("G4#");
        add("F4#");
        add("F4");
        add("D4#");
        add("F4#", 3);
        add("G4#");
        add("C5#");
        add("C5");
        add("A4#");
        add("G4#");
        add("A4#", 4);
        add("G4", 4);
        add("G4#", 2);
        changeInstrument(0, 15); // dulcimer
        addRest(1);
        add("C5");
        add("D5#");
        add("C5#");
        add("C5");
        add("A4#");
        add("C5", 2);
        add("C5#", 2);
        add("G4#", 2);
        add("G4#", 2);
        add("C4#", 2);
        add("D4#", 2);
        add("C4#", 2);
        addRest(1);
    } // end of makeSong()

    public void meta(MetaMessage meta)
    // called when a meta event occurs during sequence playing
    {
        if (meta.getType() == END_OF_TRACK) {
            System.out.println("End of the track");
            System.exit(0); // not required in J2SE 1.5
        }
    } // end of meta()

    private void programChange(int program)
    /*
     * Change the program setting by adding a PROGRAM_CHANGE message in the track.
     */
    {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.PROGRAM_CHANGE, CHANNEL, program, 0);
            // the second data byte (0) is unused
            MidiEvent event = new MidiEvent(message, tickPos);
            track.add(event);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    } // end of programChange()

    private void setMessage(int onOrOff, int note, int tickPos)
    /*
     * A MidiEvent message is wrapped up inside a ShortMessage before being added to the track.
     */
    {
        if (note < 0 || note > 127) {
            System.out.println("Note outside MIDI range (0-127): " + note);
            return;
        }
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(onOrOff, CHANNEL, note, VOLUME);
            MidiEvent event = new MidiEvent(message, tickPos);
            track.add(event);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    } // end of setMessage()

    // ----------------------------
    private void startSequencer(int tempo)
    /*
     * Start the sequence playing. The tempo setting is in BPM (beats per minute), which is combined with the PPQ (ticks / beat) resolution to
     * determine the speed of playing.
     */
    {
        try {
            sequencer.setSequence(sequence);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        sequencer.addMetaEventListener(this);
        sequencer.start();
        sequencer.setTempoInBPM(tempo);
    } // end of startSequencer()
} // end of SeqSynth class
