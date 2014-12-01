package com.github.ardb.client;

import java.math.BigDecimal;
import java.util.Set;

public interface ArdbCommands {

    Long geoadd(String key, BigDecimal longitude, BigDecimal latitude, String member);

    Long georemove(String key, String member);

    Set<LocationValue> geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

}
