package com.robert.maps.applib;

import org.andnav.osm.util.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class OnlineSetEntity implements Serializable {
    /**
     * code : 200
     * msg : success
     * data : [{"imei":"868120233783254","deviceName":"GT370-83254","icon":"automobile",
     * "status":"1","posType":"GPS","lat":"29.712344","lng":"106.117253",
     * "hbTime":"2020-07-17 09:57:37","accStatus":"0","speed":"0","gpsTime":"2020-07-17 07:10:33",
     * "activationFlag":"1","expireFlag":"1","electQuantity":"5","locDesc":null,"powerValue":null,"userName":"ZYRF","station":null}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * imei : 868120233783254
         * deviceName : GT370-83254
         * icon : automobile
         * status : 1
         * posType : GPS
         * lat : 29.712344
         * lng : 106.117253
         * hbTime : 2020-07-17 09:57:37
         * accStatus : 0
         * speed : 0
         * gpsTime : 2020-07-17 07:10:33
         * activationFlag : 1
         * expireFlag : 1
         * electQuantity : 5
         * locDesc : null
         * powerValue : null
         * userName : ZYRF
         * station : null
         */
        private int id;
        GeoPoint point;
        private String imei;
        private String deviceName;
        private String icon;
        private String status;
        private String posType;
        private String lat;
        private String lng;
        private String hbTime;
        private String accStatus;
        private String speed;
        private String gpsTime;
        private String activationFlag;
        private String expireFlag;
        private String electQuantity;
        private String locDesc;
        private String powerValue;
        private String userName;
        private String station;
        private String staLat;
        private String staLng;

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

        public String getPosType() {
            return posType;
        }

        public void setPosType(String posType) {
            this.posType = posType;
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

        public String getHbTime() {
            return hbTime;
        }

        public void setHbTime(String hbTime) {
            this.hbTime = hbTime;
        }

        public String getAccStatus() {
            return accStatus;
        }

        public void setAccStatus(String accStatus) {
            this.accStatus = accStatus;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getGpsTime() {
            return gpsTime;
        }

        public void setGpsTime(String gpsTime) {
            this.gpsTime = gpsTime;
        }

        public String getActivationFlag() {
            return activationFlag;
        }

        public void setActivationFlag(String activationFlag) {
            this.activationFlag = activationFlag;
        }

        public String getExpireFlag() {
            return expireFlag;
        }

        public void setExpireFlag(String expireFlag) {
            this.expireFlag = expireFlag;
        }

        public String getElectQuantity() {
            return electQuantity;
        }

        public void setElectQuantity(String electQuantity) {
            this.electQuantity = electQuantity;
        }

        public String getLocDesc() {
            return locDesc;
        }

        public void setLocDesc(String locDesc) {
            this.locDesc = locDesc;
        }

        public String getPowerValue() {
            return powerValue;
        }

        public void setPowerValue(String powerValue) {
            this.powerValue = powerValue;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public GeoPoint getPoint() {
            return point;
        }

        public void setPoint(GeoPoint point) {
            this.point = point;
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
    }

    @Override
    public String toString() {
        return "OnlineSetEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
