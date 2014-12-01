package com.github.ardb.client.integration;

import com.github.ardb.client.Ardb;
import com.github.ardb.client.LocationValue;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Set;

public class ArdbTest {

    Ardb ardb = new Ardb("localhost", 16379);

    @Test
    public void test() {
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97330"), "hotelcode1");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.973301"), "hotelcode9");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97331"), "hotelcode2");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97332"), "hotelcode3");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97333"), "hotelcode4");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97334"), "hotelcode5");
        ardb.geoadd("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.67334"), "hotelcode6");
        ardb.georemove("hotelgeo", "hotelcode2");

        Set<LocationValue> resutl = ardb.geosearch("hotelgeo", new BigDecimal("40.76480"), new BigDecimal("-73.97330"), BigDecimal.valueOf(100));
        System.out.println(resutl);
    }

}
