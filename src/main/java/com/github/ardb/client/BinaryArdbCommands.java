package com.github.ardb.client;

import java.util.List;

public interface BinaryArdbCommands {

    Long geoadd(byte[] key, byte[] longitude, byte[] latitude, byte[] code);

    List<byte[]> geosearch(byte[] key, byte[] longitude, byte[] latitude, byte[] radius);

}
