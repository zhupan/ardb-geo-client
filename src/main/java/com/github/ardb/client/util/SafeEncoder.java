package com.github.ardb.client.util;

import com.github.ardb.client.Protocol;
import com.github.ardb.client.exceptions.ArdbDataException;
import com.github.ardb.client.exceptions.ArdbException;

import java.io.UnsupportedEncodingException;

public class SafeEncoder {

    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new ArdbDataException("value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ArdbException(e);
        }
    }

    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new ArdbException(e);
        }
    }
}
