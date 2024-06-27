package top.sob.vanilla.api;

import java.io.*;
import java.util.*;

import top.sob.vanilla.*;

import static top.sob.vanilla.Main.LOGGER;
import static top.sob.core.api.meta.LANGUAGE;
import static top.sob.core.api.meta.DEF_CHARSET;

/** Some meta and api stuffs, might be useful. */
public final class meta {

    /** No instance constructing. */
    private meta() {
    }

    /**
     * This is used for loading classes that has constructors that throw exceptions.
     */
    @SuppressWarnings("unused")
    private static Object BufferForLoading;

    public static final ClassLoader THIS_CLASS_LOADER;
    public static final InputStream LANG_STREAM;
    public static final Reader LANG_READER;
    public static final Properties LANG;

    static {

        try {
        } catch (Exception e) {

            LOGGER.fatal("Meta init failed during buffering: ", e);
            System.exit(1);
        }

        THIS_CLASS_LOADER = Main.class.getClassLoader();
        LANG_STREAM = THIS_CLASS_LOADER.getResourceAsStream(
                String.format("assets/lang/%s.lang", LANGUAGE));
        LANG_READER = new InputStreamReader(LANG_STREAM, DEF_CHARSET);
        LANG = new Properties();

        try {
            LANG.load(LANG_READER);
        } catch (Exception e) {

            LOGGER.fatal("Meta init failed during loading: ", e);
            System.exit(1);
        }

    }

}
