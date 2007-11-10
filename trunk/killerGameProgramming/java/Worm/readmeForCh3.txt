
Chapter 3. Worms in Windows and Applets

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
There are 3 sub-directories here that are used in Chapter 3:

/WormP		a windowed application version of the WormChase game
                using the Java 3D timer to drive the animation

/WormPMillis	a windowed application version of the WormChase game
                using System.currentTimeMillis() to drive the animation

/WormApplet	an applet version of the WormChase game
                using the Java 3D timer to drive the animation


Those of you looking for full-screen programs using the GamePanel class 
should go to chapter 4.


**NOTE**
========
The WormP and WormApplet version of the WormChase game use the Java 3D timer, 
which is part of the Java 3D extension. This means it is *not* part of the 
standard J2SE distribution; you will have to download it.

Java 3D is available from http://java.sun.com/products/java-media/3D/

--------
Compilation (for all version of WormChase):

$ cd to the relevant directory (/WormP, /WormPMillis, or /WormApplet)

$ javac *.java
    // this will only succeed for /WormP and /WormApplet
       if Java 3D is installed;
    // if you get "Warning" messages, please see the note below

-------
Execution for the /WormP and /WormPMillis versions WormChase:

$ java WormChase <requested FPS>
e.g.
$ java WormChase 80


Execution for /WormApplet:

$ appletviewer WormChaseApplet.html    // or use your favourite browser


-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/WormApplet: 1 (in the Obstacles class)
/WormP: 1 (in the Obstacles class)
/WormPMillis: 1 (in the Obstacles class)

---------
Last updated: 14th April 2005
