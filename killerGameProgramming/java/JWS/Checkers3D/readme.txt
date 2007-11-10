
Appendix B. Installation using Java Web Start

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
Checkers3D is a simple Java 3D example from Chapter 15 of the book.

It requires all the JARs and DLLs from Java 3D to be placed in this 
directory:
  * j3dcore.jar, j3daudio.jar, vecmath.jar, j3dutils.jar
  * J3D.dll, j3daudio.dll, J3DUtils.dll 

**NOTE**
These JARs and DLLs are *NOT* here. You should extract 
them from the Java 3D distribution. 
I used Java 3D v1.3.1 Win32/OpenGL, available from
http://java.sun.com/products/java-media/3D/

The steps in converting these files to a JWS installer are 
explained in section 6 of the appendix.

There are a few catches:

  * Checkers3D.java already contains loadLibrary() calls.
    They should be commented out for the code to compile 
    in step 1.

  * mainClass.txt refers to vecmath_signed.jar, j3daudio_signed.jar,
    j3dcore_signed.jar, and j3dutils_signed.jar.
    Edit it to refer to the unsigned JARs, so that you can
    carry out step 1.

  * There is no keystore included. One will be created as
    a byproduct of the first call to keytool in step 3.

  * In step 5, Checkers3D.jnlp will need to be edited to 
    use a file:// for _your_ machine.

  * In step 6, Checkers3D.jnlp's codebase attribute uses my 
    server's URL. Change it to match _your_ server address. 

-----
Last updated: 20th April 2005

