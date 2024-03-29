package Chat.ChatServlet;

// Chatter.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/* Store information about a single client:
 the user's name, their UID, and the current number
 of messages read from the chat messages list

 The UID is a random integer between 0 and ID_MAX.
 */
public class Chatter {
    private static final int ID_MAX = 1024;
    private int msgsIndex;
    private int uid;
    private String userName;

    public Chatter(String nm) {
        userName = nm;
        uid = (int) Math.round(Math.random() * ID_MAX);
        msgsIndex = 0;
    }

    public int getMsgsIndex() {
        return msgsIndex;
    }

    public int getUID() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public boolean matches(String nm, int id) {
        return userName.equals(nm) && uid == id;
    }

    public void setMsgsIndex(int newIndex) {
        msgsIndex = newIndex;
    }
} // end of Chatter class
