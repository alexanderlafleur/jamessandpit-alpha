package NetBasics.Multicast;

// MultiTimeClient.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Listen for packets coming from the specified multicast group.
 The packets should hold the current date.

 The client runs forever, so doesn't use leaveGroup().
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MultiTimeClient {
    private static final String MHOST = "228.5.6.7";

    private static final int PORT = 6789;

    public static void main(String args[]) throws IOException {
        InetAddress address = InetAddress.getByName(MHOST);
        MulticastSocket msock = new MulticastSocket(PORT);
        msock.joinGroup(address);

        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        String dateStr;
        while (true) {
            // buf = new byte[1024];
            // packet = new DatagramPacket(buf, buf.length);
            msock.receive(packet);
            dateStr = new String(packet.getData()).trim();
            System.out.println(packet.getAddress() + " : " + dateStr);
        }
    } // end of main()

} // end of MultiTimeClient class

