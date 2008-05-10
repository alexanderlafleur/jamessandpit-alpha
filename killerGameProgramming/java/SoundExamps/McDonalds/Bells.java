package SoundExamps.McDonalds;

// Bells.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
public class Bells {
    public static void main(String[] args) {
        // \u0007 is the ASCII bell
        System.out.println("BELL 1\u0007");
        try {
            Thread.sleep(1000); // separate the bells
        } catch (InterruptedException e) {
        }
        // ring the bell again, using Toolkit this time
        java.awt.Toolkit.getDefaultToolkit().beep();
        System.out.println("BELL 2");
        System.out.flush();
    } // end of main()
} // end of Bells
