package com.github.ardb.client;

import com.github.ardb.client.exceptions.ArdbConnectionException;
import com.github.ardb.client.exceptions.ArdbException;
import com.github.ardb.client.util.RedisInputStream;
import com.github.ardb.client.util.RedisOutputStream;
import com.github.ardb.client.util.SafeEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Connection {

    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private Socket socket;
    private Protocol protocol = new Protocol();
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;
    private int timeout = Protocol.DEFAULT_TIMEOUT;

    public Socket getSocket() {
        return socket;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public void setTimeoutInfinite() {
        try {
            socket.setSoTimeout(0);
        } catch (SocketException ex) {
            throw new ArdbException(ex);
        }
    }

    public void rollbackTimeout() {
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException ex) {
            throw new ArdbException(ex);
        }
    }

    public Connection(String host) {
        super();
        this.host = host;
    }

    protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new ArdbConnectionException(e);
        }
    }

    protected Connection sendCommand(Protocol.Command cmd, String... args) {
        final byte[][] bargs = new byte[args.length][];
        for (int i = 0; i < args.length; i++) {
            bargs[i] = SafeEncoder.encode(args[i]);
        }
        return sendCommand(cmd, bargs);
    }

    protected Connection sendCommand(Protocol.Command cmd, byte[]... args) {
        connect();
        protocol.sendCommand(outputStream, cmd, args);
        return this;
    }

    protected Connection sendCommand(Protocol.Command cmd) {
        connect();
        protocol.sendCommand(outputStream, cmd);
        return this;
    }

    public Connection(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), timeout);
                socket.setSoTimeout(timeout);
                outputStream = new RedisOutputStream(socket.getOutputStream());
                inputStream = new RedisInputStream(socket.getInputStream());
            } catch (IOException ex) {
                throw new ArdbConnectionException(ex);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException ex) {
                throw new ArdbConnectionException(ex);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }

    protected String getStatusCodeReply() {
        flush();
        byte[] resp = (byte[]) protocol.read(inputStream);
        if (null == resp) {
            return null;
        } else {
            return SafeEncoder.encode(resp);
        }
    }


    public Long getIntegerReply() {
        flush();
        return (Long) protocol.read(inputStream);
    }

    public List<String> getMultiBulkReply() {
        return BuilderFactory.STRING_LIST.build(getBinaryMultiBulkReply());
    }

    @SuppressWarnings("unchecked")
    public List<byte[]> getBinaryMultiBulkReply() {
        flush();
        return (List<byte[]>) protocol.read(inputStream);
    }


}