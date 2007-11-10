
Chapter 30. Network Chat  / Multicasting

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

This directory contains a Chat application where the clients use UDP 
multicasting and a name server.

The client-side class:
* MultiChat

The server-side class:
* NameServer


--------------------------
Compilation:

$ java *.java

The compilation of NameServer generates 1 unchecked
warning in J2SE 5, due to its use of an un-generified ArrayList.

----------------------------
Execution:

1. Start the name server:
     $ java NameServer

2. Start the clients, e.g.
      $ java MultiChat andy
      $ java MultiChat paul



The examples are set up to run on the same machine
(i.e. the server's address is localhost).
 
---------
Last updated: 20th April 2005

