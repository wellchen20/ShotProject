package com.jpkh.cnpc.activity.entity;

public class SetLastestEntity {
    /**
     * code : 200
     * msg : success
     * data : {"imei":"868120241062972","deviceName":"65211141","icon":null,"status":"2","deviceType":null,"lat":"30.690027","lng":"105.319867","station":null,"userId":"0001_02","orgId":null,"staLat":null,"staLng":null,"deviceState":"1","hasAlarm":null,"powerOnState":null,"battery":null}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imei : 868120241062972
         * deviceName : 65211141
         * icon : null
         * status : 2
         * deviceType : null
         * lat : 30.690027
         * lng : 105.319867
         * station : null
         * userId : 0001_02
         * orgId : null
         * staLat : null
         * staLng : null
         * deviceState : 1
         * hasAlarm : null
         * powerOnState : null
         * battery : null
         */

        private String imei;
        private String deviceName;
        private String icon;
        private String status;
        private String deviceType;
        private String lat;
        private String lng;
        private String station;
        private String userId;
        private String orgId;
        private String staLat;
        private String staLng;
        private String deviceState;
        private String hasAlarm;
        private String powerOnState;
        private String battery;

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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
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

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getStaLat() {
            return staLat;
        }

        public void setStaLat(String staLat) {
            this.staLat = staLat;
        }

        public String getStaLng() {
            return staLng;
        }

        public void setStaLng(String staLng) {
            this.staLng = staLng;
        }

        public String getDeviceState() {
            return deviceState;
        }

        public void setDeviceState(String deviceState) {
            this.deviceState = deviceState;
        }

        public String getHasAlarm() {
            return hasAlarm;
        }

        public void setHasAlarm(String hasAlarm) {
            this.hasAlarm = hasAlarm;
        }

        public String getPowerOnState() {
            return powerOnState;
        }

        public void setPowerOnState(String powerOnState) {
            this.powerOnState = powerOnState;
        }

        public String getBattery() {
            return battery;
        }

        public void setBattery(String battery) {
            this.battery = battery;
        }
    }
}
