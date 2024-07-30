package top.sob.vanilla.listener;

import static top.sob.vanilla.api.meta.LANG;

import top.sob.core.*;
import top.sob.core.api.event.*;
import top.sob.vanilla.api.devTool.debug.*;
import top.sob.vanilla.thread.*;

public class ServerSideListener extends Debugable implements EventListener {

    public static final String PLAIN_TEXT_HEADER = "TEXT/PLAIN\n";
    public static final String WRAP_TEXT_HEADER = "TEXT/WRAP\n";
    public static final String PIC_URL_HEADER = "PICT/URL\n";

    public void act(Event e) {

        if (!e.getType().equals("RecvCmd")) {
            return;
        }

        act0(((String) (e.getNewValue())).split(" "));

    }

    public void act0(String[] e) {
        if (e[0].equals("TIME")) {
            Server.REG_API_THING = WRAP_TEXT_HEADER + System.currentTimeMillis();
        } else {
            Server.REG_API_THING = PLAIN_TEXT_HEADER + util.getConfig(LANG, "vanilla",
                    "game",
                    "text",
                    "unknownCmd");
        }

    }

}