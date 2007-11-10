
Chapter 32. Networked Tour3D

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

---------
Compilation: 

Use the compileNetTour.bat batch file.

It assumes that the NCSA Portfolio JAR file is in the 
subdirectory /ncsa.

e.g.
$ compileNetTour
    // you must have Java 3D installed for the compilation to succeed;
    // if you get "Warning" messages, please see the note below


Java 3D is available from http://java.sun.com/products/java-media/3D/

---------
Execution (server):

$ java TourServer

---------
Execution (clients):

Use the NetTour3D.bat batch file.

It assumes that the NCSA Portfolio JAR file is in the 
subdirectory /ncsa.

By default, NetTour3D.bat uses tour1.txt in /models
to specify the world's scenery and obstacles.

It takes at most three arguments: <client name> <xPosn> <zPosn>.
If xPosn and zPosn are not included, then the client is placed
at (0,0) on the XZ plane.

Examples:

$ NetTour3D.bat andy
$ NetTour3D.bat paul 2 2

Each client should be started in a separate DOS window.


Note: The client/server code is compiled to run on
the same machine (i.e. the server is running at the 
localhost address).


-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/NetTour3D: 18 warnings

---------
Last updated: 20th April 2005