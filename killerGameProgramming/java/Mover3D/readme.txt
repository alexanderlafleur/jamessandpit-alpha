
Chapter 20. An Articulated, Moveable Figure

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
  Hat Yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th

If you use this code, please mention my name, and include a link
to the book's Web site.

Thanks,
  Andrew

---------------------------------
Compilation: 

$ javac *.java
    // you must have Java 3D installed for the compilation to succeed;
    // if you get "Warning" messages, please see the note below


Java 3D is available from http://java.sun.com/products/java-media/3D/

---------------------------------
Execution:

$ java Mover3D

Move the viewpoint by dragging the mouse. Hold the ALT key and drag to
move in and out.

Use arrow keys to move the figure, or enter limb/figure commands
into the textfield

-------------------------
The textures used by the figure must be in the /textures subdirectory.


Some example commands:

urLeg f 30, lrLeg f -30, ulArm f 10, llArm f 10, chest t 5, head t -5 

f, f, c, c, f, f

-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/Mover3D: 47 warnings

---------
Last updated: 16th April 2005