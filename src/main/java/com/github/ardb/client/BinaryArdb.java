package com.github.ardb.client;

import java.util.List;

public class BinaryArdb implements BinaryArdbCommands {

    protected Client client = null;

    public BinaryArdb(final String host) {
        client = new Client(host);
    }

    public BinaryArdb(final String host, final int port) {
        client = new Client(host, port);
    }

    public BinaryArdb(final String host, final int port, final int timeout) {
        client = new Client(host, port);
        client.setTimeout(timeout);
    }

    public Long geoadd(final byte[] key, byte[] longitude, byte[] latitude, byte[] code) {
        client.geoadd(key, longitude, latitude, code);
        return client.getIntegerReply();
    }

    @Override
    public List<byte[]> geosearch(byte[] key, byte[] longitude, byte[] latitude, byte[] radius) {
        client.geoadd(key, longitude, latitude, radius);
        return client.getBinaryMultiBulkReply();
    }

    public String auth(final String password) {
        client.auth(password);
        return client.getStatusCodeReply();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Long getDB() {
        return client.getDB();
    }
}