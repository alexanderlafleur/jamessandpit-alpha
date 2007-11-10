
Chapter 27. Terrain Generation with Terragen

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
Terra3D.java

-----
Compilation: 

Use the compileTerra3D.bat batch file.

$ compileTerra3D
    // you must have Java 3D installed for the compilation to succeed;
    // if you get "Warning" messages, please see the note below


Java 3D is available from http://java.sun.com/products/java-media/3D/

It assumes that portfolio.jar is present in the ncsa/ subdirectory.
It can be downloaded from 
http://fivedots.coe.psu.ac.th/~ad/jg/portfolio.jar


-----
Execution

Call the Terra3D.bat file, with the name of an OBJ file
to use as the landscape.

e.g.
$ Terra3D test2

It assumes that portfolio.jar is present in the ncsa/ subdirectory.

There must also be *two* landscape related files in models/:
     test2.obj    // holds the landscape mesh
     test2.jpg    // holds the landscape texture

There must also be a scenery file in models/:
     test2.txt

There can be an optional ground cover (GC) file in models/:
     test2GC.txt

The format of the scenery and GC files is explained in 
chapter 27.

==================================

This distribution includes two examples: test1, test2.

Due to the very large size of the OBJ files, test1.obj and
and test2.obj have been separately zipped, and can be found on the 
web page for chapter 27:

http://fivedots.coe.psu.ac.th/~ad/ch175/
(In May 2005, this directory will be renamed as 
 http://fivedots.coe.psu.ac.th/~ad/ch27/).


-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/Terra3D: 8 warnings

---------
Last updated: 19th April 2005