package top.sob.vanilla.ui;

import javax.swing.*;

import java.awt.*;
import java.net.*;

import top.sob.core.ui.*;

/** A {@link GWrappedComponent}, not so useful but good enough. */
public final class GPic extends GWrappedComponent {

    protected final JLabel label = new JLabel();

    /**
     * Could be said useless, but still useful in some way. What it does is it
     * returns a new url with the parameter {@code str}, if the url is malformed, it
     * returns null.
     * 
     * @param str The string that will be constructed to a url.
     * @return The urk or null if the url is malformed.
     */
    private static final URL funnyFun(String str) {
        try {
            return new URL(str);
        } catch (Exception e) {
            return null;
        }
    }

    public GPic() {

        this(new ImageIcon(
                funnyFun("https://static.wikia.nocookie.net/omni-com-official/images/2/24/UnknownGt.png")));
    }

    public GPic(ImageIcon icon) {

        label.setIcon(icon);
        label.setBackground(Graphic.DEF_BG_COLOR);
        getContentPanel().setLayout(new BorderLayout());
        getContentPanel().add(label, BorderLayout.CENTER);

        getContentPanel().setBackground(Color.GRAY);
        getContentPanel().setForeground(Graphic.DEF_FG_COLOR);
        getLeftPageButton().setBackground(Graphic.DEF_BG_COLOR);
        getLeftPageButton().setForeground(Graphic.DEF_FG_COLOR);
        getPageText().setBackground(Graphic.DEF_BG_COLOR);
        getPageText().setForeground(Graphic.DEF_FG_COLOR);
        getRightPageButton().setBackground(Graphic.DEF_BG_COLOR);
        getRightPageButton().setForeground(Graphic.DEF_FG_COLOR);

    }

    public ImageIcon setImage(ImageIcon img) {
        label.setIcon(img);
        return img;
    }

}
