package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class AppImei implements Serializable {

    /**
     * appImei : 863580044345491
     */

    private String appImei;
    private int pageNum;
    private int pageSize;
    private String batchNumber;

    public String getAppImei() {
        return appImei;
    }

    public void setAppImei(String appImei) {
        this.appImei = appImei;
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
}
