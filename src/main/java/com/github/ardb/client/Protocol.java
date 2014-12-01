package com.github.ardb.client;

import com.github.ardb.client.exceptions.ArdbConnectionException;
import com.github.ardb.client.exceptions.ArdbDataException;
import com.github.ardb.client.util.RedisInputStream;
import com.github.ardb.client.util.RedisOutputStream;
import com.github.ardb.client.util.SafeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Protocol {

    public static final int DEFAULT_PORT = 16379;

    public static final int DEFAULT_TIMEOUT = 2000;

    public static final String CHARSET = "UTF-8";

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    public static final byte PLUS_BYTE = '+';
    public static final byte MINUS_BYTE = '-';
    public static final byte COLON_BYTE = ':';

    public void sendCommand(RedisOutputStream os, Command command, byte[]... args) {
        sendCommand(os, command.raw, args);
    }

    private void sendCommand(RedisOutputStream os, byte[] command, byte[]... args) {
        try {
            os.write(ASTERISK_BYTE);
            os.writeIntCrLf(args.length + 1);
            os.write(DOLLAR_BYTE);
            os.writeIntCrLf(command.length);
            os.write(command);
            os.writeCrLf();

            for (byte[] arg : args) {
                os.write(DOLLAR_BYTE);
                os.writeIntCrLf(arg.length);
                os.write(arg);
                os.writeCrLf();
            }
        } catch (IOException e) {
            throw new ArdbConnectionException(e);
        }
    }

    private void processError(RedisInputStream is) {
        String message = is.readLine();
        throw new ArdbDataException(message);
    }

    private Object process(RedisInputStream is) {
        try {
            byte b = is.readByte();
            if (b == MINUS_BYTE) {
                processError(is);
            } else if (b == ASTERISK_BYTE) {
                return processMultiBulkReply(is);
            } else if (b == COLON_BYTE) {
                return processInteger(is);
            } else if (b == DOLLAR_BYTE) {
                return processBulkReply(is);
            } else if (b == PLUS_BYTE) {
                return processStatusCodeReply(is);
            } else {
                throw new ArdbConnectionException("Unknown reply: " + (char) b);
            }
        } catch (IOException e) {
            throw new ArdbConnectionException(e);
        }
        return null;
    }

    private byte[] processStatusCodeReply(RedisInputStream is) {
        return SafeEncoder.encode(is.readLine());
    }

    private byte[] processBulkReply(RedisInputStream is) {
        int len = Integer.parseInt(is.readLine());
        if (len == -1) {
            return null;
        }
        byte[] read = new byte[len];
        int offset = 0;
        try {
            while (offset < len) {
                offset += is.read(read, offset, (len - offset));
            }
            // read 2 more bytes for the command delimiter
            is.readByte();
            is.readByte();
        } catch (IOException e) {
            throw new ArdbConnectionException(e);
        }

        return read;
    }

    private Long processInteger(RedisInputStream is) {
        String num = is.readLine();
        return Long.valueOf(num);
    }

    private List<Object> processMultiBulkReply(RedisInputStream is) {
        int num = Integer.parseInt(is.readLine());
        if (num == -1) {
            return null;
        }
        List<Object> ret = new ArrayList<Object>(num);
        for (int i = 0; i < num; i++) {
            ret.add(process(is));
        }
        return ret;
    }

    public Object read(RedisInputStream is) {
        return process(is);
    }

    public static byte[] toByteArray(int value) {
        return SafeEncoder.encode(String.valueOf(value));
    }

    public static enum Command {

        PING, DBSIZE, SELECT, AUTH, GEOADD, GEOSEARCH, ZREM;

        public final byte[] raw;

        Command() {
            raw = SafeEncoder.encode(this.name());
        }
    }

    public static enum Keyword {
        RADIUS, ASC, WITHCOORDINATES, WITHDISTANCES, WGS84;
        public final byte[] raw;

        Keyword() {
            raw = SafeEncoder.encode(this.name().toLowerCase());
        }
    }
}
