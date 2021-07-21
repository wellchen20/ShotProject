package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;

public class VisibleEntity implements Serializable {

    /**
     * id : asd
     * status : 1
     */

    private String id;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
