package top.sob.vanilla.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

import top.sob.vanilla.net.*;

/**
 * This class acts as a wrapper for the class {@link Pipe} because it is not a
 * {@link ByteChannel} and will not compatible with {@link connection} at
 * {@link connection#channel}.
 * 
 * @see Pipe
 * @see ByteChannel
 * @see connection
 */
public class PipeChannel implements ByteChannel {

    /** The orignal pipe. */
    protected final Pipe pipe;
    /** The source that been stored. */
    protected final Pipe.SourceChannel src;
    /** The sink that been stored. */
    protected final Pipe.SinkChannel snk;

    /**
     * The private default constructor, opens a new {@link Pipe} and stores the
     * source and sink.
     * 
     * @throws IOException {@link Pipe#open()}.
     * @see Pipe#open()
     * @see Pipe#source()
     * @see Pipe#sink()
     */
    private PipeChannel() throws IOException {
        pipe = Pipe.open();
        src = pipe.source();
        snk = pipe.sink();
    }

    /**
     * Opens a new pipe channel.
     * 
     * @return the new pipe channel.
     * @throws IOException {@link #PipeChannel()}
     * @see #PipeChannel()
     */
    public static PipeChannel open() throws IOException {
        return new PipeChannel();
    }

    /**
     * @see Pipe.SourceChannel#close()
     * @see Pipe.SinkChannel#close()
     */
    public void close() throws IOException {
        src.close();
        snk.close();
    }

    /**
     * Tells whether or not this channel is open, in this case when both source
     * channel and sink channel are open, it returns {@code true} .
     * 
     * @return {@code true}, if only both source and sink channel are open.
     */
    public boolean isOpen() {
        return src.isOpen() && snk.isOpen();
    }

    /**
     * Tells is the source channel is open.
     * 
     * @return the state of the source channel.
     */
    public boolean isSrcOpen() {
        return src.isOpen();
    }

    /**
     * Tells is the sink channel is open.
     * 
     * @return the state of the sink channel.
     */
    public boolean isSnkOpen() {
        return snk.isOpen();
    }

    /** @see Pipe.SourceChannel#read(ByteBuffer) */
    public int read(ByteBuffer buffer) throws IOException {
        return src.read(buffer);
    }

    /** @see Pipe.SinkChannel#write(ByteBuffer) */
    public int write(ByteBuffer buffer) throws IOException {
        return snk.write(buffer);
    }

}
