
Chapter 13. An Isometric Tile Game

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


============================
Compilation:

> javac *.java 
    // if you get "Warning" messages, please see the note below

============================
Execution:

> java AlienTiles


There should be three subdirectories in the current directory:

  Images/        // holds the images used in the games
  Sounds/        // holds the sound clips and MIDI sequence
  World/         // holds two game configuration files: 
                 //      worldInfo.txt and worldObjs.txt

-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/AlienTiles: 23
---------
Last updated: 14th April 2005