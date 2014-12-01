package com.github.ardb.client;

import com.github.ardb.client.util.SafeEncoder;

import java.util.ArrayList;
import java.util.List;

public class BuilderFactory {

    public static final Builder<List<String>> STRING_LIST = new Builder<List<String>>() {
        @SuppressWarnings("unchecked")
        public List<String> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List<byte[]>) data;
            final ArrayList<String> result = new ArrayList<String>(l.size());
            for (final byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(SafeEncoder.encode(barray));
                }
            }
            return result;
        }

        public String toString() {
            return "List<String>";
        }

    };

}
