package top.sob.vanilla.net;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.Objects;

import top.sob.vanilla.io.*;

/**
 * Provides basic I/O channels for the connection between the client and server.
 */
public class connection {

    public static final int DEF_BACKLOG = 5;

    /** The connection role, or the role this game is currently in. */
    public static enum ConnRole {
        server,
        client,
        local
    }

    /**
     * The client socket.
     * 
     * @see SocketChannel
     */
    protected static SocketChannel clientChannel;
    /**
     * The server socket.
     * 
     * @see ServerSocketChannel
     */
    protected static ServerSocketChannel serverChannel;

    /**
     * The byte channel for connection.
     * 
     * @see ByteChannel
     */
    protected static ByteChannel channel;

    /**
     * The connection type.
     * 
     * @see ConnRole
     */
    protected static ConnRole This;

    /**
     * Inits the connection between the client and server. This connection will not
     * be connected when been init. Note that the client and server both uses
     * {@code INET6} as the protocol.
     * 
     * @param role the connection role.
     * @param host the binded host, if the role is not local.
     * @param port the binded port, if the role is not local.
     * @throws IOException when an I/O exception happens.
     * @see ConnRole
     */
    public static void init(ConnRole role, String host, int port) throws IOException {

        // uses switch because it looks better
        switch (role) {
            case server:

                serverChannel = ServerSocketChannel.open(StandardProtocolFamily.INET6);
                This = ConnRole.server;

                serverChannel.bind(new InetSocketAddress(host, port), DEF_BACKLOG);
                serverChannel.configureBlocking(false); // Not sure is it false at default

                break;

            case client:

                clientChannel = SocketChannel.open(StandardProtocolFamily.INET6);
                This = ConnRole.client;

                clientChannel.bind(new InetSocketAddress(host, port));

                break;

            default:

                channel = PipeChannel.open();
                This = ConnRole.local;

                break;
        }

    }

    /**
     * Creates a local connection. Equivalent to
     * 
     * <pre>{@code
     * init(Conn.local, new String(), 0);
     * }</pre>
     * 
     * @throws IOException {@link #init(ConnRole, String, int)}
     */
    public static void init() throws IOException {
        init(ConnRole.local, new String(), 0);
    }

    /**
     * Connects the local connection. Equivalent to
     * 
     * <pre>{@code
     * connection.connect(null, -1);
     * }</pre>
     */
    public static void connect() throws IOException {
        if (This == ConnRole.local) {
            connect(null, -1);
        } else {
            throw new IllegalArgumentException(String.format("Current connection role is %s, not local", This));
        }
    }

    /**
     * 
     * Connects to another machine (or this if the role is local) with the host name
     * and the port.
     * 
     * @param host the host name.
     * @param port the host port.
     * @return did it worked.
     * @throws IOException when an I/O exception happens
     */
    public static boolean connect(String host, int port) throws IOException {
        switch (This) {
            case server:

                channel = serverChannel.accept(); // Not at blocking mode

                break;

            case client:

                clientChannel.connect(new InetSocketAddress(host, port));
                channel = clientChannel;

                break;
            case local:
                // Nothing needs to be done here.
                break;
        }

        return Objects.nonNull(channel);
    }

    /**
     * Closes the channels and release resources.
     * 
     * @return is it released succesfully.
     * @throws IOException {@link ByteChannel#close()}.
     */
    public static boolean close() throws IOException {

        channel.close();
        channel = null;

        return Objects.isNull(channel);
    }

    /**
     * Reads from the channel {@link #channel}.
     * 
     * @param buffer the byte buffer.
     * @return the amount of bytes read.
     * @throws IOException if an I/O exception occurs.
     */
    public static int read(ByteBuffer buffer) throws IOException {
        return channel.read(buffer);
    }

    /**
     * Writes with the channel {@link #channel}.
     * 
     * @param buffer the byte buffer.
     * @return the amount of bytes wrote.
     * @throws IOException if an I/O exception occurs.
     */
    public static int write(ByteBuffer buffer) throws IOException {
        return channel.write(buffer);
    }

}
