package top.sob.vanilla.listener;

import static top.sob.vanilla.Main.LOGGER;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import top.sob.core.api.event.*;
import top.sob.core.ui.Graphic;
import top.sob.vanilla.api.devTool.debug.*;
import top.sob.vanilla.thread.Client;

public class ClientSideListener extends Debugable implements EventListener {

    public static final String GET_TIME_CMD = "TIME";

    public void act(Event e) {

        if (e.getType().equals("ClientSideRecv")) {

            LOGGER.info(e.getNewValue());

            String[] strs = ((String) (e.getNewValue())).split("\n", 2);

            act0(strs[0], strs[1]);

        } else if (e.getType().equals("SubmitCmd")) {
            act1((String) (e.getNewValue()));
        }

    }

    // Output stuffs
    private void act0(String type, String body) {
        if (type.equals("TEXT/PLAIN")) {
            Graphic.OUTPUT.setText(body);
        } else if (type.equals("TEXT/WRAP")) {
            top.sob.vanilla.ui.Graphic.INFO_TEXT.setText(body);
            Graphic.INFO_FRAME.setVisible(true);
        } else if (type.equals("PICT/URL")) {
            try {
                top.sob.vanilla.ui.Graphic.PIC_COMP.setImage(new ImageIcon(new URL(body)));
            } catch (MalformedURLException e) {
                LOGGER.error("Recv malformed url \"" + body + "\":", e);
            }
        }
    }

    private void act1(String in) {
        if (in.equals(GET_TIME_CMD)) {
            Client.REG_API_THING = GET_TIME_CMD;
        } else {
            Client.REG_API_THING = in;
        }
    }

}
