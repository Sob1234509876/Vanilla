package top.sob.vanilla;

import org.apache.log4j.*;

import top.sob.core.api.*;
import top.sob.vanilla.listener.*;

/** This is the class that acts as an entry for the plugin loader. */
public final class Main {

    /** The logger for this */
    public static final Logger LOGGER;

    static {

        LOGGER = Logger.getLogger("Vanilla");

    }

    /** No instance constructing */
    private Main() {
    }

    /**
     * Actually not much is done in only this method. Most of the things are done in
     * {@link PluginListener0#act(top.sob.core.api.event.Event)}.
     * 
     * @param args Actually nothing.
     */
    public static void main(String[] args) {

        LOGGER.info("Vanilla run! Hooray!");
        plugin.addEventListener(new PluginListener0());

    }
}