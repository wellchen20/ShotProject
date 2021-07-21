package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class AlarmImeiEntity implements Serializable {
    String imei;
    private int pageSize;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
