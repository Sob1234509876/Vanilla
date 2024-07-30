package top.sob.vanilla.thread;

import java.io.*;
// import java.util.*;
import java.net.*;

import top.sob.core.*;
import top.sob.core.api.*;
import top.sob.vanilla.listener.*;

import static top.sob.core.ui.Graphic.INPUT;
import static top.sob.core.ui.Graphic.OUTPUT;
import static top.sob.core.api.meta.SAVES_URI;

import static top.sob.vanilla.api.meta.LANG;
import static top.sob.vanilla.Main.LOGGER;

/**
 * Before you play the game, you have to choose the game-mode and do all kinds
 * of stuff. This thread will ask the user to choose the stuffs.
 */
public final class PreGameSolver extends Thread {

    private static final String PLAY_CMD = "PLAY";
    private static final String CNS_CMD = "CNS";
    private static final String HELP_CMD = "HELP";
    // private static final String MK_SERVER_CMD = "MKS"; // TODO: just finish
    // this!!!!!!

    public static final int DEF_PORT = 10000;
    public static final int DEF_BACKLOG = Runtime.getRuntime().availableProcessors(); // IDK, just think it is 8 or
                                                                                      // whatever
    public static final String LOCAL_SERVER_URL = "http://localhost:10000/cmd/";

    protected static Server server;
    protected static Client client;

    /** @see Thread#Thread() */
    public PreGameSolver() {
        super();
    }

    /**
     * 
     * @param name the name of the new thread.
     * @see Thread#Thread(String)
     */
    public PreGameSolver(String name) {
        super(name);
    }

    /**
     * 
     * @param group see {@link Thread#Thread(ThreadGroup, String)}.
     * @param name  the name of the new thread.
     * @see Thread#Thread(ThreadGroup, String)
     */
    public PreGameSolver(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void run() {
        LOGGER.info("Pre-game cmd solver started!");

        String buffer;

        try {
            while (true) {
                buffer = INPUT.waitAndGetSubmit().toUpperCase();
                // Works now.
                LOGGER.info("User entered \"" + buffer + "\"");

                if (buffer.equals(HELP_CMD)) {
                    writeHelp();
                } else if (buffer.equals(CNS_CMD)) {
                    createNewSave();
                    break;
                } else if (buffer.equals(PLAY_CMD)) {
                    play();
                    break;
                    // The break is a tempoary fix for the pgs not stopping
                    // on getting the submit of INPUT.
                }
            }
        } catch (Exception e) {
            LOGGER.error("Excepton: ", e);
        }
    }

    /** Asks for the save file or server and then plays. */
    private static void play() throws IOException, InterruptedException {

        while (true) {

            OUTPUT.setText(util.getConfig(LANG,
                    "vanilla",
                    "pre",
                    "play",
                    "askMode"));

            String mode = INPUT.waitAndGetSubmit().toUpperCase();
            if (mode.equals("S")) {
                OUTPUT.setText(util.getConfig(LANG,
                        "vanilla",
                        "pre",
                        "play",
                        "askFile"));

                play(new File(INPUT.waitAndGetSubmit()));
                break;

            } else if (mode.equals("M")) {

                OUTPUT.setText(util.getConfig(LANG,
                        "vanilla",
                        "pre",
                        "play",
                        "askHost"));
                String host = INPUT.waitAndGetSubmit();

                OUTPUT.setText(String.format(util.getConfig(LANG,
                        "vanilla",
                        "pre",
                        "play",
                        "askPort"), DEF_PORT));
                String port = INPUT.waitAndGetSubmit();

                OUTPUT.setText(String.format(util.getConfig(LANG,
                        "vanilla",
                        "pre",
                        "play",
                        "askName")));
                String name = INPUT.waitAndGetSubmit();

                URL url = new URL(String.format("http://%s:%s/cmd/", host, port));

                play(url, name);
                break;

            } else {
                OUTPUT.setText(util.getConfig(LANG, "vanilla", "pre", "unknownCmd"));
                Thread.sleep(333);
            }

        }

    }

    /**
     * Plays with the given save file and uses the role local. This method will
     * first init the connection, then it will first start the server then the
     * client.
     * 
     * @apiNote The server is a daemon thread.
     * 
     * @param save the save file.
     * @throws IOException when an I/O exception happens.
     * @see connection#init()
     * @see connection#connect()
     * @see Server
     * @see Client
     */
    private static void play(File save) throws IOException {

        OUTPUT.setText(null);

        plugin.addEventListener(new ClientSideListener());
        plugin.addEventListener(new ServerSideListener());

        server = new Server(DEF_PORT, DEF_BACKLOG, save);
        client = new Client(new URL(LOCAL_SERVER_URL), "YOU");

        server.start();
        client.start();
    }

    /**
     * TODO: Finish this javadoc.
     * 
     * @param url
     * @param name
     * @throws IOException
     */
    private static void play(URL url, String name) throws IOException {
        client = new Client(url, name);
        client.start();
    }

    /** Creates a new save and plays it. */
    private static void createNewSave() throws IOException {
        OUTPUT.setText(util.getConfig(LANG,
                "vanilla",
                "pre",
                "cns"));

        String name = INPUT.waitAndGetSubmit();
        File saveDir = new File(SAVES_URI.getPath(), name);
        saveDir.mkdir(); // Make save directory
        new File(saveDir, "main.dat").createNewFile(); // Make main save file

        OUTPUT.setText(null);

        play(saveDir);
    }

    /**
     * Wait for like 13.7 billion years to get a help manual avalible in every
     * language.
     */
    private static void writeHelp() throws Exception {

        OUTPUT.setText(
                util.getConfig(LANG,
                        "vanilla",
                        "pre",
                        "help"));

    }

}
