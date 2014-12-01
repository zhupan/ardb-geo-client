package com.github.ardb.client;

import java.util.ArrayList;
import java.util.List;

public class BinaryClient extends Connection {

    private String password;

    private long db;

    public BinaryClient(final String host) {
        super(host);
    }

    public BinaryClient(final String host, final int port) {
        super(host, port);
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public void connect() {
        if (!isConnected()) {
            super.connect();
            if (password != null) {
                auth(password);
                getStatusCodeReply();
            }
            if (db > 0) {
                select(Long.valueOf(db).intValue());
                getStatusCodeReply();
            }
        }
    }

    public void select(final int index) {
        db = index;
        sendCommand(Protocol.Command.SELECT, Protocol.toByteArray(index));
    }

    public void ping() {
        sendCommand(Protocol.Command.PING);
    }

    public void dbSize() {
        sendCommand(Protocol.Command.DBSIZE);
    }

    public void geoadd(final byte[] key, byte[] longitude, byte[] latitude, byte[] code) {
        List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.Keyword.WGS84.raw);
        args.add(longitude);
        args.add(latitude);
        args.add(code);
        sendCommand(Protocol.Command.GEOADD, args.toArray(new byte[args.size()][]));
    }

    public void geosearch(final byte[] key, byte[] longitude, byte[] latitude, byte[] radius) {
        List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.Keyword.WGS84.raw);
        args.add(longitude);
        args.add(latitude);
        args.add(Protocol.Keyword.RADIUS.raw);
        args.add(radius);
        args.add(Protocol.Keyword.ASC.raw);
        args.add(Protocol.Keyword.WITHCOORDINATES.raw);
        args.add(Protocol.Keyword.WITHDISTANCES.raw);
        sendCommand(Protocol.Command.GEOSEARCH, args.toArray(new byte[args.size()][]));
    }

    public void auth(final String password) {
        setPassword(password);
        sendCommand(Protocol.Command.AUTH, password);
    }

    public Long getDB() {
        return db;
    }

    public void disconnect() {
        db = 0;
        super.disconnect();
    }
}
