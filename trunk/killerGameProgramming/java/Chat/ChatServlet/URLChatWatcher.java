package Chat.ChatServlet;

// URLChatWatcher.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A threaded URLChatWatcher object periodically sends a "read" 
 message to the ChatServlet:
 ChatServlet?cmd=read&name=??  + uid cookie

 The response is all the visible messages that have not
 already been read, or "no".

 The messages are displayed in the top-level client's 
 text area by calling its showMsg() method.


 ---- Changes: 30th August 2004
 Server port changed to 8100 from 8080

 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class URLChatWatcher extends Thread {
    private static final int SLEEP_TIME = 2000; // 2 secs between pollings

    private static final String SERVER = "http://localhost:8100/servlet/ChatServlet";

    private URLChat client;

    private String userName;

    private String cookieStr = null;

    public URLChatWatcher(URLChat c, String nm, String cs) {
        this.client = c;
        this.userName = nm;
        this.cookieStr = cs;
    }

    @Override
    public void run()
    // Keep polling forever
    {
        URL url;
        URLConnection conn;
        BufferedReader br;
        String line, response;
        StringBuffer resp;

        try {
            String readRequest = SERVER + "?cmd=read&name=" + URLEncoder.encode(this.userName, "UTF-8");
            while (true) {
                Thread.sleep(SLEEP_TIME);

                url = new URL(readRequest); // send a "read" message
                conn = url.openConnection();

                // Set the cookie value to send
                conn.setRequestProperty("Cookie", this.cookieStr);

                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                resp = new StringBuffer(); // build up the response
                while ((line = br.readLine()) != null) {
                    if (!fromClient(line)) {
                        resp.append(line + "\n");
                    }
                }
                br.close();

                response = resp.toString();
                if ((response != null) && !response.equals("\n")) {
                    this.client.showMsg(response); // show the response
                }
            }
        } catch (Exception e) {
            this.client.showMsg("Servlet Error: watching terminated\n");
            System.out.println(e);
        }
    } // end of run()

    private boolean fromClient(String line)
    // A line (message) is from a client if it begins with (Name)
    {
        if (line.startsWith("(" + this.userName)) {
            return true;
        }
        return false;
    } // end of fromClient()

} // end of URLChatWatcher class
