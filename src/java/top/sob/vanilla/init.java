package top.sob.vanilla;

import top.sob.vanilla.thread.*;

/** Inits game logic and gts */
public final class init {

    public static final Thread PGS_THREAD = new PreGameSolver("PGS");

    /** No instance constructing */
    private init() {
    }

    /**
     * Inits the gts
     * 
     * <pre>{@code
     * // За Коммунистическую партию!
     *                                                                 ______-=+__
     *        {=}================|___             ☭__ |      _
     *                                _____________/ ==== \____| `|__
     *                              / -------------------------- \
     *                             |____________________________|
     *                              ][][][][][][][][][][][][][][][][][][][][][
     * 
     * // IDK what is this.
     * }</pre>
     */
    public static void runGTInit() {
        Main.LOGGER.info("Running GT Init");
    }

    /** Inits threads and stuffs */
    public static void runGameLogicInit() {
        Main.LOGGER.info("Running Game Logic Init");
        PGS_THREAD.start();
    }

}
