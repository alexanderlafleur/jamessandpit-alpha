
Chapter 4. Full-Screen Worms

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
There are 3 sub-directories here that are used in Chapter 4:

/WormAFS        An almost full-screen application version of the WormChase game
                using the Java 3D timer to drive the animation. The JFrame's
		borders and title bars are visible, as are the desktop's
		taskbar.

/WormUPS	A full-screen application version of the WormChase game
                using the Java 3D timer to drive the animation.
		The JFrame covers the entire screen, and is undecorated
		(no borders, title bar).

/WormFSEM	A full-screen application version of the WormChase game
                using the Java 3D timer to drive the animation.
		The JFrame utilizes full-screen exclusive mode.


Those of you looking for windowed programs or an applet using the GamePanel 
class should go to chapter 3.


**NOTE**
========
All the versions of the WormChase game here use the Java 3D timer, 
which is part of the Java 3D extension. This means it is *not* part of the 
standard J2SE distribution; you will have to download it.

Java 3D is available from http://java.sun.com/products/java-media/3D/

--------
Compilation for all versions:

$ cd to the relevant directory (/WormAFS, /WormUFS, or /WormFSEM)

$ javac *.java
    // this will only succeed if Java 3D is installed;
    // if you get "Warning" messages, please see the note below

--------
Execution for all versions:

$ java WormChase <requested FPS>
e.g.
$ java WormChase 80

-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/WormAFS: 1 (in the Obstacles class)
/WormUFS: 1 (in the Obstacles class)
/WormFSEM: 1 (in the Obstacles class)

---------
Last updated: 14th April 2005