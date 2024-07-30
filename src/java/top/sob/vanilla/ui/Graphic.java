package top.sob.vanilla.ui;

import top.sob.core.util;
import top.sob.core.ui.*;

import static top.sob.vanilla.api.meta.LANG;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

/** Some graphic related objects */
public final class Graphic {

    /** No constructing. */
    private Graphic() {
    }

    public static final GOutput INFO_TEXT = new GOutput();
    public static final GPic PIC_COMP = new GPic();
    public static final JFrame PIC_FRAME = new JFrame(util.getConfig(LANG, "vanilla", "ui", "frameName", "picFrame"));

    /** New */

    public static final Color DEF_BG_COLOR = top.sob.core.ui.Graphic.DEF_BG_COLOR;
    public static final Color DEF_FG_COLOR = top.sob.core.ui.Graphic.DEF_FG_COLOR;
    public static final Border DEF_BORDER = top.sob.core.ui.Graphic.DEF_BORDER;

    /** For some import problems` easy solution (and quite efficient). */

    static {
        INFO_TEXT.setBackground(DEF_BG_COLOR);
        INFO_TEXT.setForeground(DEF_FG_COLOR);
        top.sob.core.ui.Graphic.INFO.getContentPanel().setLayout(new BorderLayout());
        top.sob.core.ui.Graphic.INFO.getContentPanel().add(INFO_TEXT, BorderLayout.CENTER);
    }
}
