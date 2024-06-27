package top.sob.vanilla.thread;

import java.io.*;
// import java.util.*;
// import java.net.*;

import top.sob.core.*;
import top.sob.vanilla.api.meta;
import top.sob.vanilla.net.connection;

import static top.sob.core.ui.Graphic.INPUT;
import static top.sob.core.ui.Graphic.OUTPUT;
import static top.sob.core.api.meta.SAVES_URI;

import static top.sob.vanilla.Main.LOGGER;

/**
 * Before you play the game, you have to choose the game-mode and do all kinds
 * of stuff. This thread will ask the user to choose the stuffs.
 */
public final class PreGameSolver extends Thread {

    private static final String PLAY_CMD = "PLAY";
    private static final String CNS_CMD = "CNS";
    private static final String HELP_CMD = "HELP";

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
                } else if (buffer.equals(PLAY_CMD)) {
                    play();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Excepton: ", e);
        }
    }

    /** Asks for the save file or server and then plays. */
    private static void play() {
    }

    /**
     * Plays with the given save file.
     * 
     * @param save the save file.
     */
    private static void play(File save) throws IOException {

        LOGGER.info("Starting a local game...");

        connection.init();
        connection.connect();

        LOGGER.info("Connected to local game.");

        // TODO: write the thread for the server and the client.
    }

    /** Creates a new save and plays it. */
    private static void createNewSave() throws IOException {
        OUTPUT.setText(
                util.getProperty(meta.LANG,
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
                util.getProperty(meta.LANG,
                        "vanilla",
                        "pre",
                        "help"));

    }

}
