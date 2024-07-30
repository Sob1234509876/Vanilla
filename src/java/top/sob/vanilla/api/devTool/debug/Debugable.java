package top.sob.vanilla.api.devTool.debug;

import java.lang.reflect.*;

/**
 * A funny class, what it does is when invoking the method {@code toString()} it
 * returns a string with the format like this:
 * 
 * <pre>{@code
 * String.format("%s%s", getClass().getName(), strings);
 * }</pre>
 * 
 * Where {@code strings} is an array of fields-value entry of the class extended
 * this
 * abstract class.
 */
public abstract class Debugable {

    @Override
    public String toString() {

        Field[] fields;
        String[] strings;

        fields = getClass().getFields();
        strings = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].canAccess(true);
                strings[i] = String.format("%s=%s", fields[i].getName(), fields[i].get(this));
            } catch (Exception e) {
            }
        }

        return String.format("%s%s", getClass().getName());
    }

}
