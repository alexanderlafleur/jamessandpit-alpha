
Chapter 18. 3D Sprites

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


============================
Tour3D.java

This application requires the NCSA Portfolio JAR file
(portfolio.jar), which can be downloaded from 
http://fivedots.coe.psu.ac.th/~ad/jg/portfolio.jar


-----
Compilation: 

Use the compileTour.bat batch file.
It assumes that the NCSA Portfolio JAR is in the subdirectory
ncsa/

$ compileTour
    // you must have Java 3D installed for the compilation to succeed;
    // if you get "Warning" messages, please see the note below


Java 3D is available from http://java.sun.com/products/java-media/3D/

-----
Execution: 

Use the Tour3D.bat batch file.
It assumes that the NCSA Portfolio JAR is in the subdirectory
ncsa/

It is called with a tour filename argument

e.g. 
$ Tour3D ctour

The ctour filename should be in the subdirectory /models as the
text file ctour.txt

-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/Tour3D: 15 warnings

---------
Last updated: 16th April 2005