package top.sob.vanilla.thread;

import static top.sob.core.api.meta.DEF_CHARSET;
import static top.sob.vanilla.Main.LOGGER;
import static top.sob.vanilla.api.meta.COPIED_ASSETS_DIR;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import com.sun.net.httpserver.*;

import top.sob.core.api.*;
import top.sob.core.api.event.*;

/**
 * A server thread, but actually just creates a {@link HttpServer} and init it
 * with some context handler ({@link #BackAPIHandler} and
 * {@link #FrontHtmlHandler}) and starts it, not much happens with this thread
 * only.
 */
public class Server extends Thread {

    public final static int EXC_AMOUNT = Runtime.getRuntime().availableProcessors() + 2; // For possible maxium usage of
                                                                                         // cpu.
    public final static int DEF_BUFFER_SIZE = 8192;
    public final static String DEF_INDEX_CONTEXT = "/";
    public final static String DEF_CMD_CONTEXT = "/cmd";

    public static String REG_API_THING;

    protected final HttpServer server;
    protected final File saveFile;

    /**
     * Creates a server
     * 
     * @param port
     * @param backlog
     * @param save
     * @throws IOException
     */
    public Server(int port, int backlog, File save) throws IOException {
        server = HttpServer.create(new InetSocketAddress(Inet6Address.getLoopbackAddress(), port), backlog);
        saveFile = save;
    }

    @Override
    public void run() {

        server.createContext(DEF_CMD_CONTEXT, new BackAPIHandler());
        server.createContext(DEF_INDEX_CONTEXT, new FrontHtmlHandler());

        server.setExecutor(Executors.newFixedThreadPool(EXC_AMOUNT));
        server.start();
        LOGGER.info("Server started...");
    }

    /** An http handler for sending the index html file to the client. */
    private static class FrontHtmlHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {

                File indexFile = new File(COPIED_ASSETS_DIR, "net/index.html");
                InputStream is = new FileInputStream(indexFile);
                OutputStream os = exchange.getResponseBody();
                byte[] buffer = new byte[DEF_BUFFER_SIZE];
                int len;

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, indexFile.length());
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");

                // I/O
                while ((len = is.read(buffer)) > 0) {
                    os.write(buffer, 0, len); // Make sure no nulls are sent
                }

                is.close();
                exchange.close();

            } catch (Exception e) {
                LOGGER.error("Exception detected during front handling:", e);
                // Hope this thing would not throw any sort of exceptions, except internet
                // service problems.
            }

        }

    }

    /** An api http handler for running the game and deal with game commands. */
    private static class BackAPIHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                if (!exchange.getRequestMethod().equals("POST")) {
                    handleIllegal(exchange);
                    return;
                }

                // LOGGER.info(String.format("Valid request from %s.",
                // exchange.getRemoteAddress())); // Logs the request

                String data = readFromExchange(exchange);

                LOGGER.info(String.format("Read \"%s\" from client.", data)); // Logs the data

                plugin.fireEvent(new Event("RecvCmd", data, null)); // Fire event
                sendToExchange(exchange);

            } catch (Exception e) {
                LOGGER.error("Exception detected during back handling:", e);
                // Curse the executors, it never showed up any exception stuff and I wasted
                // about 1 hour.
            }

        }

        // Just reads all the data from the exchange and decodes (Also replaces all the
        // nulls in the decoded string).
        private String readFromExchange(HttpExchange exchange) throws IOException {

            byte[] buffer = new byte[DEF_BUFFER_SIZE];
            int readLen;
            InputStream is = exchange.getRequestBody();

            readLen = is.read(buffer);
            readLen = readLen == -1 ? 0 : readLen;

            return new String(buffer, DEF_CHARSET).replace("\0", "");

        }

        // Sends the string (decoded with the default charset) from the static register
        // api thing.
        private void sendToExchange(HttpExchange exchange) throws IOException {
            byte[] tmp = (REG_API_THING == null ? new byte[1] : REG_API_THING.getBytes(DEF_CHARSET));

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, tmp.length);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=" + DEF_CHARSET.name());
            OutputStream os = exchange.getResponseBody();
            os.write(tmp);
            os.close();
        }

        // Handles illegal requests e.x requests using GET or other methods.
        private void handleIllegal(HttpExchange exchange) throws IOException {
            LOGGER.info(String.format("Invalid request from %s using %s.",
                    exchange.getRemoteAddress(),
                    exchange.getRequestMethod()));
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
        }
    }

    /**
     * Returns the save file (folder) the server is binded with.
     * 
     * @return The save`s abstract path.
     */
    public File getSaveFile() {
        return saveFile;
    }

}
