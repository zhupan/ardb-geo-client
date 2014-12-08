package com.github.ardb.client;

import java.math.BigDecimal;

public class LocationValue implements Comparable {

    private String member;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal distance;

    public LocationValue(String member, BigDecimal latitude, BigDecimal longitude, BigDecimal distance) {
        this.member = member;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return 1;
        }
        if (distance == null) {
            return -1;
        }
        return distance.compareTo(((LocationValue) o).getDistance());
    }
}
