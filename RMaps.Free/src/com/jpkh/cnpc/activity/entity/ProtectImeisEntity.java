package com.jpkh.cnpc.activity.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class ProtectImeisEntity implements Serializable {

    /**
     * imeis : ["121212","121212"]
     * type : 1
     */

    private String type;
    private Set<String> imeis;
    private String delay;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getImeis() {
        return imeis;
    }

    public void setImeis(Set<String> imeis) {
        this.imeis = imeis;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
