package NetBasics.Multicast;

// MultiTimeServer.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* This server uses multicasting to send out a UDP packet
 containing the current date every second.
 */
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

public class MultiTimeServer {
    private static final String MHOST = "228.5.6.7";
    private static final int PORT = 6789;

    public static void main(String args[]) throws Exception {
        InetAddress address = InetAddress.getByName(MHOST);
        MulticastSocket msock = new MulticastSocket(PORT);
        msock.joinGroup(address);
        DatagramPacket packet;
        System.out.print("Ticking");
        while (true) {
            Thread.sleep(1000);
            System.out.print(".");
            String str = (new Date()).toString();
            packet = new DatagramPacket(str.getBytes(), str.length(), address, PORT);
            msock.send(packet);
        }
    } // end of main()
} // end of MultiTimeServer class
