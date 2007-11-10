package Chat.Threaded;

// ChatGroup.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Maintains info about all the current clients.

 A single ChatGroup object is used by all the 
 ChatServerHandler threads, so methods which
 manipulate the chatPeople ArrayList must be synchronised
 so that concurrent updates are prevented.

 ChatGroup handles the addition/removal of client details,
 the answering of "who" messages, and the broadcasting of
 a message to all the clients.
 */

import java.io.PrintWriter;
import java.util.ArrayList;

public class ChatGroup {
    private ArrayList chatPeople; // holds a list of Chatter objects

    public ChatGroup() {
        this.chatPeople = new ArrayList();
    }

    synchronized public void addPerson(String cliAddr, int port, PrintWriter out)
    // add a new Chatter object to the list
    {
        this.chatPeople.add(new Chatter(cliAddr, port, out));
        broadcast("Welcome a new chatter (" + cliAddr + ", " + port + ")");
    }

    synchronized public void delPerson(String cliAddr, int port)
    // remove the Chatter object for this person
    {
        Chatter c;
        for (int i = 0; i < this.chatPeople.size(); i++) {
            c = (Chatter) this.chatPeople.get(i);
            if (c.matches(cliAddr, port)) {
                this.chatPeople.remove(i);
                broadcast("(" + cliAddr + ", " + port + ") has departed");
                break;
            }
        }
    } // end of delPerson()

    synchronized public void broadcast(String msg)
    /*
     * Send msg to all the clients, including back to the original sender.
     */
    {
        Chatter c;
        for (int i = 0; i < this.chatPeople.size(); i++) {
            c = (Chatter) this.chatPeople.get(i);
            c.sendMessage(msg);
        }
    } // end of broadcast()

    synchronized public String who()
    /*
     * Returns a string of who is currently logged on, in the form "WHO$$ cliAddr1 & port1 & ... cliAddrN & portN & "
     */
    {
        Chatter c;
        String whoList = "WHO$$ ";
        for (int i = 0; i < this.chatPeople.size(); i++) {
            c = (Chatter) this.chatPeople.get(i);
            whoList += c.toString();
        }
        return whoList;
    } // end of who()

} // end of ChatGroup class
