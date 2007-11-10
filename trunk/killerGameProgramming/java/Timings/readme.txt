
Chapter 2. An Animation Framework

From:
  Killer Game Programming in Java
  Andrew Davison
  O'Reilly, May 2005
  ISBN: 0-596-00730-2
  http://www.oreilly.com/catalog/killergame/
  Web Site for the book: http://fivedots.coe.psu.ac.th/~ad/jg

Contact Address:
  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th


If you use this code, please mention my name, and include a link
to the book's Web site.

Thanks,
  Andrew

---------
There are 5 files here:

TimerRes.java:       Determine the resolution of the Java system timer, 
                     the Java 3D timer, the sun.misc.Perf counter, and
                     System.nanoTime() in J2SE 5.0.

StopWatch.java       A class surrounding the Perf counter; used by TimerRes.java

SleepAcc.java:       Test the accuracy of sleep() with different delays.

SwingTimerTest.java:    Test the accuracy of the javax.swing timer by trying 
                        to repaint a JPanel at a given FPS (frames per second).
                        We measure the actual FPS achieved.

UtilTimerTest.java      Test the accuracy of the java.util timer used with 
                        a TimerTask by to repaint a JPanel at a given FPS.
                        Measure the actual FPS achieved.


Those of you looking for programs using the GamePanel class developed
in chapter 2 should go to chapters 3 and 4.


**NOTE**
========
All of this code uses the Java 3D timer, which is part of the Java 3D 
extension. This means it is *not* part of the standard J2SE distribution;
you will have to download it.

Java 3D is available from http://java.sun.com/products/java-media/3D/


The StopWatch class is built around the sun.misc.Perf counter, which
is only available in J2SE 1.4.2., and later.

--------
Compilation (for all files):

$ javac *.java
    // this will only succeed if Java 3D is installed;
    // the compilation for TimerRes requires J2SE 5.0 for System.nanoTime()

--------
Execution:

$ java TimerRes


$ java SleepAcc


$ java SwingTimerTest <FPS>
e.g
$ java SwingTimerTest 80
    // the JFrame will be updated roughly every second


$ java UtilTimerTest <FPS>
e.g.
$ java UtilTimerTest 100
    // the JFrame will be updated every second

---------
Last updated: 14th April 2005
