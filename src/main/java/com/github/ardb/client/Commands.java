package com.github.ardb.client;

import java.math.BigDecimal;

public interface Commands {

    void geoadd(String key, BigDecimal longitude, BigDecimal latitude, String code);

    void geosearch(String key, BigDecimal longitude, BigDecimal latitude, BigDecimal radius);

}