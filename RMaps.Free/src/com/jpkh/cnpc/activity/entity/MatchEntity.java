package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class MatchEntity implements Serializable {
    /**
     * station : 5059.51602.5
     * nearestImei : 868120236392582
     */

    private String station;
    private String nearestImei;

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getNearestImei() {
        return nearestImei;
    }

    public void setNearestImei(String nearestImei) {
        this.nearestImei = nearestImei;
    }
}
