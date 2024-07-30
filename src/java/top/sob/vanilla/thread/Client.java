package top.sob.vanilla.thread;

import static top.sob.core.api.meta.DEF_CHARSET;
import static top.sob.vanilla.Main.LOGGER;

import java.io.*;
import java.net.*;

import top.sob.core.api.*;
import top.sob.core.api.event.*;
import top.sob.core.ui.*;

public class Client extends Thread {

    protected URL url;
    protected final String username;

    public static String REG_API_THING;

    /**
     * Creates a client that is connected to the url given and with the given
     * username. Will throw {@link ProtocolException} if the protocol is not
     * http(s).
     * 
     * @param url      The host.
     * @param username The username.
     * @throws IOException
     */
    public Client(URL url, String username) throws IOException {
        super("Client");
        if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
            throw new ProtocolException(
                    String.format("Illegal protocal %s, only http is allowed", url.getProtocol()));
        }
        this.url = url;
        this.username = username;
    }

    @Override
    public void run() {

        String input;
        HttpURLConnection conn;

        try {

            LOGGER.info("Client started...");

            while (true) {

                conn = initConn(url);
                input = getInput();
                conn.connect();
                LOGGER.info("Connected on " + url + "...");
                write(conn, input);
                showNAfterMath(conn);

            }
        } catch (Exception e) {
            LOGGER.fatal("Fatal exception:", e);
        }
    }

    // Initialize connection.
    private HttpURLConnection initConn(URL url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(4096);

        return conn;
    }

    // Gets inputed string or game commands from user.
    private String getInput() {
        plugin.fireEvent(new Event("SubmitCmd", Graphic.INPUT.waitAndGetSubmit().toUpperCase(), null)); // Fires event
        return REG_API_THING;
    }

    // Writes a string to the server.
    private void write(HttpURLConnection conn, String str) throws IOException {
        LOGGER.info(String.format("Will write \"%s\"...", str));

        conn.getOutputStream().write(str.getBytes(DEF_CHARSET));
    }

    // Shows the received data and shows it (Done in ClietSideListener)
    private void showNAfterMath(HttpURLConnection conn) throws IOException {
        BufferedReader reader;
        String tmp;
        StringBuilder sb = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEF_CHARSET));

        while ((tmp = reader.readLine()) != null) {
            sb.append(tmp + "\n");
        }

        plugin.fireEvent(new Event("ClientSideRecv", sb.toString(), null)); // Fires a new event, especially to
        // ClientSideListener.

        conn.disconnect();
    }

}
