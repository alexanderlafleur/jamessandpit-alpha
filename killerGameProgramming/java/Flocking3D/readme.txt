
Chapter 23. Flocking Boids

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
Flocking3D.java


-----
Compilation

$ javac *.java
    // you must have Java 3D installed for the compilation to succeed;
    // if you get "Warning" messages, please see the note below


Java 3D is available from http://java.sun.com/products/java-media/3D/


-----
Execution: 

Use the Flocking3D.bat batch file.

There can be up to three arguments which set the number of
predators, prey, and obstacles. Usually, we have no arguments
(then the default values are used) or set all three.

BNF format for the arguments:

$ Flocking3D [ NumPredators [ NumPrey [NumObstacles] ] ]

The [ ...] brackets means the "..." is optional.
e.g. 

$ Flocking3D
     - means use the defaults, 40 predators, 160 prey, 20 obstacles


$ Flocking3D 10 200 15
     - means 10 predators, 200 prey, 15 obstacles


$ Flocking3D 1 30
      - means use 1 predator, 30 prey (and 20 obstacles)


-----------
Note on "unchecked or unsafe operation" Warnings

As explained in chapter 3, I have not used J2SE 5.0's type-safe 
collections, so that this code will compile in early versions of
J2SE (e.g. version 1.4).

The warning messages are always related to my use of collections
(e.g. ArrayList) without specifying a type for the objects they will
contain at run time.

No. of Warnings generated in J2SE 5.0 for the examples:
/Flocking3D: 18 warnings

---------
Last updated: 19th April 2005