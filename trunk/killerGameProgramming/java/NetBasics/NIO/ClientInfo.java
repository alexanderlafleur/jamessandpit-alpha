package NetBasics.NIO;

// ClientInfo.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Details for a single client and methods for receiving and
 sending strings to the client.

 Since client input is non-blocking then the arrival of data
 does not mean that it comprises a complete message. 

 The ongoing message is maintained in inBuffer until
 a '\n' is detected and then the complete string is returned.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

// import java.util.*;

public class ClientInfo {
    private static final int BUFSIZ = 1024; // max size of a message

    private SocketChannel channel;

    private SelectScoreServer ss;

    private ByteBuffer inBuffer;

    private Charset charset; // for decoding bytes --> string

    private CharsetDecoder decoder;

    public ClientInfo(SocketChannel chan, SelectScoreServer ss) {
        this.channel = chan;
        this.ss = ss;
        this.inBuffer = ByteBuffer.allocateDirect(BUFSIZ);
        this.inBuffer.clear();

        this.charset = Charset.forName("ISO-8859-1");
        this.decoder = this.charset.newDecoder();

        showClientDetails();
    } // end of ClientInfo()

    private void showClientDetails()
    // show client details; not used further
    {
        Socket sock = this.channel.socket();
        InetAddress iaddr = sock.getInetAddress();
        System.out.println("Client address: " + iaddr.getHostAddress());
        System.out.println("Client name: " + iaddr.getHostName());
        System.out.println("Client port: " + sock.getPort());
    } // end of showClientDetails()

    public void closeDown() {
        try {
            this.channel.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String readMessage()
    /*
     * readMessage() is called when there are bytes waiting to be read, but this may not be a complete message. The message is stored in inBuffer until a '\n' is detected.
     */
    {
        String inputMsg = null;
        try {
            int numBytesRead = this.channel.read(this.inBuffer);
            if (numBytesRead == -1) { // channel has gone
                this.channel.close();
                this.ss.removeChannel(this.channel); // tell SelectScoreServer
            } else {
                inputMsg = getMessage(this.inBuffer);
            }
        } catch (IOException e) {
            System.out.println("rm: " + e);
            this.ss.removeChannel(this.channel); // tell SelectScoreServer
        }

        return inputMsg;
    } // end of readMessage()

    private String getMessage(ByteBuffer buf) {
        String msg = null;
        int posn = buf.position(); // current buffer sizes
        int limit = buf.limit();
        // System.out.println("Position: " + posn + "; limit: " + limit);

        buf.position(0); // set range of bytes in buffer for translation
        buf.limit(posn);
        try {
            CharBuffer cb = this.decoder.decode(buf);
            msg = cb.toString();
        } catch (CharacterCodingException cce) {
            System.out.println(cce);
        }

        System.out.println("Current msg: " + msg);
        buf.limit(limit); // reset buffer to full range of bytes
        buf.position(posn);

        if (msg.endsWith("\n")) { // we assume '\n' is the last char
            buf.clear();
            return msg;
        }
        return null;
    } // end of getMessage()

    public boolean sendMessage(String msg) {
        String fullMsg = msg + "\r\n";

        ByteBuffer outBuffer = ByteBuffer.allocateDirect(BUFSIZ);
        outBuffer.clear();
        outBuffer.put(fullMsg.getBytes());
        outBuffer.flip();

        boolean msgSent = false;
        try {
            // send the data, don't assume it goes all at once
            while (outBuffer.hasRemaining()) {
                this.channel.write(outBuffer);
            }
            msgSent = true;
        } catch (IOException e) {
            System.out.println(e);
            this.ss.removeChannel(this.channel); // tell SelectScoreServer
        }

        return msgSent;
    } // end of sendMessage()

} // end of ClientInfo class

