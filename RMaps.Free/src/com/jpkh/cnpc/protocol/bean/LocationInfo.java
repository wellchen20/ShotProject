package com.jpkh.cnpc.protocol.bean;

import java.io.Serializable;

public class LocationInfo implements Serializable {
    /**
     * appImei : 12121
     * loginName : ZYRF
     * lat : 23.34343
     * lng : 100.23232
     * address : 北京市xxx路
     * hbTime : 2020-07-30 14:20:22
     */

    private String appImei;
    private String loginName;
    private String lat;
    private String lng;
    private String address;
    private String hbTime;

    public String getAppImei() {
        return appImei;
    }

    public void setAppImei(String appImei) {
        this.appImei = appImei;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHbTime() {
        return hbTime;
    }

    public void setHbTime(String hbTime) {
        this.hbTime = hbTime;
    }
}
