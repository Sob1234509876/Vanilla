package top.sob.vanilla.listener;

import top.sob.core.api.event.*;

import top.sob.vanilla.*;
import top.sob.vanilla.api.devTool.debug.*;

/**
 * A basic listener. The main purpose of this listener is to listen init events
 * from the core.
 */
public class BasicListener extends Debugable implements EventListener {

    public void act(Event e) {

        if (e.getType().equals("GTInit")) {
            init.runGTInit();
        } else if (e.getType().equals("GameLogicInit")) {
            init.runGameLogicInit();
        }
    }

}
