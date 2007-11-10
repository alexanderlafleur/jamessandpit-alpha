package Timings;

// StopWatch.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A stop watch using the sun.misc.Perf high-res counter. */

import sun.misc.Perf;

public class StopWatch {
    private Perf hiResTimer;

    private long freq;

    private long startTime;

    public StopWatch() {
        this.hiResTimer = Perf.getPerf();
        this.freq = this.hiResTimer.highResFrequency();
    }

    public void start() {
        this.startTime = this.hiResTimer.highResCounter();
    }

    public long stop()
    // return the elapsed time in nanoseconds
    {
        return (this.hiResTimer.highResCounter() - this.startTime) * 1000000000L / this.freq;
    }

    public long getResolution()
    // return counter resolution in nanoseconds
    {
        long diff, count1, count2;

        count1 = this.hiResTimer.highResCounter();
        count2 = this.hiResTimer.highResCounter();
        while (count1 == count2) {
            count2 = this.hiResTimer.highResCounter();
        }
        diff = (count2 - count1);

        count1 = this.hiResTimer.highResCounter();
        count2 = this.hiResTimer.highResCounter();
        while (count1 == count2) {
            count2 = this.hiResTimer.highResCounter();
        }
        diff += (count2 - count1);

        count1 = this.hiResTimer.highResCounter();
        count2 = this.hiResTimer.highResCounter();
        while (count1 == count2) {
            count2 = this.hiResTimer.highResCounter();
        }
        diff += (count2 - count1);

        count1 = this.hiResTimer.highResCounter();
        count2 = this.hiResTimer.highResCounter();
        while (count1 == count2) {
            count2 = this.hiResTimer.highResCounter();
        }
        diff += (count2 - count1);

        return (diff * 1000000000L) / (4 * this.freq);
    } // end of getResolution()

} // end of StopWatch class
