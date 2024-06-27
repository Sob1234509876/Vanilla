package top.sob.vanilla.listener;

import top.sob.core.api.event.*;

import top.sob.vanilla.*;

/**
 * A listener for events. There might be more added in the future so currently
 * it is named {@code PluginListener0}. The main purpose of this listener is to
 * listen init events from the core.
 */
public class PluginListener0 implements EventListener {

    public void act(Event e) {
        Main.LOGGER.debug(e);

        if (e.getType().equals("GTInit")) {
            init.runGTInit();
        } else if (e.getType().equals("GameLogicInit")) {
            init.runGameLogicInit();
        }
    }

}
