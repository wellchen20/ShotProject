package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class InfoEntity implements Serializable {
    /**
     * imei :
     * deviceName :
     * station :
     */

    private String imei;
    private String deviceName;
    private String station;
    private int pageNum;
    private int pageSize;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
