package com.github.ardb.client;

import java.math.BigDecimal;

public interface Commands {

    void geoadd(String key, BigDecimal longitude, BigDecimal latitude, String member);

    void georemove(String key, String member);

    void geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

}