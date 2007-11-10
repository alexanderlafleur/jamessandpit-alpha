
Chapter 29. Networking Basics  /  Firewall Examples

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

There are four examples in this directory.


1. DayPing.java, DayPing.bat
A TCP client that attempts to contact a specified host at port 13.
The batch file includes proxy settings for **my** system. Change
them as necessary.

2. GetWebPage.java, GetWebPage.bat
An application that retrieves a specified Web page using a URL object.
The batch file includes proxy settings for **my** system. Change
them as necessary.

3. GetWebPageP.java, Base64Converter.java
This variant of GetWebPage.java sets the proxy values inside its
code and requests authorization. The settings and authorization login
ID are for **my** system. Change them as necessary.

4. TimeClient.java, TimeServlet.java
The client contacts the servlet to get the current time and date.
The servlet URL in the client is hardwired to be 
http://localhost:8100/servlet/TimeServlet. Change it as necessary.

---------
Last updated: 19th April 2005

