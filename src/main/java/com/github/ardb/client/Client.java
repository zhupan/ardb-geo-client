package com.github.ardb.client;

import com.github.ardb.client.util.SafeEncoder;

import java.math.BigDecimal;

public class Client extends BinaryClient implements Commands {

    public Client(final String host) {
        super(host);
    }

    public Client(final String host, final int port) {
        super(host, port);
    }

    @Override
    public void geoadd(String key, BigDecimal longitude, BigDecimal latitude, String code) {
        geoadd(SafeEncoder.encode(key), SafeEncoder.encode(longitude.toString()), SafeEncoder.encode(latitude.toString()), SafeEncoder.encode(code));
    }

    @Override
    public void geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius) {
        geosearch(SafeEncoder.encode(key), SafeEncoder.encode(longitude.toString()), SafeEncoder.encode(latitude.toString()), SafeEncoder.encode(radius.toString()));
    }
}
