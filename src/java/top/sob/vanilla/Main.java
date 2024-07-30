package top.sob.vanilla;

import java.io.*;

import org.apache.log4j.*;

import top.sob.core.api.*;
import top.sob.vanilla.listener.*;

/** This is the class that acts as an entry for the plugin loader. */
public final class Main {

    /** The logger for this */
    public static final Logger LOGGER;
    private static final int BUFFER_SIZE = 8192;

    static {

        LOGGER = Logger.getLogger("Vanilla");

    }

    /** No instance constructing */
    private Main() {
    }

    /**
     * Actually not much is done in only this method. Most of the things are done in
     * {@link BasicListener#act(top.sob.core.api.event.Event)}.
     * 
     * @param args Actually nothing.
     */
    public static void main(String[] args) throws IOException {

        LOGGER.info("Vanilla run! Hooray!");
        init();
        plugin.addEventListener(new BasicListener());

    }

    private static void init() throws IOException {
        boolean isExsist = !new File("tmp/Vanilla/assets/net/").mkdirs();

        // Copy file
        if (!isExsist) {
            File file = new File("tmp/Vanilla/assets/net/index.html");
            InputStream is = Main.class.getClassLoader().getResourceAsStream("assets/net/index.html");
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;

            // I/O
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }

            os.close();
            is.close();
        }
    }
}