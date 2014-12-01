package com.github.ardb.client;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Ardb extends BinaryArdb implements ArdbCommands {

    public Ardb(String host) {
        super(host);
    }

    public Ardb(String host, int port) {
        super(host, port);
    }

    public Ardb(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    public String ping() {
        client.ping();
        return client.getStatusCodeReply();
    }

    public Long dbSize() {
        client.dbSize();
        return client.getIntegerReply();
    }

    @Override
    public Long geoadd(String key, BigDecimal longitude, BigDecimal latitude, String code) {
        client.geoadd(key, longitude, latitude, code);
        return client.getIntegerReply();
    }

    @Override
    public Set<LocationValue> geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        client.geosearch(key, longitude, latitude, radius);
        return getResult();
    }

    private Set<LocationValue> getResult() {
        List<String> membersWithScores = client.getMultiBulkReply();
        Set<LocationValue> map = createTreeMap();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            map.add(new LocationValue(iterator.next(), new BigDecimal(iterator.next()), new BigDecimal(iterator.next()), new BigDecimal(iterator.next())));
        }
        return map;
    }

    private Set<LocationValue> createTreeMap() {
        return new TreeSet<LocationValue>();
    }

    public String auth(final String password) {
        client.auth(password);
        return client.getStatusCodeReply();
    }
}
