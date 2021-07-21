package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class HandleMsgEntity implements Serializable {
    public String id;
    public String imei;
    public String handleStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
