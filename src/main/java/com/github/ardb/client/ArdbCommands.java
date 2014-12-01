package com.github.ardb.client;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface ArdbCommands {

    Long geoadd(String key, BigDecimal longitude, BigDecimal latitude, String code);

    Set<LocationValue> geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

}
