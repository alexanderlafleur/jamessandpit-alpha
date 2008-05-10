package NetTour3D;

// TourGroup.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Maintains info about all the current clients.

 A single TourGroup object is used by all the 
 TourServerHandler threads, so methods which
 manipulate the tourPeople ArrayList must be synchronised
 so that concurrent updates are prevented.

 TourGroup handles 
 * the addition/removal of client details
 * broadcasting, but not to the original sender
 * sendTo() for communication with one client

 Very similar to ChatGroup in the multithreaded Chat server.
 */
import java.io.PrintWriter;
import java.util.ArrayList;

public class TourGroup {
    private ArrayList tourPeople;

    // holds a list of TouristInfo objects
    public TourGroup() {
        tourPeople = new ArrayList();
    }

    synchronized public void addPerson(String cliAddr, int port, PrintWriter out) {
        tourPeople.add(new TouristInfo(cliAddr, port, out));
    }

    synchronized public void broadcast(String cliAddr, int port, String msg)
    // broadcast to everyone but original msg sender
    {
        TouristInfo c;
        for (int i = 0; i < tourPeople.size(); i++) {
            c = (TouristInfo) tourPeople.get(i);
            if (!c.matches(cliAddr, port)) {
                c.sendMessage(msg);
            }
        }
    } // end of broadcast()

    synchronized public void delPerson(String cliAddr, int port, String byeMsg)
    // remove tourist and send 'bye' msg to all the others
    {
        TouristInfo c;
        for (int i = 0; i < tourPeople.size(); i++) {
            c = (TouristInfo) tourPeople.get(i);
            if (c.matches(cliAddr, port)) {
                tourPeople.remove(i);
                broadcast(cliAddr, port, byeMsg);
                break;
            }
        }
    } // end of delPerson()

    synchronized public void sendTo(String cliAddr, int port, String msg)
    // send msg to a single cliAddr/port
    {
        TouristInfo c;
        for (int i = 0; i < tourPeople.size(); i++) {
            c = (TouristInfo) tourPeople.get(i);
            if (c.matches(cliAddr, port)) {
                c.sendMessage(msg);
                break;
            }
        }
    } // end of sendTo()
} // end of TourGroup class
