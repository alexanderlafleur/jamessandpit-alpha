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

    private String userName;

    private int uid;

    private int msgsIndex;

    public Chatter(String nm) {
        this.userName = nm;
        this.uid = (int) Math.round(Math.random() * ID_MAX);
        this.msgsIndex = 0;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getUID() {
        return this.uid;
    }

    public int getMsgsIndex() {
        return this.msgsIndex;
    }

    public void setMsgsIndex(int newIndex) {
        this.msgsIndex = newIndex;
    }

    public boolean matches(String nm, int id) {
        return (this.userName.equals(nm) && (this.uid == id));
    }

} // end of Chatter class
